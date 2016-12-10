/*
 *  文件创建时间： 2016年11月29日
 *  文件创建者: tangww
 *  所属工程: JCache
 *  CopyRights Received EMail Dev. Dept. 21CN 
 *
 *  备注: 
 */
package com.jcache.memory;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import org.apache.log4j.Logger;

import com.jcache.setting.Settings;

/**
 * 
 * 类功能描述：
 *
 * <p> 版权所有：21CN.com
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 <p>
 * 
 * @author <a href="mailto:tangww@corp.21cn.com">tangww</a>
 * @version 
 * @since 2016年11月29日 
 *
 */
public class SlabClass1 {
	Logger log = Logger.getLogger(SlabClass.class);
	
//	Slab[] slabs; //array of slab pointers
	LinkedList<Slab> slabs;
	int memBase;
	int memAlloced;
	
	public SlabClass1(){
//		slabs = new Slab[Settings.MAX_NUMBER_OF_SLAB_CLASSES];
		slabs = new LinkedList<>();
	}
	
	public void init(){
		int size = Settings.chunkSize+32;
		if(!Settings.prealloc){
			log.info(" prealloc ? "+Settings.prealloc );
			return;
		}
		
		ByteBuffer baseBuf = null;
		try{
			baseBuf = ByteBuffer.allocateDirect(Settings.MAX_NUMBER_OF_SLAB_CLASSES*Settings.itemSizeMax);
			memBase = baseBuf.capacity();
		}catch(Exception e){
			log.error(" Init allocate direct buffer fail.Will allocate in smaller chunks. For "+e.getMessage(), e);
			return;
		}
		
		//此处留下了第一个没有初始化
		int i = Settings.POWER_SMALLEST;
		for(; i<Settings.MAX_NUMBER_OF_SLAB_CLASSES-1 && size<=Settings.slabChunkSizeMax/Settings.factor; i++){
			if(size % Settings.CHUNK_ALIGN_BYTES != 0)
				size += Settings.CHUNK_ALIGN_BYTES-(size % Settings.CHUNK_ALIGN_BYTES);
			
//			slabs[i].chunkSize = size;
//			slabs[i].itemCount = Settings.itemSizeMax/size;
			int itemCount = Settings.slabPageSize/size;
			slabs.add(new Slab(size, itemCount));
			size *= Settings.factor;
			
			log.info("slab class "+i+": chunk size "+size+" item count "+itemCount);
		}
		
		int powerLargest = i; 
//		slabs[powerLarget].chunkSize = Settings.itemSizeMax;
//		slabs[powerLarget].itemCount = 1;
		slabs.add(new Slab(Settings.slabChunkSizeMax, Settings.slabPageSize/Settings.slabChunkSizeMax));
		log.info("slab class "+i+": chunk size "+size+" item count "+1);
		
		slabsPreallocate(powerLargest);
	}
	
	public void slabsPreallocate(int slabCount){
		int prealloc = 0;
		for(int i=Settings.POWER_SMALLEST; i<Settings.POWER_LARGEST; i++){
			if(++prealloc > slabCount)
				return;
			if(!doSlabsNewSlab(i)){
				log.error("Error while preallocating slab memory!\n If using -L or other prealloc options, max memory must be "
						+"at least "+slabs.size()+" megabytes.\n");
				return;
			}
		}
	}
	
	public boolean doSlabsNewSlab(int id){
		Slab slab = slabs.get(id);
		Slab global = slabs.get(Settings.SLAB_GLOBAL_PAGE_POOL);
		int len = (Settings.slabReassign || Settings.slabChunkSizeMax != Settings.slabPageSize) 
				? Settings.slabPageSize : slab.chunkSize*slab.itemCount;
		if(memBase > 0 && memAlloced+len > memBase && slab.itemCount > 0 && global.itemCount == 0)
			return false;
		
		return false;
	}
	
//	public boolean growSlabList(id){
//		
//	}
	
}
