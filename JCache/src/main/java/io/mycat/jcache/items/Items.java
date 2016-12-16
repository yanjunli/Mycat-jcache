package io.mycat.jcache.items;

import java.awt.event.ItemListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import io.mycat.jcache.context.JcacheContext;
import io.mycat.jcache.enums.ItemFlags;
import io.mycat.jcache.setting.Settings;
import io.mycat.jcache.util.ItemUtil;

/**
 * 
 * @author liyanjun
 * @author tangww
 * @author  yangll
 *
 */
public class Items {
	private static  AtomicLong casIdGeneraytor = new AtomicLong();
	final static AtomicBoolean[] allocItemStatus = new AtomicBoolean[Settings.POWER_LARGEST];
	static {
        try {
           for(int i=0; i<allocItemStatus.length; i++){
        	   allocItemStatus[i] = new AtomicBoolean(false);
           }
        } catch (Exception ex) { throw new Error(ex); }
    }
	
	public long do_item_get(){
		return 0;
	}

	
	public long lru_pull_tail(int slab_idx, int cur_lru, int total_chunks, boolean do_evict, int cur_hv){
		long id;
		int removed = 0;
		if(slab_idx == 0)
			return 0;
		
		int tries = 5;
		
		long search;
		long next_it;
		int move_to_lru = 0;
		int limit;
		
		int slabIdx = slab_idx | cur_lru;
		
		synchronized (allocItemStatus[slabIdx]) {
			
			
		}
		
		return 0;
	}

	public boolean do_item_link(long addr,int hv){
		byte flags = (byte)(ItemUtil.getItflags(addr) | ItemFlags.ITEM_LINKED.getFlags());
		ItemUtil.setItflags(addr,flags);
		ItemUtil.setTime(addr,System.currentTimeMillis());
		//TODO
//		STATS_LOCK();
//		stats_state.curr_bytes += ITEM_ntotal(it);
//		stats_state.curr_items += 1;
//		stats.total_items += 1;
//		STATS_UNLOCK();

		ItemUtil.setCAS(Settings.useCas?get_cas_id():0,addr);
		//TODO assoc_insert(it,hv);//插入hash chain中
		item_link_q(addr);
		ItemUtil.setRefCount(addr, (short) (ItemUtil.getRefCount(addr)+1));
		return true;
	}

	public static void item_link_q(long addr) {

	}


	/* Get the next CAS id for a new item. */
	public static long get_cas_id() {
		return casIdGeneraytor.getAndIncrement();
	}
	
}
