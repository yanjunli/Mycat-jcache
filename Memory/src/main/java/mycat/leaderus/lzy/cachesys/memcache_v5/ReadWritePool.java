package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class ReadWritePool {
    private static ConcurrentHashMap<String, Chunk> cache = new ConcurrentHashMap();
    private static AtomicLong CAS = new AtomicLong(0);

    /**
     * 对应set命令 注意timeout 传入为 当前时间加过期时间
     *
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */
    public static int set(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null)
            ManagerMemory.removeChunk(tmp);
        tmp = ManagerMemory.getChunk(bytesSizes);
        if (tmp == null) return ReturnStatus.ERROR.ordinal();
        tmp.setKey(key);
        tmp.setReading();
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


    /**
     * 对应add命令 注意timeout 传入为 当前时间加过期时间
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */

    public static int add(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        if (cache.get(key) == null) {
            if (set(key, flags, bytesSizes, timeout, data) == ReturnStatus.ERROR.ordinal())
                return ReturnStatus.NOT_STORED.ordinal();
            else
                return ReturnStatus.STORED.ordinal();
        } else {
            return ReturnStatus.NOT_STORED.ordinal();
        }

    }


    /**
     * 对应replace命令 注意timeout 传入为 当前时间加过期时间
     *
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */
    public static int replace(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null) {
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

    private static int replace_incr_decr(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null) {
            if (!(tmp.getByteBuffer().capacity() < bytesSizes)) {
                chunkInfo(flags, bytesSizes, timeout, data, tmp);
                ManagerMemory.addUsed(tmp);
                return ReturnStatus.STORED.ordinal();
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


    /**
     * 对应append命令 注意timeout 传入为 当前时间加过期时间
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */
    public static int append(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null) {
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


    /**
     * 对应prepend命令 注意timeout 传入为 当前时间加过期时间
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */
    public static int prepend(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null) {
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


    /**
     * 对应CAS命令 注意timeout 传入为 当前时间加过期时间
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @param CASInput
     * @return
     */
    public static int CAS(String key, int flags, int bytesSizes, long timeout, byte[] data, long CASInput) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null) {
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


    /**
     * 对应get命令 返回空为 没有查到
     * @param keys
     * @return
     */
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
                    tmpChunk.setReading(true);
                    ByteBuffer buffer = cache.get(key).getByteBuffer();
                    tmp = new byte[buffer.limit()];
                    buffer.get(tmp).flip();
                    //results[i] = new Results(tmpChunk.getFlags(),tmpChunk, tmpChunk.getByteSizes(), tmp);
                    tmpChunk.setReading(false);
                } else {
                    System.out.println("缓存过期");
                    if (tmpChunk.getReading() == 0)
                        ManagerMemory.removeChunk(tmpChunk);
                }
            }
            i++;
        }
        return results;
    }


    /**
     * 对应gets命令 返回空为 没有查到
     * @param keys
     * @return
     */
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
                    tmpChunk.setReading(true);
                    ByteBuffer buffer = cache.get(key).getByteBuffer();
                    tmp = new byte[buffer.limit()];
                    buffer.get(tmp).flip();
                    //results[i] = new ResultsWithCAS(tmpChunk.getFlags(), tmpChunk.getByteSizes(), tmp, tmpChunk.getCAS());
                    tmpChunk.setReading(false);
                } else {
                    System.out.println("缓存过期");
                    if (tmpChunk.getReading() == 0)
                        ManagerMemory.removeChunk(tmpChunk);
                }
            }
            i++;
        }
        return results;
    }


    /**
     * delete命令
     * @param key
     * @return
     */
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

    public static int flush_all(long time) {
        if (time > 100)
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        cache.clear();
        ManagerMemory.removeAllChunk();
        return ReturnStatus.OK.ordinal();
    }

    /**
     * incr
     * @param key
     * @param increment_value
     * @return
     */
    public static int incr(String key, int increment_value) {
        return incr_decr(key, increment_value, true);
    }

    /**
     * decr
     * @param key
     * @param decrement_value
     * @return
     */
    public static int decr(String key, int decrement_value) {
        return incr_decr(key, decrement_value, false);
    }

    private static int incr_decr(String key, int value, boolean flag) {
        Chunk tmp;
        if ((tmp = cache.get(key)) != null) {
            if (ManagerMemory.removeUsedChunk(tmp)) {
                byte[] tmpValue = new byte[tmp.getByteSizes()];
                tmp.getByteBuffer().get(tmpValue).flip();
                long val;
                try {
                    val = Integer.parseInt(new String(tmpValue)) & 0xFFFFFFF;
                } catch (NumberFormatException e) {
                    return ReturnStatus.CLIENT_ERROR.ordinal();
                }
                tmpValue = flag ? Long.toString(val + value).getBytes(Charset.forName("UTF-8")) : Long.toString(val - value).getBytes(Charset.forName("UTF-8"));
                return replace_incr_decr(tmp.getKey(), tmp.getFlags(), tmpValue.length, tmp.getTimeout(), tmpValue);
            } else
                return ReturnStatus.ERROR.ordinal();
        } else {
            return ReturnStatus.NOT_FOUND.ordinal();
        }
    }

    static void remove(String key) {
        cache.remove(key);
    }


}
