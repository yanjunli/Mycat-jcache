package io.mycat.jcache.context;

import io.mycat.jcache.hash.Hash;
import io.mycat.jcache.memory.SlabPool;

/**
 * jcache 上下文
 * @author liyanjun
 *
 */
public class JcacheContext {
	
	private Hash hash;
	
	private SlabPool slabPool;

	public Hash getHash() {
		return hash;
	}

	public void setHash(Hash hash) {
		this.hash = hash;
	}

	public SlabPool getSlabPool() {
		return slabPool;
	}

	public void setSlabPool(SlabPool slabPool) {
		this.slabPool = slabPool;
	}
}
