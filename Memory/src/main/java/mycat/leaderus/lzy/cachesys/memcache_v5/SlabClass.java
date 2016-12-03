package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 行知道人 on 2016/11/29.
 */
class SlabClass {

    private static ByteBuffer allMem = ByteBuffer.allocateDirect(MemConfig.TOTAL_MEMORY_SIZE);
    private static LinkedBlockingQueue<Slab> slabs = new LinkedBlockingQueue<Slab>();
    private static LinkedBlockingQueue<Slab> used = new LinkedBlockingQueue<Slab>();
    private static LinkedBlockingQueue<Slab> tmpUsed = new LinkedBlockingQueue<Slab>();

    static {
        for (int i = 0; i < MemConfig.TOTAL_MEMORY_SIZE / MemConfig.SLAB_SIZE; i++) {
            allMem.position(i * MemConfig.SLAB_SIZE);
            allMem.limit((i + 1) * MemConfig.SLAB_SIZE);
            slabs.add(new Slab(allMem.slice()));

        }

        new Thread() {
            @Override
            public void run() {
                LinkedBlockingQueue<Chunk> tmpEmpChunks = new LinkedBlockingQueue<>();
                Slab tmp;
                while (true) {
                    if (used.size() > 10) {
                        try {
                            tmp = used.remove();
                        } catch (NoSuchElementException e) {
                            tmp = null;
                        }
                        if (tmp != null) {
                            Chunk[] tmpChunks = tmp.getChunks();
                            if (tmpChunks != null)
                                for (int i = 0; i < tmpChunks.length; i++) {
                                    if (ManagerMemory.removeEmptyChunk(tmpChunks[i])) {
                                        tmpEmpChunks.add(tmpChunks[i]);
                                    }
                                }
                            if (tmpEmpChunks.size() == tmpChunks.length) {
                                slabs.add(tmp);
                            } else {
                                ManagerMemory.allEmptyChunks(tmpEmpChunks, tmpChunks[0].getByteBuffer().capacity() / MemConfig.CHUNK_SIZES - 1);
                                used.add(tmp);
                            }
                            tmpEmpChunks.clear();
                        }
                    }
                    if (tmpUsed.size() > 0) {
                        used.addAll(tmpUsed);
                        tmpUsed.clear();
                    }
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    static Slab getSlab() {
        Slab tmp = null;
        if (slabs.size() > 0) {
            try {
                tmp = slabs.remove();
            } catch (NoSuchElementException e) {
                return null;
            }
            used.add(tmp);
        }
        return tmp;
    }
}
