/*
 *  文件创建时间： 2016年12月15日
 *  文件创建者: PigBrother(LZY/LZS)二师兄
 *  所属工程: JCache
 *  CopyRights
 *
 *  备注:
 */
package io.mycat.jcache.memhashtable;

import io.mycat.jcache.hash.Hash;
import io.mycat.jcache.hash.Hash_func_type;
import io.mycat.jcache.hash.impl.HashImpl;

import java.nio.ByteBuffer;

/**
 * Created by PigBrother(LZS/LZY) on 2016/12/15 7:17.
 */
public class HashTable {
    private static ByteBuffer cached = ByteBuffer.allocateDirect(0x7ffffff8);
    static Hash hash = new HashImpl(Hash_func_type.PIG_HASH);
    static {
        for (int i = 0; i < 0xfffffff; i++) {
            cached.putLong(-1);
        }
    }

    public static long find(String key, long ... key_attributes){
        long  index = cached.getLong((int)hash.hash(key)&0xfffffff);
        if(index != -1){
            return 0;
        }else{
            return -1;//调用者需做处理  -1 等于 Store_item_type.NOT_FOUND
        }
    }
}
