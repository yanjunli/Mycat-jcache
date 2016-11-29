package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class Chunk {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    private ByteBuffer buffer;
    private long timeout;
    public Chunk(ByteBuffer slice) {
        this.buffer = slice;
        this.buffer.clear();
    }
    public void setTimeout(long timeout){
        this.timeout = timeout;
    }
    public long getTimeout(){
        return this.timeout;
    }
    public ByteBuffer getByteBuffer(){
        return this.buffer;
    }

}
