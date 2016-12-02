package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 行知道人 on 2016/11/29.
 */
class Chunk {
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

    int getReading() {
        return reading.get();
    }

    void setReading(boolean flag) {
        if (flag)
            reading.getAndIncrement();
        else
            reading.getAndDecrement();
    }

    void setReading() {
        reading.set(0);
    }

    long getCAS() {
        return CAS;
    }

    void setCAS(long CAS) {
        this.CAS = CAS;
    }

    int getByteSizes() {
        return byteSizes;
    }

    void setByteSizes(int byteSizes) {
        this.byteSizes = byteSizes;
    }

    int getFlags() {
        return flags;
    }

    void setFlags(int flags) {
        this.flags = flags;
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
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
