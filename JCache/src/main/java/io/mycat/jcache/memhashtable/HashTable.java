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
import io.mycat.jcache.util.ItemUtil;

import java.nio.ByteBuffer;

/**
 * Created by PigBrother(LZS/LZY) on 2016/12/15 7:17.
 */

/**
 * hashtable 作为底层的 提供 find put delect 既可满足需求
 */
public class HashTable {
    private static ByteBuffer cached = ByteBuffer.allocateDirect(0x7ffffff8);
    static Hash hash = new HashImpl(Hash_func_type.PIG_HASH);
    static {
        for (int i = 0; i < 0xfffffff; i++) {
            cached.putLong(-1);
        }
    }

    public static long find(String key) {
        long index = cached.getLong((int) (hash.hash(key) & 0xfffffff));
        if(index != -1)
            do {
                if (ItemUtil.getKey(index).equals(key)) {
                    return index;
                }
            } while ((index = ItemUtil.getHNext(index)) != -1);
        return -1; //调用者需做处理  -1 等于 Store_item_type.NOT_FOUND
    }

    public static long put(String key, long item){
        int index = (int) (hash.hash(key) & 0xfffffff);
        long pre_index = -1;
        long pre_index2 = -1;
        while ((pre_index=cached.get(index))!= -1){
            if(ItemUtil.getKey(pre_index).equals(key))
                return pre_index;
            pre_index2=pre_index;
        }
        if(pre_index2!=-1)
            ItemUtil.setHNext(pre_index2,item);
        else
            cached.putLong(index,item);
        return item;
    }

    public static long delect(String key, long item){
        long index = cached.getLong((int) (hash.hash(key) & 0xfffffff));
        long pre_index = index;
        if(index==item) {
            cached.putLong((int) (hash.hash(key) & 0xfffffff), -1);
            return item;//delete success;  hashtable 里面没有冲突 ，直接删除，
        }
        if(index!=-1)
            do {
                pre_index = index;
                if (ItemUtil.getKey(index).equals(key)) {
                    ItemUtil.setHNext(pre_index,ItemUtil.getHNext(index));
                    return index;  //return 需要被进一步处理的item的adrr ，此时 这个item已经不在hashtable里了  ，调用者可以释放 item了
                }
            } while ((index = ItemUtil.getHNext(index)) != -1);
        return -1; //调用者需做处理  -1 等于 Store_item_type.NOT_FOUND
    }
}
