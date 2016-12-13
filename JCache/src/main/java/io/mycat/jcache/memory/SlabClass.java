package io.mycat.jcache.memory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author tangww
 * @author liyanjun
 * @author PigBrother
 *
 */
public class SlabClass {
	
	int chunkSize;  /* sizes of items  当前的slabclass存储items的总大小  */

	int perSlab;    /* perslab 表示每个 slab 可以切分成多少个 chunk, 如果一个 slab 等于1M, 那么就有 perslab = 1M / size . how many items per slab */
	
	/**
	 * 这里存放的是 item 的内存首地址
	 *
	 * PigBrother改
	 * LinkedList 是线程不安全的  后面也没有看见synchronized 以及用Atomic类来 保证原子性
	 * 故认为要 将 LinkedList --->   LinkedBlockingQueue
	 */
	LinkedBlockingQueue<Long> freeItems;
	
	/**
	 * 这个字段和  usedSlabs 字段有什么区别？
	 */
	//TODO 这个字段和  usedSlabs 字段有什么区别？
	LinkedBlockingQueue<Slab> slabs;
	
	/**
	 * 分配的slab链表
	 */
	LinkedBlockingQueue<Slab> usedSlabs;
	
	/**
	 * 当前总共剩余多少个空闲的item
	 * 当sl_curr=0的时候，说明已经没有空闲的item，需要分配一个新的slab（每个1M，可以切割成N多个Item结构）
	 * 下面的变量 也是 没有原子性   由于 slabclas 一定是 多个操作同时进行的
	 * 故 在SlabPool里  用 ++  一定会出现 多线程安全问题
	 */
	AtomicInteger sl_curr; /* total free items in list */
	
	/**
	 * 总共分配多少个slabs
	 */
	AtomicInteger allocatedslabs; /* how many slabs were allocated for this class */
		
	/**
	 * 还不清楚是干什么用的
	 */
	//TODO 还不清楚是干什么用的
	AtomicInteger list_size;  /* size of prev array */
	
	/**
	 * 总共请求的总byte  用于统计
	 */
	AtomicInteger requested;  /* The number of requested bytes */
	
	public SlabClass(int chunkSize, int perSlab){
		this.chunkSize = chunkSize;
		this.perSlab = perSlab;
		slabs = new LinkedBlockingQueue<>();
		usedSlabs = new LinkedBlockingQueue<>();
		freeItems = new LinkedBlockingQueue<>();
	}
}
