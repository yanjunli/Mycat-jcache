package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class Chunk {
    private String key;
    private ByteBuffer buffer;
    private long timeout;
    private int byteSizes;
    private long CAS;
    private int flags;
    private AtomicInteger reading = new AtomicInteger(0);

    Chunk(ByteBuffer slice) {
        this.buffer = slice;
        this.buffer.clear();
    }

    public int getReading() {
        return reading.get();
    }

    public void setReading(boolean flag) {
        if (flag)
            reading.getAndIncrement();
        else
            reading.getAndDecrement();
    }

    public long getCAS() {
        return CAS;
    }

    public void setCAS(long CAS) {
        this.CAS = CAS;
    }

    public int getByteSizes() {
        return byteSizes;
    }

    public void setByteSizes(int byteSizes) {
        this.byteSizes = byteSizes;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    long getTimeout() {
        return this.timeout;
    }

    void setTimeout(long timeout){
        this.timeout = timeout;
    }

    ByteBuffer getByteBuffer(){
        return this.buffer;
    }

}
