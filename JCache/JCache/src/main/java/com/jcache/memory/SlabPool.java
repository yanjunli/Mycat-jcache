package com.jcache.memory;

import java.nio.ByteBuffer;

import org.apache.log4j.Logger;

import com.jcache.enums.ItemFlags;
import com.jcache.setting.Settings;

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
	
	public void init(int memLimit){
		int size = Settings.chunkSize+32;
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
		
		//此处留下了第一个没有初始化
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
		
		slabsPreallocate(powerLargest);
	}
	
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
	
	public boolean doSlabsNewSlab(int id){
		SlabClass slabc = slabClassArr[id];
		SlabClass globalc = slabClassArr[Settings.SLAB_GLOBAL_PAGE_POOL];
		int len = (Settings.slabReassign || Settings.slabChunkSizeMax != Settings.slabPageSize) 
				? Settings.slabPageSize : slabc.chunkSize*slabc.perSlab;
		if(memBase > 0 && memAlloced+len > memBase && slabc.perSlab > 0 && globalc.perSlab == 0)
			return false;
		
		Slab gslab = getPagefromGlobalPool();
		Slab slab = memoryAllocate(len);
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
			doSlabsFree(slab.items.get(i), 0, id);
		}
	}
	
	//创建空闲item  
	public void doSlabsFree(Item item, int size, int id){
		if(id < Settings.POWER_SMALLEST || id> powerLargest){
			return;
		}
		
		SlabClass slabc = slabClassArr[id];
		if((item.it_flags & ItemFlags.ITEM_CHUNKED.getFlags()) == 0){
			item.it_flags = ItemFlags.ITEM_SLABBED.getFlags();
			item.slabsClsid = id;
			slabc.requested -= size; //已经申请到的空间数量更新 
			slabc.freeItems.addFirst(item);
		}else{
			//doSlabsFreeChunked(item, size, id, slabc);
		}
	}
	
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
			slab = new Slab(Settings.slabPageSize, size/Settings.slabPageSize);
			slab.buf = buf;
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
			slab = new Slab(Settings.slabPageSize, size/Settings.slabPageSize);
			slab.buf = buf;
		}
		
		memAlloced += size;
		log.info("memory allocated "+slab);
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
