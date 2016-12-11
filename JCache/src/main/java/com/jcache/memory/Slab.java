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
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * @author tangww
 * @author liyanjun
 * @version com.jcache
 * @since 2016年11月29日 
 *
 */
public class Slab {
	final int chunkSize;
	final int itemCount;
	final ByteBuffer buf;
	final BitSet chunkAllocateTrack;
//	LinkedList<Item> items;
	SlabClass slabc; /* 保存SlabClass 引用,用于回收时,将 unused item 放入到  freeItems 链表中 */
	final AtomicBoolean allocLockStatus = new AtomicBoolean(false);
	private final long startAddress;
		
	public Slab(ByteBuffer buf,int chunkSize, int itemCount){
		this.chunkSize = chunkSize;
		this.itemCount = itemCount;
		this.buf = buf;
		chunkAllocateTrack = new BitSet(itemCount);
		startAddress = ((sun.nio.ch.DirectBuffer) buf).address();
	}
	
	public void setSlabClass(SlabClass slabc){
		this.slabc = slabc;
	}
	
	/**
	 * 获取第 chunkindex 个 chunk
	 * @param chunkindex
	 * @return 返回内存地址
	 */
	public Long allocatChunk(int chunkindex){
		while(!allocLockStatus.compareAndSet(false, true)) { }
		 try {
			 if(chunkindex > itemCount){
					return null;
				}
				if(chunkAllocateTrack.get(chunkindex)==false){
					int offStart = chunkindex * chunkSize;
					long addr = startAddress + offStart;
					markChunksUsed(chunkindex,addr);
		            return addr;
				}else{
					return null;
				}
         } finally {
             allocLockStatus.set(false);
         }
	}
    
    private void markChunksUsed(int chunkindex,long addr) {
        chunkAllocateTrack.set(chunkindex);
        slabc.freeItems.remove(addr);
    }

    private void markChunksUnused(int chunkindex,long addr) {
        chunkAllocateTrack.clear(chunkindex);
        slabc.freeItems.add(addr);
    }

    /**
     * 释放 第 chunkindex 块
     * @param parent
     * @param chunkindex
     * @param addr
     * @return
     */
    public boolean recycleBuffer(ByteBuffer parent, int chunkindex,long addr) {
    	
        if (parent == this.buf) {
        	while(!allocLockStatus.compareAndSet(false, true)){}
            try {
                markChunksUnused(chunkindex,addr);
            } finally {
                allocLockStatus.set(false);
            }
            return true;
        }
        return false;
    }

	@Override
	public String toString() {
		return "Slab [chunkSize=" + chunkSize + ", itemCount=" + itemCount + ", memorySize=" + buf.capacity() + "]";
	}
}
