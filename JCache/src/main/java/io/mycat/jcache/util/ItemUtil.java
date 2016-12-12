package io.mycat.jcache.util;

/**
 * item 工具
 * @author liyanjun
 * bytebuffer 组织形式， header 和 data 部分。
 * prev,next,hnext,flushTime,expTime,nbytes,refCount,slabsClisd,it_flags,nsuffix,nskey,CAS,key,suffix,value
 */
public class ItemUtil {
	
	/**
	 * 获取 /记录上一个item的地址,主要用于LRU链和freelist链   
	 * @param item
	 * @return
	 */
	public static byte getPrev(long addr){
		return UnSafeUtil.getByte(addr);
	}
	
	public static void setPrev(long addr,byte prev){
		UnSafeUtil.putByte(addr, prev);
	}
	
	/**
	 * 记录下一个item的地址,主要用于LRU链和freelist链
	 * @param item
	 * @return
	 */
	public static byte getNext(long addr){
		return UnSafeUtil.getByte(addr+1);
	}
	
	public static void setNext(long addr,byte next){
		UnSafeUtil.putByte(addr+1, next);
	}
	
	/**
	 * 记录HashTable的下一个Item的地址 
	 * @param item
	 * @return
	 */
	public static byte getHNext(long addr){
		return UnSafeUtil.getByte(addr+2);
	}
	
	/**
	          最近访问的时间，只有set/add/replace等操作才会更新这个字段
	 * 当执行flush命令的时候，需要用这个时间和执行flush命令的时间相比较，来判断是否失效  
	 * typedef unsigned int rel_time_t;
	 * Time relative to server start. Smaller than time_t on 64-bit systems. 
	 * @param item
	 * @return
	 */
	public static int getFlushTime(long addr){
		return UnSafeUtil.getInt(addr+3);
	}
	
	/**
	 * 缓存的过期时间。设置为0的时候，则永久有效。
	 * 如果Memcached不能分配新的item的时候，设置为0的item也有可能被LRU淘汰
	 * typedef unsigned int rel_time_t;
	 * Time relative to server start. Smaller than time_t on 64-bit systems.
	 * @param item
	 * @return
	 */
	public static long getExpTime(long addr){
		return UnSafeUtil.getLong(addr+7);
	}
	
	/**
	 * value数据大小
	 * @param item
	 * @return
	 */
	public static int getNbytes(long addr){
		return UnSafeUtil.getInt(addr+11);
	}
	
	/**
	 * 引用的次数。通过这个引用的次数，可以判断item是否被其它的线程在操作中。
	 * 也可以通过refcount来判断当前的item是否可以被删除，只有refcount -1 = 0的时候才能被删除  
	 * @param item
	 * @return
	 */
	public static short getRefCount(long addr){
		return UnSafeUtil.getShort(addr+15);
	}
	
	/**
	 * which slab class we're in 标记item属于哪个slabclass下
	 * @param item
	 * @return
	 */
	public static byte getSlabsClsid(long addr){
		return UnSafeUtil.getByte(addr+17);
	}
	
	public static void setSlabsClsid(long addr,byte clsid){
		UnSafeUtil.putByte(addr+17, clsid);
	}
	
	/**
	 * ITEM_* above
	 * @param item
	 * @return
	 */
	public static byte getItflags(long addr){
		return UnSafeUtil.getByte(addr+18);
	}
	
	public static void setItflags(long addr,byte flags){
		UnSafeUtil.putByte(addr+18,flags);
	}
	
	/**
	 * length of flags-and-length string
	 * @param item
	 * @return
	 */
	public static byte getNsuffix(long addr){
		return UnSafeUtil.getByte(addr+19);
	}
	
	/**
	 * length of flags-and-length string
	 * @param item
	 * @return
	 */
	public static byte getNskey(long addr){
		return UnSafeUtil.getByte(addr+20);
	}

}
