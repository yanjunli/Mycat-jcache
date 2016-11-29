package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 行知道人 on 2016/11/29.
 */
public class SlabClass {

    private static ByteBuffer allMem = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
    private static LinkedBlockingQueue<Slab> slabs = new LinkedBlockingQueue<Slab>();
    private static LinkedBlockingQueue<Slab> used = new LinkedBlockingQueue<Slab>();
    static {
        for (int i = 0; i < Integer.MAX_VALUE/MemConfig.SLAB_SIZE ; i++) {
            allMem.position(i*MemConfig.SLAB_SIZE);
            allMem.limit((i+1)*MemConfig.SLAB_SIZE);
            slabs.add(new Slab(allMem.slice()));
        }

    }

    public static Slab getSlab(){
        Slab tmp = null;
        if(slabs.size()>0) {
            tmp = slabs.remove();
            used.add(tmp);
        }
        return tmp;
    }
}
