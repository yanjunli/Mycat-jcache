package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class ReadWritePool {
    private static ConcurrentHashMap<String, Chunk> cache = new ConcurrentHashMap();

    public static boolean put(String key, byte[] data, long timeout){
        Chunk tmp = ManagerMemory.getChunk(data.length);
        tmp.setKey(key);
        tmp.setTimeout(timeout);
        tmp.getByteBuffer().put(data).flip();
        cache.put(key,tmp);
        return true;
    }

    public static byte[] get(String key){
        byte[] tmp = null;
        Chunk tmpChunk = cache.get(key);
        if(tmpChunk!=null) {
            if (tmpChunk.getTimeout() > System.currentTimeMillis()) {
                ByteBuffer buffer = cache.get(key).getByteBuffer();
                tmp = new byte[buffer.limit()];
                buffer.get(tmp).flip();
            } else {
                System.out.println("缓存过期");
                ManagerMemory.removeChunk(tmpChunk);
            }
        }
        return tmp;
    }

    protected static void remove(String key){
        cache.remove(key);
    }


}
