package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class ReadWritePool {
    private static ConcurrentHashMap<String, Chunk> cache = new ConcurrentHashMap();
    private static AtomicLong CAS = new AtomicLong(0);

    public static int set(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        boolean b = cache.get(key) != null ? ManagerMemory.removeChunk(cache.get(key)) : false;
        Chunk tmp = ManagerMemory.getChunk(bytesSizes);
        if (tmp == null) return ReturnStatus.ERROR.ordinal();
        tmp.setKey(key);
        chunkInfo(flags, bytesSizes, timeout, data, tmp);
        cache.put(key, tmp);
        ManagerMemory.addUsed(tmp);
        return ReturnStatus.STORED.ordinal();
    }

    private static void chunkInfo(int flags, int bytesSizes, long timeout, byte[] data, Chunk tmp) {
        tmp.setTimeout(timeout);
        tmp.setFlags(flags);
        tmp.setCAS(CAS.addAndGet(1));
        tmp.setByteSizes(bytesSizes);
        ((ByteBuffer) tmp.getByteBuffer().clear()).put(data).flip();
    }


    public static int add(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        boolean b = cache.get(key) != null ? true : false;
        if (!b) {
            if (set(key, flags, bytesSizes, timeout, data) == ReturnStatus.ERROR.ordinal())
                return ReturnStatus.NOT_STORED.ordinal();
            else
                return ReturnStatus.STORED.ordinal();
        } else {
            return ReturnStatus.NOT_STORED.ordinal();
        }

    }

    public static int repalce(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Chunk tmp = cache.get(key);
        if (tmp != null) {
            if (!(tmp.getByteBuffer().capacity() < bytesSizes)) {
                if (ManagerMemory.removeUsedChunk(tmp)) {
                    chunkInfo(flags, bytesSizes, timeout, data, tmp);
                    ManagerMemory.addUsed(tmp);
                    return ReturnStatus.STORED.ordinal();
                }
                return ReturnStatus.NOT_STORED.ordinal();
            } else {
                ManagerMemory.removeChunk(tmp);
                if (set(key, flags, bytesSizes, timeout, data) == ReturnStatus.ERROR.ordinal())
                    return ReturnStatus.NOT_STORED.ordinal();
                else
                    return ReturnStatus.STORED.ordinal();
            }
        }
        return ReturnStatus.NOT_STORED.ordinal();
    }

    public static int append(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        boolean b = cache.get(key) != null ? true : false;
        if (b) {
            Chunk tmp = cache.get(key);
            if (ManagerMemory.removeUsedChunk(tmp)) {
                byte[] tmpbytes = new byte[tmp.getByteSizes()];
                tmp.getByteBuffer().get(tmpbytes).flip();
                byte[] newbytes = new byte[tmpbytes.length + bytesSizes];
                System.arraycopy(tmpbytes, 0, newbytes, 0, tmpbytes.length);
                System.arraycopy(data, 0, newbytes, tmpbytes.length, bytesSizes);
                if (pend(key, flags, timeout, tmp, newbytes)) return ReturnStatus.STORED.ordinal();
            }
            return ReturnStatus.CLIENT_ERROR.ordinal();
        }
        return ReturnStatus.NOT_STORED.ordinal();
    }

    public static int prepend(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        boolean b = cache.get(key) != null ? true : false;
        if (b) {
            Chunk tmp = cache.get(key);
            if (ManagerMemory.removeUsedChunk(tmp)) {
                byte[] tmpbytes = new byte[tmp.getByteSizes()];
                tmp.getByteBuffer().get(tmpbytes).flip();
                byte[] newbytes = new byte[tmpbytes.length + bytesSizes];
                System.arraycopy(data, 0, newbytes, 0, bytesSizes);
                System.arraycopy(tmpbytes, 0, newbytes, bytesSizes, tmpbytes.length);
                if (pend(key, flags, timeout, tmp, newbytes)) return ReturnStatus.STORED.ordinal();
            }
            return ReturnStatus.CLIENT_ERROR.ordinal();
        }
        return ReturnStatus.NOT_STORED.ordinal();
    }

    private static boolean pend(String key, int flags, long timeout, Chunk tmp, byte[] newbytes) {
        if (newbytes.length <= tmp.getByteBuffer().capacity()) {
            chunkInfo(flags, newbytes.length, timeout, newbytes, tmp);
            ManagerMemory.addUsed(tmp);
            return true;
        } else {
            ManagerMemory.addUsed(tmp);
            ManagerMemory.removeChunk(tmp);
            if (set(key, flags, newbytes.length, timeout, newbytes) == ReturnStatus.STORED.ordinal())
                return true;
        }
        return false;
    }

    public static int CAS(String key, int flags, int bytesSizes, long timeout, byte[] data, long CASInput) {
        boolean b = cache.get(key) != null ? true : false;
        if (b) {
            Chunk tmp = cache.get(key);
            if (ManagerMemory.removeUsedChunk(tmp)) {
                if (tmp.getCAS() == CASInput) {
                    if (!(tmp.getByteBuffer().capacity() < bytesSizes)) {
                        chunkInfo(flags, bytesSizes, timeout, data, tmp);
                        ManagerMemory.addUsed(tmp);
                        return ReturnStatus.STORED.ordinal();
                    } else {
                        ManagerMemory.removeChunk(tmp);
                        return set(key, flags, bytesSizes, timeout, data);
                    }
                } else {
                    return ReturnStatus.EXISTS.ordinal();
                }
            }
            return ReturnStatus.ERROR.ordinal();
        } else {
            return ReturnStatus.NOT_FOUND.ordinal();
        }
    }

    public static Results[] get(String[] keys) {
        byte[] tmp;
        Results[] results = new Results[keys.length];
        Chunk tmpChunk;
        int i = 0;
        for (String key : keys) {
            tmpChunk = cache.get(key);
            results[i] = null;
            if (tmpChunk != null) {
                if (tmpChunk.getTimeout() > System.currentTimeMillis()) {
                    ByteBuffer buffer = cache.get(key).getByteBuffer();
                    tmp = new byte[buffer.limit()];
                    buffer.get(tmp).flip();
                    results[i] = new Results(tmpChunk.getFlags(), tmpChunk.getByteSizes(), tmp);
                } else {
                    System.out.println("缓存过期");
                    ManagerMemory.removeChunk(tmpChunk);
                }
            }
            i++;
        }
        return results;
    }

    public static ResultsWithCAS[] gets(String[] keys) {
        byte[] tmp;
        ResultsWithCAS[] results = new ResultsWithCAS[keys.length];
        Chunk tmpChunk;
        int i = 0;
        for (String key : keys) {
            tmpChunk = cache.get(key);
            results[i] = null;
            if (tmpChunk != null) {
                if (tmpChunk.getTimeout() > System.currentTimeMillis()) {
                    ByteBuffer buffer = cache.get(key).getByteBuffer();
                    tmp = new byte[buffer.limit()];
                    buffer.get(tmp).flip();
                    results[i] = new ResultsWithCAS(tmpChunk.getFlags(), tmpChunk.getByteSizes(), tmp, tmpChunk.getCAS());
                } else {
                    System.out.println("缓存过期");
                    ManagerMemory.removeChunk(tmpChunk);
                }
            }
            i++;
        }
        return results;
    }

    public static int delete(String key) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null) {
            if (ManagerMemory.removeChunk(tmp))
                return ReturnStatus.DELETED.ordinal();
            else
                return ReturnStatus.ERROR.ordinal();
        } else {
            return ReturnStatus.NOT_FOUND.ordinal();
        }
    }

    static void remove(String key) {
        cache.remove(key);
    }


}
