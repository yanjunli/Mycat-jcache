package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class Slab {
    private Chunk[] chunks;
    private ByteBuffer allChunks;
    Slab(ByteBuffer slice) {
        this.allChunks = slice;
    }
    Slab init(int size){
        chunks = new Chunk[MemConfig.SLAB_SIZE/size];
        for (int i = 0; i < chunks.length ; i++) {
            allChunks.position(i*size);
            allChunks.limit((i+1)*size);
            chunks[i] = new Chunk(allChunks.slice());
        }
        return this;
    }
    Chunk[] getChunks(){
        return this.chunks;
    }
}
