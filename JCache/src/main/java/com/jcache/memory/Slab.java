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
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 
 * 类功能描述：
 *
 * <p> 版权所有：21CN.com
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 <p>
 * 
 * @author <a href="mailto:tangww@corp.21cn.com">tangww</a>
 * @version com.jcache
 * @since 2016年11月29日 
 *
 */
public class Slab {
	int chunkSize;
	int itemCount;
	ByteBuffer buf;
	BitSet chunkAllocateTrack;
	LinkedList<Item> items;
	final AtomicBoolean allocLockStatus = new AtomicBoolean(false);
	
	public Slab(int chunkSize, int itemCount){
		this.chunkSize = chunkSize;
		this.itemCount = itemCount;
		
		chunkAllocateTrack = new BitSet(itemCount);
		items = new LinkedList<>();
	}

	@Override
	public String toString() {
		return "Slab [chunkSize=" + chunkSize + ", itemCount=" + itemCount + ", memorySize=" + buf.capacity() + "]";
	}
}
