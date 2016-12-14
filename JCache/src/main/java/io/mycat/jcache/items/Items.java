package io.mycat.jcache.items;

import java.util.concurrent.atomic.AtomicBoolean;

import io.mycat.jcache.setting.Settings;

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
	
}
