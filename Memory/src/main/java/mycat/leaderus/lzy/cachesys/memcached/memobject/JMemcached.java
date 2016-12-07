package mycat.leaderus.lzy.cachesys.memcached.memobject;

import mycat.leaderus.lzy.cachesys.memcache_v5.Results;
import mycat.leaderus.lzy.cachesys.memcache_v5.ResultsWithCAS;
import mycat.leaderus.lzy.cachesys.memcache_v5.ReturnStatus;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by PigBrother(LZS/LZY) on 2016/12/7 18:33.
 */
public class JMemcached {
    private static ConcurrentHashMap<String, Long> cached = new ConcurrentHashMap<>();

    private static LinkedBlockingDeque<ByteBuffer> buffers = new LinkedBlockingDeque<>();


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


    public static ReturnStatus set(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Long tmp;
        if ((tmp = cached.get(key)) != null) {
            if (SlabClass.getBufferLength(tmp) > Slab.VALUES_OFFSET + data.length) {
                SlabClass.setBuffer(flags, bytesSizes, timeout, data, tmp);
                return ReturnStatus.STORED;
            } else {
                SlabClass.setBuffer(0, 0, 0, new byte[]{0}, tmp);
            }
        }
        cached.remove(key);
        tmp = SlabClass.setBuffer(flags, bytesSizes, timeout, data);
        cached.put(key, tmp);
        return ReturnStatus.STORED;
    }


    /**
     * 对应add命令 注意timeout 传入为 当前时间加过期时间
     *
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */

