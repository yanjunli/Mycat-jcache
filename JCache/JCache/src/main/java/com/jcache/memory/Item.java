/*
 *  文件创建时间： 2016年11月29日
 *  文件创建者: tangww
 *  所属工程: JCache
 *  CopyRights Received EMail Dev. Dept. 21CN 
 *
 *  备注: 
 */
package com.jcache.memory;

/**
 * 
 * 类功能描述：
 *
 * <p> 版权所有：21CN.com
 * <p> 未经本公司许可，不得以任何方式复制或使用本程序任何部分 <p>
 * 
 * @author <a href="mailto:tangww@corp.21cn.com">tangww</a>
 * @version newEDM
 * @since 2016年11月29日 
 *
 */
public class Item {
	int prev;
	int next;
	int hNext;
	long flushTime;
	long expTime;
	int nbytes;
	short refCount;
	int slabsClsid;
	int nskey;
	byte[] bytes;
	int it_flags;
	
	/**
	 * #define ITEM_key(item) (((char*)&((item)->data)) \
         + (((item)->it_flags & ITEM_CAS) ? sizeof(uint64_t) : 0))
	 * @return
	 */
	public static String itemKey(){
		
		return null;
	}
	
	/**
	 * #define ITEM_data(item) ((char*) &((item)->data) + (item)->nkey + 1 \
         + (item)->nsuffix \
         + (((item)->it_flags & ITEM_CAS) ? sizeof(uint64_t) : 0))
	 * @return
	 */
	public static String itemData(){
		
		return null;
	}
	
	/**
	 * #define ITEM_suffix(item) ((char*) &((item)->data) + (item)->nkey + 1 \
         + (((item)->it_flags & ITEM_CAS) ? sizeof(uint64_t) : 0))
	 * @return
	 */
	public static String itemSuffix(){
		
		return null;
	}
	
	/**
	 * #define ITEM_ntotal(item) (sizeof(struct _stritem) + (item)->nkey + 1 \
         + (item)->nsuffix + (item)->nbytes \
         + (((item)->it_flags & ITEM_CAS) ? sizeof(uint64_t) : 0))
	 */
	public static String itemNtotal(){
		
		return null;
	}
	
	
	
	
	public int getIt_flags() {
		return it_flags;
	}
	public void setIt_flags(int it_flags) {
		this.it_flags = it_flags;
	}
	public int getPrev() {
		return prev;
	}
	public void setPrev(int prev) {
		this.prev = prev;
	}
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public int gethNext() {
		return hNext;
	}
	public void sethNext(int hNext) {
		this.hNext = hNext;
	}
	public long getFlushTime() {
		return flushTime;
	}
	public void setFlushTime(long flushTime) {
		this.flushTime = flushTime;
	}
	public long getExpTime() {
		return expTime;
	}
	public void setExpTime(long expTime) {
		this.expTime = expTime;
	}
	public int getNbytes() {
		return nbytes;
	}
	public void setNbytes(int nbytes) {
		this.nbytes = nbytes;
	}
	public short getRefCount() {
		return refCount;
	}
	public void setRefCount(short refCount) {
		this.refCount = refCount;
	}
	public int getSlabsClsid() {
		return slabsClsid;
	}
	public void setSlabsClsid(int slabsClsid) {
		this.slabsClsid = slabsClsid;
	}
	public int getNskey() {
		return nskey;
	}
	public void setNskey(int nskey) {
		this.nskey = nskey;
	}
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
}
