package com.jcache.memory;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.jcache.enums.ItemFlags;
import com.jcache.setting.Settings;
import com.jcache.util.ItemUtil;

/*
 * 
 * @author tangww
 * @author liyanjun
 *
 */
public class SlabPool {
	Logger log = Logger.getLogger(SlabPool.class);
	
	SlabClass[] slabClassArr;
	int memBase;
	int memAlloced;
	int powerLargest;
	ByteBuffer baseBuf = null;
	
	public SlabPool(){
		this.slabClassArr = new SlabClass[Settings.MAX_NUMBER_OF_SLAB_CLASSES];
	}
	
	public int slabsClassid(int size){
		int res = Settings.POWER_SMALLEST;
		if(size <= 0 || size > Settings.itemSizeMax)
			return 0;
		while(size > slabClassArr[res].chunkSize){
			res ++;
			if(res == powerLargest)
				return powerLargest;
		}
		
		return res;
	}
	
	/** Init the subsystem. 1st argument is the limit on no. of bytes to allocate,
	    0 if no limit. 2nd argument is the growth factor; each slab will use a chunk
	    size equal to the previous slab's chunk size times this factor.
	    3rd argument specifies if the slab allocator should allocate all memory
	    up front (if true), or allocate memory in chunks as it is needed (if false)
	*/
	public void init(int memLimit){
		int size = Settings.chunkSize+Settings.ITEM_HEADER_LENGTH;
		if(!Settings.prealloc){
			log.info(" prealloc ? "+Settings.prealloc );
			return;
		}
		
		try{
			baseBuf = ByteBuffer.allocateDirect(memLimit);
			memBase = memLimit;
		}catch(Exception e){
			log.error(" Init allocate direct buffer fail.Will allocate in smaller chunks. For "+e.getMessage(), e);
			return;
		}
		
		/* 此处留下了第一个没有初始化   magic slab class for storing pages for reassignment see Settings.SLAB_GLOBAL_PAGE_POOL   */
		int i = Settings.POWER_SMALLEST;
		for(; i<Settings.MAX_NUMBER_OF_SLAB_CLASSES-1 && size<=Settings.slabChunkSizeMax/Settings.factor; i++){
			if(size % Settings.CHUNK_ALIGN_BYTES != 0)
				size += Settings.CHUNK_ALIGN_BYTES-(size % Settings.CHUNK_ALIGN_BYTES);
			
			slabClassArr[i].chunkSize = size;
			slabClassArr[i].perSlab = Settings.slabPageSize/size;
			size *= Settings.factor;

			log.info("slab class "+i+": chunk size "+size+" item count "+slabClassArr[i].perSlab);
		}
		
		this.powerLargest = i; 
		slabClassArr[powerLargest].chunkSize = Settings.itemSizeMax;
		slabClassArr[powerLargest].perSlab = Settings.slabPageSize/Settings.slabChunkSizeMax;
		log.info("slab class "+i+": chunk size "+size+" item count "+slabClassArr[powerLargest].perSlab);
		
		/* 预分配slab 在 slabclasss 中 */
		slabsPreallocate(powerLargest);
	}
	
	/**
	 * 预分配slab 在 slabclasss 中 
	 * @param slabCount
	 */
	public void slabsPreallocate(int slabCount){
		int prealloc = 0;
		for(int i=Settings.POWER_SMALLEST; i<Settings.MAX_NUMBER_OF_SLAB_CLASSES; i++){
			if(++prealloc > slabCount)
				return;
			if(!doSlabsNewSlab(i)){
				log.error("Error while preallocating slab memory!\n If using -L or other prealloc options, max memory must be "
						+"at least "+slabCount+" megabytes.\n");
				return;
			}
		}
	}
	
	/**
	 * 
	 */
	public boolean doSlabsNewSlab(int id){
		SlabClass slabc = slabClassArr[id];
		SlabClass globalc = slabClassArr[Settings.SLAB_GLOBAL_PAGE_POOL];
		int len = (Settings.slabReassign || Settings.slabChunkSizeMax != Settings.slabPageSize) 
				? Settings.slabPageSize : slabc.chunkSize*slabc.perSlab;
		if(memBase > 0 && memAlloced+len > memBase && slabc.perSlab > 0 && globalc.perSlab == 0)
			return false;
		
		Slab gslab = getPagefromGlobalPool();
		Slab slab = memoryAllocate(len);
		//TODO grow_slab_list(id)
		if(gslab == null && slab == null){
			log.error("new slab fail from class id "+id);
			return false;
		}
		
		memoryInitSet(slab.buf);
		splitSlabPageInfoFreelist(slab, id);
		
		slabc.slabs.addLast(slab);
		
		return true;
	}
	
	//将ptr指向的内存空间按第id个slabclass的size进行切分
	public void splitSlabPageInfoFreelist(Slab slab, int id){
		SlabClass slabc = slabClassArr[id];
		//每个slabclass有多个slab,对每个slab按slabclass对应的size进行切分
		for(int i=0; i<slabc.perSlab; i++){
			doSlabsFree(slab.allocatChunk(i), 0, id);
		}
	}
	
	//创建空闲item  
	public void doSlabsFree(long addr, int size, int id){
		if(id < Settings.POWER_SMALLEST || id> powerLargest){
			return;
		}
		SlabClass slabc = slabClassArr[id];
		int it_flags = ItemUtil.getItflags(addr);
		if((it_flags & ItemFlags.ITEM_CHUNKED.getFlags()) == 0){
			ItemUtil.setItflags(addr, ItemFlags.ITEM_SLABBED.getFlags());
			ItemUtil.setSlabsClsid(addr, (byte)id);
			slabc.sl_curr ++;
			slabc.requested -= size; //已经申请到的空间数量更新 
		}else{
			//doSlabsFreeChunked(item, size, id, slabc);
		}
	}
	
	/* Fast FIFO queue */
	public Slab getPagefromGlobalPool(){
		SlabClass slabc = slabClassArr[Settings.SLAB_GLOBAL_PAGE_POOL];
		if(slabc.slabs.size() < 1){
			return null;
		}
		
		return slabc.slabs.removeLast();
	}
	
	public Slab memoryAllocate(int size){
		Slab slab = null;
		if(baseBuf == null){
			ByteBuffer buf = ByteBuffer.allocateDirect(size);
			slab = new Slab(buf,Settings.slabPageSize, size/Settings.slabPageSize);
		}else{
			if(size > (memBase-memAlloced)){
				return null;
			}
			
			if(size % Settings.CHUNK_ALIGN_BYTES != 0){
				size += Settings.CHUNK_ALIGN_BYTES - (size % Settings.CHUNK_ALIGN_BYTES);
			}
			
			baseBuf.position(memAlloced);
			if(size < (memBase-memAlloced)){
				baseBuf.limit(memAlloced+size);
			}else{
				baseBuf.limit(memBase);
			}
			ByteBuffer buf = baseBuf.slice();
			slab = new Slab(buf,Settings.slabPageSize, size/Settings.slabPageSize);
		}
		
		memAlloced += size;
		if(log.isInfoEnabled()){
			log.info("memory allocated "+slab);
		}
		return slab;
	}
	
	
	public void memoryInitSet(ByteBuffer buf){
		buf.position(0);
		buf.limit(buf.capacity());
		
		// fill with zeroes to ensure deterministic behavior upon handling 'uninitialized' data
	    for (int i = 0, n = buf.remaining(); i < n; i++) {
	    	buf.put(i, (byte) 0);
	    }
	    
	    buf.position(0);
	}
	
}
