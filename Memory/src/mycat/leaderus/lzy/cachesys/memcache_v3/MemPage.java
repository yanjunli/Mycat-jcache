package mycat.leaderus.lzy.cachesys.memcache_v3;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by 行知道人 on 2016/11/23.
 */
public class MemPage {
    private int timeout;
    private ByteBuffer pageBuffer=ByteBuffer.allocateDirect(10*1024*1024);
    private int pos1=0,pos2=0;
    private int pageId;
    private int remainder=10*1024*1024;
    private HashMap<String,ByteBuffer> hashMap = new HashMap<String,ByteBuffer>();
    public synchronized int put(String key, byte[] bytes){
        remainder-=bytes.length;
        pos1=pageBuffer.position();
        pageBuffer.put(bytes);
        pos2 = pageBuffer.position();
        pageBuffer.position(pos1);
        pageBuffer.limit(pos2);
        hashMap.put(key,pageBuffer.slice());
        return pageId;
    }
    private class MemInfo{
        public int pos1;
        public int pos2;
        public int sizes;
    }
}
