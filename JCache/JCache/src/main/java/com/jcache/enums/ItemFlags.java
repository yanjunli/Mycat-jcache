package com.jcache.enums;

public enum ItemFlags {
	ITEM_LINKED(1),
	ITEM_CAS(2),
	ITEM_SLABBED(4),
	ITEM_FETCHED(8),
	ITEM_ACTIVE(16),
	ITEM_CHUNKED(32),
	ITEM_CHUNK(64);
	
	private int flags;
	
	private ItemFlags(int flags) {
		this.flags = flags;
	}

	public int getFlags() {
		return flags;
	} 
}