    public static ReturnStatus add(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        if (cached.get(key) == null) {
            if (set(key, flags, bytesSizes, timeout, data) == ReturnStatus.ERROR)
                return ReturnStatus.NOT_STORED;
            else
                return ReturnStatus.STORED;
        } else {
            return ReturnStatus.NOT_STORED;
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
    public static ReturnStatus replace(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Long tmp;
        if ((tmp = cached.get(key)) != null) {
            if (set(key, flags, bytesSizes, timeout, data) == ReturnStatus.ERROR)
                return ReturnStatus.NOT_STORED;
            else
                return ReturnStatus.STORED;
        }
        return ReturnStatus.NOT_STORED;
    }

//    private static int replace_incr_decr(String key, int flags, int bytesSizes, long timeout, byte[] data) {
//        Chunk tmp;
//        if ((tmp = cache.get(key)) != null) {
//            if (!(tmp.getByteBuffer().capacity() < bytesSizes)) {
//                chunkInfo(flags, bytesSizes, timeout, data, tmp);
//                ManagerMemory.addUsed(tmp);
//                return ReturnStatus.STORED.ordinal();
//            } else {
//                ManagerMemory.removeChunk(tmp);
//                if (set(key, flags, bytesSizes, timeout, data) == ReturnStatus.ERROR.ordinal())
//                    return ReturnStatus.NOT_STORED.ordinal();
//                else
//                    return ReturnStatus.STORED.ordinal();
//            }
//        }
//        return ReturnStatus.NOT_STORED.ordinal();
//    }


    /**
     * 对应append命令 注意timeout 传入为 当前时间加过期时间
     *
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */
    public static ReturnStatus append(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Long tmp;
        if ((tmp = cached.get(key)) != null) {
            ResultsWithCAS rw = SlabClass.getBuffer(tmp);
            byte[] newbytes = new byte[rw.values.length + bytesSizes];
            System.arraycopy(rw.values, 0, newbytes, 0, rw.values.length);
            System.arraycopy(data, 0, newbytes, rw.values.length, bytesSizes);
            return set(key, flags | rw.flags, newbytes.length, timeout, newbytes);
        } else {
            return ReturnStatus.NOT_STORED;
        }
    }


    /**
     * 对应prepend命令 注意timeout 传入为 当前时间加过期时间
     *
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @return
     */
    public static ReturnStatus prepend(String key, int flags, int bytesSizes, long timeout, byte[] data) {
        Long tmp;
        if ((tmp = cached.get(key)) != null) {
            ResultsWithCAS rw = SlabClass.getBuffer(tmp);
            byte[] newbytes = new byte[rw.values.length + bytesSizes];
            System.arraycopy(data, 0, newbytes, 0, bytesSizes);
            System.arraycopy(rw.values, 0, newbytes, data.length, rw.values.length);
            return set(key, flags | rw.flags, newbytes.length, timeout, newbytes);
        } else {
            return ReturnStatus.NOT_STORED;
        }
    }


    /**
     * 对应CAS命令 注意timeout 传入为 当前时间加过期时间
     *
     * @param key
     * @param flags
     * @param bytesSizes
     * @param timeout
     * @param data
     * @param CASInput
     * @return
     */
    public static ReturnStatus CAS(String key, int flags, int bytesSizes, long timeout, byte[] data, long CASInput) {
        Long tmp;
        if ((tmp = cached.get(key)) != null) {
            if (SlabClass.setBufferWithCAS(flags, bytesSizes, timeout, data, tmp, CASInput))
                return ReturnStatus.STORED;
            else
                return ReturnStatus.EXISTS;
        } else {
            return ReturnStatus.NOT_FOUND;
        }
    }


    /**
     * 对应get命令 返回空为 没有查到
     *
     * @param keys
     * @return
     */
    public static Results[] get(String[] keys) {
        byte[] tmp;
        Results[] results = new Results[keys.length];
        Long tmpChunk;
        int i = 0;
        long timeout = System.currentTimeMillis();
        for (String key : keys) {
            tmpChunk = cached.get(key);
            results[i] = null;
            if (tmpChunk != null) {
                results[i] = SlabClass.getBuffer(tmpChunk).getResults();
                if (results[i].timeout < timeout)
                    results[i] = null;
            } else {
                results[i] = null;
            }
            i++;
        }
        return results;
    }


    /**
     * 对应gets命令 返回空为 没有查到
     *
     * @param keys
     * @return
     */
    public static ResultsWithCAS[] gets(String[] keys) {
        byte[] tmp;
        ResultsWithCAS[] results = new ResultsWithCAS[keys.length];
        Long tmpChunk;
        int i = 0;
        long timeout = System.currentTimeMillis();
        for (String key : keys) {
            tmpChunk = cached.get(key);
            results[i] = null;
            if (tmpChunk != null) {
                results[i] = SlabClass.getBuffer(tmpChunk);
                if (results[i].timeout < timeout)
                    results[i] = null;
            } else
                results[i] = null;
            i++;
        }
        return results;
    }


    /**
     * delete命令
     *
     * @param key
     * @return
     */
    public static ReturnStatus delete(String key) {
        Long tmp;
        if ((tmp = cached.remove(key)) != null) {
            if (SlabClass.setBuffer(0, 0, 0, new byte[]{0}, tmp))
                return ReturnStatus.OK;
            else
                return ReturnStatus.ERROR;
        } else {
            return ReturnStatus.NOT_FOUND;
        }
    }

    public static ReturnStatus flush_all(long time) {
        if (time > 100)
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        cached.clear();
        SlabClass.clear();
        return ReturnStatus.OK;
    }

    /**
     * incr
     *
     * @param key
     * @param increment_value
     * @return
     */
//    public static int incr(String key, int increment_value) {
//        return incr_decr(key, increment_value, true);
//    }
//
//    /**
//     * decr
//     *
//     * @param key
//     * @param decrement_value
//     * @return
//     */
//    public static int decr(String key, int decrement_value) {
//        return incr_decr(key, decrement_value, false);
//    }

//    private static int incr_decr(String key, int value, boolean flag) {
//        Long tmp;
//        if ((tmp = cached.get(key)) != null) {
//            if (ManagerMemory.removeUsedChunk(tmp)) {
//                byte[] tmpValue = new byte[tmp.getByteSizes()];
//                tmp.getByteBuffer().get(tmpValue).flip();
//                long val;
//                try {
//                    val = Integer.parseInt(new String(tmpValue)) & 0xFFFFFFF;
//                } catch (NumberFormatException e) {
//                    return ReturnStatus.CLIENT_ERROR.ordinal();
//                }
//                tmpValue = flag ? Long.toString(val + value).getBytes(Charset.forName("UTF-8")) : Long.toString(val - value).getBytes(Charset.forName("UTF-8"));
//                return replace_incr_decr(tmp.getKey(), tmp.getFlags(), tmpValue.length, tmp.getTimeout(), tmpValue);
//            } else
//                return ReturnStatus.ERROR.ordinal();
//        } else {
//            return ReturnStatus.NOT_FOUND.ordinal();
//        }
//    }

}




