package com.jcache.memory;

import java.util.LinkedList;

public class SlabClass {
	
	int chunkSize;
	int perSlab;  //perslab 表示每个 slab 可以切分成多少个 chunk, 如果一个 slab 等于1M, 那么就有 perslab = 1M / size
	int requested;
	LinkedList<Slab> slabs;
	LinkedList<Slab> usedSlabs;
	LinkedList<Item> freeItems;
	
	public SlabClass(int chunkSize, int perSlab){
		this.chunkSize = chunkSize;
		this.perSlab = perSlab;
		slabs = new LinkedList<>();
		usedSlabs = new LinkedList<>();
		freeItems = new LinkedList<>();
	}
}
