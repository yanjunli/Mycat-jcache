package com.jcache.memory;

import java.util.LinkedList;

/**
 * 
 * @author tangww
 * @author liyanjun
 *
 */
public class SlabClass {
	
	int chunkSize;  /* sizes of items  当前的slabclass存储items的总大小  */

	int perSlab;    /* perslab 表示每个 slab 可以切分成多少个 chunk, 如果一个 slab 等于1M, 那么就有 perslab = 1M / size . how many items per slab */
	
	/**
	 * 这里存放的是 item 的内存首地址
	 */
	LinkedList<Long> freeItems;
	
	/**
	 * 这个字段和  usedSlabs 字段有什么区别？
	 */
	//TODO 这个字段和  usedSlabs 字段有什么区别？
	LinkedList<Slab> slabs;
	
	/**
	 * 分配的slab链表
	 */
	LinkedList<Slab> usedSlabs;
	
	/**
	 * 当前总共剩余多少个空闲的item
	 * 当sl_curr=0的时候，说明已经没有空闲的item，需要分配一个新的slab（每个1M，可以切割成N多个Item结构）
	 */
	int sl_curr; /* total free items in list */
	
	/**
	 * 总共分配多少个slabs
	 */
	int allocatedslabs; /* how many slabs were allocated for this class */
		
	/**
	 * 还不清楚是干什么用的
	 */
	//TODO 还不清楚是干什么用的
	int list_size;  /* size of prev array */
	
	/**
	 * 总共请求的总byte  用于统计
	 */
	int requested;  /* The number of requested bytes */
	
	public SlabClass(int chunkSize, int perSlab){
		this.chunkSize = chunkSize;
		this.perSlab = perSlab;
		slabs = new LinkedList<>();
		usedSlabs = new LinkedList<>();
		freeItems = new LinkedList<>();
	}
}
