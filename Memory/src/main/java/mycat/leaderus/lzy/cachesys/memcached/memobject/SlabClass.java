package mycat.leaderus.lzy.cachesys.memcached.memobject;

import mycat.leaderus.lzy.cachesys.memcache_v5.ResultsWithCAS;
import mycat.leaderus.lzy.cachesys.memcached.config.MemConfig;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by PigBrother(LZS/LZY) on 2016/12/7 15:02.
 */
class SlabClass {
    private static SlabClass[] all = new SlabClass[99];
    private static AtomicInteger slabClassTotal = new AtomicInteger(0);
    private static AtomicInteger allSize = new AtomicInteger(0);
    public final short slabClassInx;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(MemConfig.PAGE_TOTAL_MEMORY_SIZE);
    private Slab[] slabs = new Slab[MemConfig.PAGE_TOTAL_MEMORY_SIZE / MemConfig.SLAB_SIZE];
    private AtomicInteger slabIndex = new AtomicInteger(0);
    private LinkedList<Integer> slabclass[] = new LinkedList[(int) Math.ceil(Math.log(MemConfig.SLAB_SIZE / MemConfig.CHUNK_SIZES) / Math.log(MemConfig.FACTOR))];

    private SlabClass(short slabClassInx) {
        for (int i = 0; i < slabs.length; i++) {
            buffer.position(i * MemConfig.SLAB_SIZE);
            buffer.limit((i + 1) * MemConfig.SLAB_SIZE);
            slabs[i] = new Slab(buffer.slice(), (short) i);
        }
        for (int i = 0; i < slabclass.length; i++) {
            slabclass[i] = new LinkedList<>();
        }
        this.slabClassInx = slabClassInx;
        all[slabClassInx] = this;
        allSize.incrementAndGet();
    }

    private static long getSlabClass(SlabClass tmpSlabClass, int flags, int bytesSizes, long timeout, byte[] data) {
        int index;
        int size = data.length + Slab.VALUES_OFFSET;
        if (size <= MemConfig.CHUNK_SIZES) {
            index = 0;
        } else {
            index = (int) Math.ceil(Math.log(((double) size / MemConfig.CHUNK_SIZES)) / Math.log(MemConfig.FACTOR));
            if (index > (int) Math.floor(Math.log((MemConfig.SLAB_SIZE) / MemConfig.CHUNK_SIZES) / Math.log(MemConfig.FACTOR)))
                throw new RuntimeException("输入的数据过大，请调整配置文件里的SLAB_SIZE项");
        }
        if (tmpSlabClass.slabclass[index].size() == 0) {
            int slab = tmpSlabClass.getSlab();
            if (slab != -1) {
                int tmpslab = tmpSlabClass.slabClassInx << 16 | tmpSlabClass.slabs[slab].slabInx;
                tmpSlabClass.slabclass[index].addLast(tmpSlabClass.slabs[slab].slabInx & 0x7fff);
                return (long) tmpslab << 32 | tmpSlabClass.slabs[slab].init(index).setChunk(flags, bytesSizes, timeout, data);
            }
            //return getSlabClass(new SlabClass((short) slabClassTotal.getAndIncrement()), flags, bytesSizes, timeout, data);
        } else {
            int j = tmpSlabClass.slabclass[index].size();
            for (int i = 0; i < j; i++) {
                int tmp = tmpSlabClass.slabs[tmpSlabClass.slabclass[index].get(i)].setChunk(flags, bytesSizes, timeout, data);
                if (tmp != -1) {
                    return (long) tmpSlabClass.slabClassInx << 48 | (long) tmpSlabClass.slabs[tmpSlabClass.slabclass[index].get(i)].slabInx << 32 | tmp;
                }
            }
            if ((j = tmpSlabClass.getSlab()) != -1) {
                int tmp = tmpSlabClass.slabs[j].init(index).setChunk(flags, bytesSizes, timeout, data);
                int jj = tmpSlabClass.slabClassInx << 16 | tmpSlabClass.slabs[j].slabInx;
                tmpSlabClass.slabclass[index].addLast(tmpSlabClass.slabs[j].slabInx & 0x7fff);
                return (long) jj << 32 | tmp;
            } else {
                //return getSlabClass(new SlabClass((short) slabClassTotal.getAndIncrement()), flags, bytesSizes, timeout, data);
            }
        }
        return -1;
    }

    public static long setBuffer(int flags, int bytesSizes, long timeout, byte[] data) {
        SlabClass tmpSlabClass;
        if (allSize.get() == 0) {
            tmpSlabClass = new SlabClass((short) slabClassTotal.getAndIncrement());
            return getSlabClass(tmpSlabClass, flags, bytesSizes, timeout, data);
        } else {
            int tmp = allSize.get();
            long tmp2;
            for (int i = 0; i < tmp; i++) {
                if (all[i] == null) continue;
                if ((tmp2 = getSlabClass(all[i], flags, bytesSizes, timeout, data)) != -1)
                    return tmp2;
            }
            return getSlabClass(new SlabClass((short) slabClassTotal.getAndIncrement()), flags, bytesSizes, timeout, data);
        }
    }

    public static boolean setBuffer(int flags, int bytesSizes, long timeout, byte[] data, long index) {
        short i = (short) ((index & 0x7fff000000000000L) >> 48);
        short j = (short) ((index & 0x7fff00000000L) >> 32);
        int k = (int) (index & 0x7fffffff);
        return all[i].slabs[j].setChunk(flags, bytesSizes, timeout, data, k);
    }

    public static boolean setBufferWithCAS(int flags, int bytesSizes, long timeout, byte[] data, long index, long CAS) {
        short i = (short) ((index & 0x7fff000000000000L) >> 48);
        short j = (short) ((index & 0x7fff00000000L) >> 32);
        int k = (int) (index & 0x7fffffff);
        return all[i].slabs[j].setChunkWithCAS(flags, bytesSizes, timeout, data, k, CAS);
    }


    public static ResultsWithCAS getBuffer(long index) {
        //System.out.println(index);
        short i = (short) ((index & 0x7fff000000000000L) >> 48);
        short j = (short) ((index & 0x7fff00000000L) >> 32);
        int k = (int) (index & 0x7fffffff);
        // System.out.println(i);
        // System.out.println(j);
        // System.out.println(k);
        return all[i].slabs[j].getChunk(k);
    }

    public static int getBufferLength(long index) {
        short i = (short) ((index & 0x7fff000000000000L) >> 48);
        short j = (short) ((index & 0x7fff00000000L) >> 32);
        int k = (int) (index & 0x7fffffff);
        return all[i].slabs[j].getLengthInt(k);
    }

    public static void clear() {
        for (int i = 0; i < slabClassTotal.get(); i++) {
            SlabClass sc = all[i];
            all[i] = null;
            for (int j = 0; j < sc.slabclass.length; j++) {
                sc.slabclass[j].clear();
            }
            for (int j = 0; j < sc.slabs.length; j++) {
                sc.slabs[j].destory();
            }
        }
    }

    public static void aa() {
        System.out.println(allSize.get());
        for (int i = 0; i < allSize.get(); i++) {
            for (int j = 0; j < all[i].slabs.length; j++) {
                all[i].slabs[j].getaa();
            }
            System.out.println("------------");
        }
    }

    private int getSlab() {
        int tmp;
        if ((tmp = slabIndex.getAndIncrement()) < slabs.length)
            return tmp;
        return -1;
    }

}
