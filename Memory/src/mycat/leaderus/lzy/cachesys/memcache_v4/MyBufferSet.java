package mycat.leaderus.lzy.cachesys.memcache_v4;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by 行知道人 on 2016/11/28.
 */
public class MyBufferSet implements Runnable{
    private boolean isDead = false;
    private ConcurrentLinkedQueue<MyBuffer> buffers;
    public MyBufferSet(int size){
        buffers=new ConcurrentLinkedQueue<MyBuffer>();
        for (int i = 0; i < 1024*1024/size ; i++) {
            buffers.add(new MyBuffer(size));
        }
    }

    @Override
    public void run() {

    }
}
