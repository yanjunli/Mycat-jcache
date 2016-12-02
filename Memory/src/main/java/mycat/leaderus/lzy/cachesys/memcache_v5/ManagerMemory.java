package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class ManagerMemory {
    private static LinkedBlockingQueue<Chunk>[] used = new LinkedBlockingQueue[MemConfig.SLAB_SIZE / MemConfig.CHUNK_SIZES];
    private static LinkedBlockingQueue<Chunk>[] empty = new LinkedBlockingQueue[MemConfig.SLAB_SIZE / MemConfig.CHUNK_SIZES];

    static {
        for (int i = 0; i < used.length; i++) {
            used[i]= new LinkedBlockingQueue<>();
            empty[i] = new LinkedBlockingQueue<>();
        }
        new Thread(){

            @Override
            public void run() {
                LinkedBlockingQueue<Chunk> tmp = new  LinkedBlockingQueue<Chunk>();
                Chunk tmpChunk = null;
                while (true){
                    for (int i = 0; i < used.length ; i++) {
                        tmp.clear();
                        if (used[i].size() > 0) {
                            try {
                                tmpChunk = used[i].remove();
                            } catch (NoSuchElementException e) {
                                tmpChunk = null;
                            }
                            while (tmpChunk != null) {
                                if (tmpChunk.getTimeout() < System.currentTimeMillis())
                                    empty[i].add(tmpChunk);
                                else
                                    tmp.add(tmpChunk);
                            }
                        }
                        if(tmp.size()>0){
                            used[i].addAll(tmp);
                        }
                    }
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    static Chunk getChunk(int size){
        int index = (size - 1) / MemConfig.CHUNK_SIZES;
        Chunk tmpChunk;
        if(empty[index].size()==0) {
            Slab tmp = SlabClass.getSlab();
            if (tmp != null)
                empty[index].addAll(Arrays.asList(tmp.init((index+1) * MemConfig.CHUNK_SIZES).getChunks()));
        }
        if(empty[index].size()!=0) {
            try {
                tmpChunk = empty[index].remove();
            } catch (NoSuchElementException e) {
                tmpChunk = null;
            }
        }else {
            throw new RuntimeException("内存暂时不足");
        }
        return tmpChunk;
    }
    static void addUsed(Chunk e){
        used[e.getByteBuffer().capacity()/ MemConfig.CHUNK_SIZES-1].add(e);
    }

    static boolean removeChunk(Chunk e){
        int index = e.getByteBuffer().capacity() / MemConfig.CHUNK_SIZES - 1;
        ReadWritePool.remove(e.getKey());
        used[index].remove(e);
        empty[index].add(e);
        return true;
    }
    static boolean removeEmptyChunk(Chunk e){
        int index = e.getByteBuffer().capacity() / MemConfig.CHUNK_SIZES - 1;
        return empty[index].remove(e);
    }
    static boolean removeUsedChunk(Chunk e){
        int index = e.getByteBuffer().capacity() / MemConfig.CHUNK_SIZES - 1;
        return used[index].remove(e);
    }
}
