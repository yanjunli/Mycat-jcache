package mycat.leaderus.lzy.cachesys.memcached.memobject;

import mycat.leaderus.lzy.cachesys.memcache_v5.ResultsWithCAS;
import mycat.leaderus.lzy.cachesys.memcached.config.MemConfig;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by PigBrother(LZS/LZY) on 2016/12/7 11:04.
 * <p>
 * chunk
 * <p>
 * flag int 4bytes
 * timeout long 8bytes
 * CAS long 8bytes
 * value's length int 4bytes
 * //isReUsed length int;
 * values length bytes
 * -----------------------------
 * chunk size
 * timeout= 4+ x * chunk size
 */
class Slab {
    static final int VALUES_OFFSET = 24;
    private static final int TIMEOUT_OFFSET = 4;
    private static final int CAS_OFFSET = 12;
    private static final int VALUESLENTH_OFFSET = 20;
    private static AtomicLong CAS = new AtomicLong(0);
    private static AtomicLong CAStmp = new AtomicLong(0);
    public final short slabInx;
    private ByteBuffer buffer;
    private Thread thread;
    // private static int CHUNK_REUSED_OFFSET = 24;
    private int chunk_size;
    private int index;
    private boolean isStop;
    private AtomicLong[] usedChunk;
    private AtomicLong chunkIndex;
    private int chunkTotalNum;
    private boolean isThread = false;
    private boolean isInit = false;
    // private static AtomicInteger tmpAI = new AtomicInteger();

    Slab(ByteBuffer buffer, short slabInx) {
        this.buffer = buffer;
        this.slabInx = slabInx;
    }

    boolean destory() {
        isStop = true;
        while (true) {
            if (isThread)
                break;
            try {
                Thread.sleep(2200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isThread = isStop = false;
        isInit = true;
        return isInit;
    }

    Slab init(int index) {
        if (!isInit) {
            chunkIndex = new AtomicLong(0);
            this.index = index;
            chunk_size = (int) Math.pow(MemConfig.FACTOR, this.index) * MemConfig.CHUNK_SIZES;
            usedChunk = new AtomicLong[((chunkTotalNum = MemConfig.SLAB_SIZE / chunk_size) >>> 6) + 1];
            for (int i = 0; i < usedChunk.length; i++) {
                usedChunk[i] = new AtomicLong(0);
            }
            for (int i = 0; i < chunkTotalNum; i++) {
                buffer.putLong(TIMEOUT_OFFSET + chunk_size * i, Long.MAX_VALUE);
                // buffer.putInt(CHUNK_REUSED_OFFSET + chunk_size * i, 0);
            }
            System.out.println(chunk_size);
            System.out.println(usedChunk.length);
            System.out.println(chunkTotalNum);
            thread = new Thread() {
                long timeout, tmp;

                public void run() {
                    while (true) {
                        timeout = System.currentTimeMillis();
                        for (int i = 0; i < chunkTotalNum; i++) {
                            if ((((tmp = usedChunk[i >>> 6].get()) >>> (i & 0x3f)) & 1) == 1) {
                                //  chunktmp = buffer.getInt(CHUNK_REUSED_OFFSET + i * chunk_size);
                                if (buffer.getLong(TIMEOUT_OFFSET + i * chunk_size) < timeout)
                                    usedChunk[i >>> 6].compareAndSet(tmp, tmp | ~(1 << (i & 0x3f)));
                            }
                        }
                        try {
                            Thread.sleep(10000);
                            if (isStop)
                                break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isThread = true;
                }
            };
            thread.start();
        }
        return this;
    }

    int setChunk(int flags, int bytesSizes, long timeout, byte[] data) {
        if (isInit) {
            init(data.length);
        }
        long chunkIndex = this.chunkIndex.getAndAdd(1);
        long tmp;
        int ai;
        if (chunkIndex >= chunkTotalNum) {
            for (int i = 0; i < chunkTotalNum; i++) {
                if ((((tmp = usedChunk[i >>> 6].get()) >>> (i & 0x3f)) & 1) == 0) {
                    buffer.putLong(TIMEOUT_OFFSET + chunk_size * i, Long.MAX_VALUE);
                    //tmpAI.set(ai=buffer.getInt(CHUNK_REUSED_OFFSET + i * chunk_size));
                    if (usedChunk[i >>> 6].compareAndSet(tmp, tmp | (1 << (i & 0x3f)))) {
                        chunkIndex = i;
                        break;
                    }
                }
            }
        }
        if (chunkIndex < chunkTotalNum) {
            int tmpIndex = (int) (chunkIndex * chunk_size);
            bytesSizes = bytesSizes > data.length ? data.length : bytesSizes;
            buffer.putInt(tmpIndex, flags).putLong(tmpIndex + TIMEOUT_OFFSET, timeout).putLong(tmpIndex + CAS_OFFSET, CAS.incrementAndGet()).putInt(tmpIndex + VALUESLENTH_OFFSET, bytesSizes);
            for (int i = 0; i < bytesSizes; i++) {
                buffer.put(tmpIndex + VALUES_OFFSET + i, data[i]);
            }
        } else
            return -1;
        return (int) chunkIndex;
    }

    boolean setChunk(int flags, int bytesSizes, long timeout, byte[] data, int tmpIndex) {
        if (timeout == 0) {
            buffer.putLong(tmpIndex + TIMEOUT_OFFSET, timeout);
            return true;
        }
        bytesSizes = bytesSizes > data.length ? data.length : bytesSizes;
        buffer.putInt(tmpIndex, flags).putLong(tmpIndex + TIMEOUT_OFFSET, timeout).putLong(tmpIndex + CAS_OFFSET, CAS.incrementAndGet()).putInt(tmpIndex + VALUESLENTH_OFFSET, bytesSizes);
        for (int i = 0; i < bytesSizes; i++) {
            buffer.put(tmpIndex + VALUES_OFFSET + i, data[i]);
        }
        return true;
    }

    boolean setChunkWithCAS(int flags, int bytesSizes, long timeout, byte[] data, int tmpIndex, long CAS) {
        if (timeout == 0) {
            buffer.putLong(tmpIndex + TIMEOUT_OFFSET, timeout);
            return true;
        }
        CAStmp.set(CAS);
        if (CAStmp.compareAndSet(buffer.getLong(tmpIndex + CAS_OFFSET), CAS + 1)) {
            bytesSizes = bytesSizes > data.length ? data.length : bytesSizes;
            buffer.putInt(tmpIndex, flags).putLong(tmpIndex + TIMEOUT_OFFSET, timeout).putLong(tmpIndex + CAS_OFFSET, Slab.CAS.incrementAndGet()).putInt(tmpIndex + VALUESLENTH_OFFSET, bytesSizes);
            for (int i = 0; i < bytesSizes; i++) {
                buffer.put(tmpIndex + VALUES_OFFSET + i, data[i]);
            }
            return true;
        }
        return false;
    }

    ResultsWithCAS getChunk(int index) {
        index *= chunk_size;
        int valuelength = buffer.getInt(index + VALUESLENTH_OFFSET);
        byte[] bytes = new byte[valuelength];
        for (int i = 0; i < valuelength; i++) {
            System.err.println(index + VALUES_OFFSET + i);
            bytes[i] = buffer.get(index + VALUES_OFFSET + i);
        }
        return new ResultsWithCAS(buffer.getInt(index), buffer.getLong(index + TIMEOUT_OFFSET), buffer.getLong(index + CAS_OFFSET), buffer.getInt(index + VALUESLENTH_OFFSET), bytes);
    }

    int getLengthInt(int index) {
        return chunk_size;
    }
}
