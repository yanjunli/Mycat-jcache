package io.mycat.jcache.util;

import io.mycat.jcache.setting.Settings;

/**
 * item 工具
 * @author liyanjun
 * @author PigBrother
 * bytebuffer 组织形式， header 和 data 部分。
 * prev,next,hnext,flushTime,expTime,nbytes,refCount,slabsClisd,it_flags,nsuffix,nskey,CAS,key,suffix,value
 */
public class ItemUtil {
	
	/**
	 * 获取 /记录上一个item的地址,主要用于LRU链和freelist链   
	 * @param item
	 * @return
	 */
	/**
	 * This place had huge bug ,  byte  didn't enough to contain the information the address
	 * This place must be long
	 * @author PigBrother
	 * @param addr
	 * @return
     */
	public static long getPrev(long addr){
		return UnSafeUtil.getByteVolatile(addr);
	}

	/**
	 * This place had huge bug ,  byte  didn't enough to contain the information the address
	 * This place must be long
	 * @author PigBrother
	 * @param addr
	 * @return
	 */
	public static void setPrev(long addr,long prev){
		//This place used Volatile is not necessary.
		//after codes didn't  use   Volatile
		// setNest()
		UnSafeUtil.putLongVolatile(addr, prev);
	}
	
	/**
	 * 记录下一个item的地址,主要用于LRU链和freelist链
	 * @param //item
	 * @return
	 */
	public static long getNext(long addr){
		//return UnSafeUtil.getByte(addr+1);
		return UnSafeUtil.getLong(addr+8);
	}
	/**
	 * This place had huge bug ,  byte  didn't enough to contain the information the address
	 * This place must be long
	 * @author PigBrother
	 * @param addr
	 * @return
	 */
	public static void setNext(long addr,long next){
		///UnSafeUtil.putByte(addr+1, next);   old
		//PigBrother
		UnSafeUtil.putLong(addr+8, next);
	}
	
	/**
	 * 记录HashTable的下一个Item的地址 
	 * @param item
	 * @return
	 */
	/**
	 * This place had huge bug ,  byte  didn't enough to contain the information the address
	 * This place must be long
	 * @author PigBrother
	 * @param addr
	 * @return
	 */
	public static long getHNext(long addr){
		//return UnSafeUtil.getByte(addr+2);
		return UnSafeUtil.getLong(addr+16);
	}
	// fixed setHNext(long addr, long next)
	//PigBrohter
	public static void setHNext(long addr,long hnext){
		UnSafeUtil.putLong(addr+16, hnext);
	}
	/**
	          最近访问的时间，只有set/add/replace等操作才会更新这个字段
	 * 当执行flush命令的时候，需要用这个时间和执行flush命令的时间相比较，来判断是否失效  
	 * typedef unsigned int rel_time_t;
	 * Time relative to server start. Smaller than time_t on 64-bit systems. 
	 * @param //item
	 * @return
	 */
	public static int getFlushTime(long addr){
		//PigBrother
		//return UnSafeUtil.getInt(addr+3);
		return UnSafeUtil.getInt(addr+24);
	}
	
	/**
	 * 缓存的过期时间。设置为0的时候，则永久有效。
	 * 如果Memcached不能分配新的item的时候，设置为0的item也有可能被LRU淘汰
	 * typedef unsigned int rel_time_t;
	 * Time relative to server start. Smaller than time_t on 64-bit systems.
	 * @param //item
	 * @return
	 */
	public static long getExpTime(long addr){
		//PigBrother
		//return UnSafeUtil.getLong(addr+7);
		return UnSafeUtil.getLong(addr+32);
	}
	
	/**
	 * value数据大小
	 * @param //item
	 * @return
	 */
	public static int getNbytes(long addr){
		//PigBrother
		//return UnSafeUtil.getInt(addr+11);
		return UnSafeUtil.getInt(addr+36);
	}
	
	/**
	 * 引用的次数。通过这个引用的次数，可以判断item是否被其它的线程在操作中。
	 * 也可以通过refcount来判断当前的item是否可以被删除，只有refcount -1 = 0的时候才能被删除  
	 * @param //item
	 * @return
	 */
	public static short getRefCount(long addr){
		//PigBrother
		//return UnSafeUtil.getShort(addr+15);
		return UnSafeUtil.getShort(addr+40);
	}
	
	/**
	 * which slab class we're in 标记item属于哪个slabclass下
	 * @param //item
	 * @return
	 */
	public static byte getSlabsClsid(long addr){
		//PigBrother
		//return UnSafeUtil.getByte(addr+17);
		return UnSafeUtil.getByte(addr+42);
	}
	
	public static void setSlabsClsid(long addr,byte clsid){
		// PigBrother
		// /UnSafeUtil.putByte(addr+17, clsid);
		UnSafeUtil.putByte(addr+42, clsid);
	}
	
	/**
	 * ITEM_* above
	 * @param //item
	 * @return
	 */
	public static byte getItflags(long addr){
		// PigBrother
		// 这个memcached里 是一个int  用 byte表示是否够了？
		// /return UnSafeUtil.getByte(addr+18);
		return UnSafeUtil.getByte(addr+43);
	}
	
	public static void setItflags(long addr,byte flags){
		// PigBrother
		//UnSafeUtil.putByte(addr+18,flags);
		UnSafeUtil.putByte(addr+43,flags);
	}
	
	/**
	 * length of flags-and-length string
	 * @param //item
	 * @return
	 */
	public static byte getNsuffix(long addr){
		// PigBrother
		//return UnSafeUtil.getByte(addr+19);
		return UnSafeUtil.getByte(addr+43);
	}
	
	/**
	 * length of flags-and-length string
	 * @param //item
	 * @return
	 */
	public static byte getNskey(long addr){
		// PigBrother
		//return UnSafeUtil.getByte(addr+20);
		return UnSafeUtil.getByte(addr+44);
	}
	
	/**
	 * Generates the variable-sized part of the header for an object.
	 *
	 * key     - The key
	 * nkey    - The length of the key
	 * flags   - key flags
	 * nbytes  - Number of bytes to hold value and addition CRLF terminator
	 * suffix  - Buffer for the "VALUE" line suffix (flags, size).
	 * nsuffix - The length of the suffix is stored here.
	 *
	 * Returns the total size of the header.
	 */	
	public static int item_make_header(byte nkey,int flags,int nbytes,long suffix,long nsuffix){
	    /* suffix is defined at 40 chars elsewhere.. */
//	    *nsuffix = (uint8_t) snprintf(suffix, 40, " %u %d\r\n", flags, nbytes - 2);
//	    return sizeof(item) + nkey + *nsuffix + nbytes;
		StringBuffer sb = new StringBuffer();
		sb.append(" ").append(flags).append(" ").append((nbytes-2));
		String suffixStr = sb.toString();
		
		if(suffixStr.length()>40){
			suffixStr = suffixStr.substring(0, 40);
		}
		UnSafeUtil.setBytes(suffix, suffixStr.getBytes(), 0, suffixStr.length());
		UnSafeUtil.putByte(nsuffix, (byte)suffixStr.length());
		return Settings.ITEM_HEADER_LENGTH + nkey + suffixStr.length() + nbytes;
	}

}
