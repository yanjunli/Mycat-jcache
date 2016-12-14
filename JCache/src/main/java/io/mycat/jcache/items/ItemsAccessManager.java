package io.mycat.jcache.items;

import io.mycat.jcache.context.JcacheContext;

/**
 * 
 * 
 * @author liyanjun
 *
 */
public class ItemsAccessManager {
	
	JcacheContext context;
	
	public ItemsAccessManager(JcacheContext context){
		this.context = context;
	}

	/*
	 * Returns an item if it hasn't been marked as expired,
	 * lazy-expiring as needed.
	 */
//	long item_get( int nkey){
//		
//	}
	
}
