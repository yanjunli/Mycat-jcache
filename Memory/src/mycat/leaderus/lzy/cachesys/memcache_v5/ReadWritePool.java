package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class ReadWritePool {
    private static ConcurrentHashMap<String, Chunk> cache = new ConcurrentHashMap();

    public static boolean set(String key, byte[] data, long timeout){
        boolean b = cache.get(key)!=null?ManagerMemory.removeChunk(cache.get(key)):false;
        Chunk tmp = ManagerMemory.getChunk(data.length);
        tmp.setKey(key);
        tmp.setTimeout(timeout);
        ((ByteBuffer)tmp.getByteBuffer().clear()).put(data).flip();
        cache.put(key,tmp);
        ManagerMemory.addUsed(tmp);
        return true;
    }

    public static byte[] get(String key){
        byte[] tmp = null;
        Chunk tmpChunk = cache.get(key);;
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

    public static boolean add(String key, byte[] data, long timeout){
        boolean b = cache.get(key)!=null?true:false;
        if(!b){
            set(key,data,timeout);
            return true;
        }else {
            return false;
        }

    }

    public static boolean repalce(String key, byte[] data, long timeout){
        boolean b = cache.get(key)!=null?true:false;
        if(b){
            Chunk tmp = cache.get(key);
            if(!(tmp.getByteBuffer().capacity()<data.length)) {
                ManagerMemory.removeUsedChunk(tmp);
                ((ByteBuffer) tmp.getByteBuffer().clear()).put(data).flip();
                tmp.setTimeout(timeout);
                ManagerMemory.addUsed(tmp);
            }else{
                ManagerMemory.removeChunk(tmp);
                set(key,data,timeout);
            }
            return true;
        }
        return false;

    }

    public static boolean append(String key, byte[] data, long timeout){
        boolean b = cache.get(key)!=null?true:false;
        if(b) {
            Chunk tmp = cache.get(key);
            ManagerMemory.removeUsedChunk(tmp);
            byte[] tmpbytes = new byte[tmp.getByteBuffer().limit()];
            tmp.getByteBuffer().get(tmpbytes).flip();
            byte[] newbytes = new byte[tmpbytes.length + data.length];
            System.arraycopy(tmpbytes, 0, newbytes, 0, tmpbytes.length);
            System.arraycopy(data, 0, newbytes, tmpbytes.length, data.length);
            if (newbytes.length <= tmp.getByteBuffer().capacity()) {
                ((ByteBuffer) tmp.getByteBuffer().clear()).put(newbytes).flip();
                tmp.setTimeout(timeout);
                ManagerMemory.addUsed(tmp);
            } else {
                ManagerMemory.addUsed(tmp);
                ManagerMemory.removeChunk(tmp);
                set(key, newbytes, timeout);
            }
            return true;
        }
        return false;
    }

    static void remove(String key){
        cache.remove(key);
    }


}
