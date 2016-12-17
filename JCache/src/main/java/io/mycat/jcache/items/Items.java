package io.mycat.jcache.items;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;

import io.mycat.jcache.context.JcacheContext;
import io.mycat.jcache.enums.ItemFlags;
import io.mycat.jcache.enums.LRU_TYPE_MAP;
import io.mycat.jcache.memhashtable.HashTable;
import io.mycat.jcache.net.JcacheGlobalConfig;
import io.mycat.jcache.net.conn.Connection;
import io.mycat.jcache.setting.Settings;
import io.mycat.jcache.util.ItemUtil;

/**
 * 
 * @author liyanjun
 * @author tangww
 *
 */
public class Items {
		
	final static AtomicBoolean[] allocItemStatus = new AtomicBoolean[Settings.POWER_LARGEST];
	static {
        try {
           for(int i=0; i<allocItemStatus.length; i++){
        	   allocItemStatus[i] = new AtomicBoolean(false);
           }
        } catch (Exception ex) { throw new Error(ex); }
    }
	
	/**
	 * Allocates a new item.
	 * @param key     key
	 * @param flags
	 * @param exptime  过期时间
	 * @param nbytes  value length
	 * @return
	 */
	public static long do_item_alloc(String key,int flags,long exptime,int nbytes){
		String suffixStr = ItemUtil.item_make_header_suffix(key.length(), flags, nbytes);
		int ntotal = ItemUtil.item_make_header(key.length(), flags, nbytes,suffixStr);
		long itemaddr = 0;
		
		if(Settings.useCas){
			ntotal += 8;
		}
		
		int clsid = JcacheContext.getSlabPool().slabsClassid(ntotal);
		if(clsid == 0){
			return 0;
		}
		
		for(int i=0;i<10;i++){
			long total_bytes = 0;
			if(Settings.lruMaintainerThread){
				lru_pull_tail(clsid,LRU_TYPE_MAP.COLD_LRU.ordinal(),0,0);
			}
			
			itemaddr = JcacheContext.getSlabPool().slabs_alloc(ntotal, clsid, flags);
			
			if(Settings.expireZeroDoesNotEvict){
				total_bytes -= noexp_lru_size(clsid);  //TODO 
			}
			
			if(itemaddr==0){
				if(Settings.lruMaintainerThread){
					lru_pull_tail(clsid,LRU_TYPE_MAP.HOT_LRU.ordinal(),0,0);
					lru_pull_tail(clsid,LRU_TYPE_MAP.WARM_LRU.ordinal(),0,0);
					if(lru_pull_tail(clsid,LRU_TYPE_MAP.COLD_LRU.ordinal(),total_bytes,Settings.LRU_PULL_EVICT)<=0){
						break;
					}
				}else{
					if(lru_pull_tail(clsid,LRU_TYPE_MAP.COLD_LRU.ordinal(),0,Settings.LRU_PULL_EVICT)<=0){
						break;
					}
				}
			}else{
				break;
			}
			ItemUtil.setNext(itemaddr, 0);
			ItemUtil.setPrev(itemaddr, 0);
			
			if(Settings.lruMaintainerThread){
				if(exptime==0&& Settings.expireZeroDoesNotEvict){
					clsid = clsid|LRU_TYPE_MAP.NOEXP_LRU.ordinal();
				}else{
					clsid = clsid|LRU_TYPE_MAP.HOT_LRU.ordinal();
				}
			}else{
				clsid = clsid|LRU_TYPE_MAP.COLD_LRU.ordinal();
			}
			
			ItemUtil.setSlabsClsid(itemaddr, (byte)clsid);
			byte flag = ItemUtil.getItflags(itemaddr);
			ItemUtil.setItflags(itemaddr, (byte)(flag|(Settings.useCas?ItemFlags.ITEM_CAS.getFlags():0)));
			ItemUtil.setNskey(itemaddr,(byte)key.length());
			ItemUtil.setNbytes(itemaddr, nbytes);
			try {
				ItemUtil.setKey(key.getBytes(JcacheGlobalConfig.defaultCahrset), itemaddr);
				ItemUtil.setExpTime(itemaddr, exptime);
				byte[] suffixBytes = suffixStr.getBytes(JcacheGlobalConfig.defaultCahrset);
				ItemUtil.setSuffix(itemaddr, suffixBytes);
				ItemUtil.setNsuffix(itemaddr, (byte)suffixBytes.length);
				if((flag&ItemFlags.ITEM_CHUNKED.getFlags())>0){
					//TODO 
//					long item_chunk = ItemUtil.ITEM_data(itemaddr);
				}
				ItemUtil.setHNext(itemaddr, 0);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return itemaddr;
	}
	
	public static long do_item_get(String key,Connection conn){
		long addr = HashTable.find(key);
		int was_found = 0;
		if(addr!=-1){
//			ItemUtil.
//			refcount_incr(&it->refcount); //TODO
			was_found = 1;
			long exptime = ItemUtil.getExpTime(addr);
			if(item_is_flushed(addr)){
				do_item_unlink(addr);
				do_item_remove(addr);
				addr = 0;
//	            pthread_mutex_lock(&c->thread->stats.mutex);
//	            c->thread->stats.get_flushed++;
//	            pthread_mutex_unlock(&c->thread->stats.mutex);
				was_found = 2;
			}else if(exptime!=0&& exptime <= System.currentTimeMillis()){  //已过期
				do_item_unlink(addr);
				do_item_remove(addr);
	            addr = 0;
//	            pthread_mutex_lock(&c->thread->stats.mutex);
//	            c->thread->stats.get_expired++;
//	            pthread_mutex_unlock(&c->thread->stats.mutex);
				was_found = 3;
			}else{
				byte flags = ItemUtil.getItflags(addr);
				ItemUtil.setItflags(addr, (byte)(flags|ItemFlags.ITEM_FETCHED.getFlags()|ItemFlags.ITEM_ACTIVE.getFlags()));
			}
		}
		
	    /* For now this is in addition to the above verbose logging. */
//	    LOGGER_LOG(c->thread->l, LOG_FETCHERS, LOGGER_ITEM_GET, NULL, was_found, key, nkey);
		return addr;
	}
	
	public static void do_item_unlink(long addr){
		byte flags = ItemUtil.getItflags(addr);
		if((flags&ItemFlags.ITEM_LINKED.getFlags())!=0){
			ItemUtil.setItflags(addr, (byte)(flags&~ItemFlags.ITEM_LINKED.getFlags()));
			// TODO  stats modify
			String key = ItemUtil.getKey(addr);
			// HashTable.delect(key, addr);  TODO  这个方法可鞥有问题
		}
	}
	
	public static void do_item_remove(long addr){
		if(refcount_decr(addr)==0){
			item_free(addr);
		}
	}
	
	public static void item_free(long addr){
		int ntotal = ItemUtil.ITEM_ntotal(addr);
		int clsid  = ItemUtil.getSlabsClsid(addr);
		JcacheContext.getSlabPool().slabs_free(addr, ntotal, clsid);
	}
	
	/**
	 * TODO
	 * @param clsid
	 * @return
	 */
	public static int noexp_lru_size(int clsid){
//	    int id = CLEAR_LRU(slabs_clsid);
//	    id |= NOEXP_LRU;
//	    unsigned int ret;
//	    pthread_mutex_lock(&lru_locks[id]);
//	    ret = sizes_bytes[id];
//	    pthread_mutex_unlock(&lru_locks[id]);
//	    return ret;
		return 0;
	}
	
	/**
	 * TODO 
	 * @param addr
	 */
	public static int refcount_decr(long addr){
		return 0;
	}
	
	public static boolean item_is_flushed(long itemaddr){
	    long oldest_live = Settings.oldestLive;
	    long cas = ItemUtil.getCAS(itemaddr);
	    long oldest_cas = Settings.oldestCas;
	    long time = ItemUtil.getTime(itemaddr);
	    
	    if (oldest_live == 0 || oldest_live > System.currentTimeMillis())
	        return false;
	    if ((time <= oldest_live)
	            || (oldest_cas != 0 && cas != 0 && cas < oldest_cas)) {
	        return true;
	    }
		return false;
	}

	/**
	 * TODO
	 * @param orig_id
	 * @param cur_lru
	 * @param total_bytes
	 * @param flags
	 * @return
	 */
	public static int lru_pull_tail(int orig_id,int cur_lru,long total_bytes,int flags){
		return 0;
	}
	
//	public long lru_pull_tail(int slab_idx, int cur_lru, int total_chunks, boolean do_evict, int cur_hv){
//		long id;
//		int removed = 0;
//		if(slab_idx == 0)
//			return 0;
//		
//		int tries = 5;
//		
//		long search;
//		long next_it;
//		int move_to_lru = 0;
//		int limit;
//		
//		int slabIdx = slab_idx | cur_lru;
//		
//		synchronized (allocItemStatus[slabIdx]) {
//			
//		}
//		
//		return 0;
//	}

//  TODO   
//	==========引用计数 部分   begin	
//	unsigned short refcount_incr(unsigned short *refcount) {
//		#ifdef HAVE_GCC_ATOMICS
//		    return __sync_add_and_fetch(refcount, 1);
//		#elif defined(__sun)
//		    return atomic_inc_ushort_nv(refcount);
//		#else
//		    unsigned short res;
//		    mutex_lock(&atomics_mutex);
//		    (*refcount)++;
//		    res = *refcount;
//		    mutex_unlock(&atomics_mutex);
//		    return res;
//		#endif
//		}
//
//		unsigned short refcount_decr(unsigned short *refcount) {
//		#ifdef HAVE_GCC_ATOMICS
//		    return __sync_sub_and_fetch(refcount, 1);
//		#elif defined(__sun)
//		    return atomic_dec_ushort_nv(refcount);
//		#else
//		    unsigned short res;
//		    mutex_lock(&atomics_mutex);
//		    (*refcount)--;
//		    res = *refcount;
//		    mutex_unlock(&atomics_mutex);
//		    return res;
//		#endif
//		}
//	==========引用计数 部分   end		
}
