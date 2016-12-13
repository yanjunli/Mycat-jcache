package io.mycat.jcache.memory;

import io.mycat.jcache.enums.ItemFlags;
import io.mycat.jcache.setting.Settings;
import io.mycat.jcache.util.ItemUtil;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;

/*
 * 
 * @author tangww
 * @author liyanjun
 * @author PigBrother
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
		
		Slab slab = null;
		if(grow_slab_list(id)||((slab=getPagefromGlobalPool())==null&&(slab = memoryAllocate(len))==null)){
			log.error("new slab fail from class id "+id);
			return false;
		}
		
		memoryInitSet(slab.buf);
		splitSlabPageInfoFreelist(slab, id);

		/**
		slabc.slabs.addLast(slab);//PigBrother改
		//多线程安全问题
		 * 改变了下面一句代码
		 */
		slabc.slabs.add(slab);

		return true;
	}
	
	public boolean grow_slab_list(int id){
//	    slabclass_t *p = &slabclass[id];
//	    if (p->slabs == p->list_size) {
//	        size_t new_size =  (p->list_size != 0) ? p->list_size * 2 : 16;
//	        void *new_list = realloc(p->slab_list, new_size * sizeof(void *));
//	        if (new_list == 0) return 0;
//	        p->list_size = new_size;
//	        p->slab_list = new_list;
//	    }
//	    return 1;
		
		
		
		return false;
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
			/**
			 * PigBrother
			slabc.sl_curr ++;
			slabc.requested -= size; //已经申请到的空间数量更新

			还是多线程问题
			 *改变了下面二句代码
			 */
			slabc.sl_curr.incrementAndGet();
			slabc.requested.addAndGet(-size);

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
		//PigBrother。
		//原先是removeLast（） ，slabs 是空的 可以这样  但是 如果是 usedslab就不可以了
		return slabc.slabs.remove();
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
