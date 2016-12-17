package io.mycat.jcache.items;

import io.mycat.jcache.net.conn.Connection;

/**
 * 
 * 
 * @author liyanjun
 *
 */
public class ItemsAccessManager {
	/*
	 * Returns an item if it hasn't been marked as expired,
	 * lazy-expiring as needed.
	 */
	public long item_get(String key,Connection conn){
		return Items.do_item_get(key,conn);
	}
	
	/**
	 * Allocates a new item.
	 * @param key     key
	 * @param flags
	 * @param exptime  过期时间
	 * @param nbytes  value length
	 * @return
	 */
	public long item_alloc(String key,int flags,long exptime,int nbytes){
		return Items.do_item_alloc(key,flags,exptime,nbytes);
	}
}
