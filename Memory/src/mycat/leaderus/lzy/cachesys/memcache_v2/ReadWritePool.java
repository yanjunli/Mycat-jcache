package mycat.leaderus.lzy.cachesys.memcache_v2;


import java.util.Date;
import java.util.LinkedList;

/**
 * Created by 行知道人 on 2016/11/10.
 */
public class ReadWritePool {
    private static MyCatBuffer[] cache ;
    private static LinkedList<Integer> reAllocation = new LinkedList<Integer>();
    private static int pos=0;

    public ReadWritePool(int size) {
        cache = new MyCatBuffer[size];
    }

    public int put(byte[] data){
        int posTmp=0;
        try {
            MyCatBuffer tmpBuffer = DirectByteBufferPool.allocation(data.length,pos<cache.length);
            tmpBuffer.getByteBuffer().put(data).flip();
            tmpBuffer.setDeadline(new Date(System.currentTimeMillis()+600000));
            posTmp=reAllocation.size()>0?reAllocation.removeFirst():pos++;
            cache[posTmp] = tmpBuffer.setPos(posTmp);
            return posTmp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posTmp;
    }

    public byte[] get(int uuid){

        MyCatBuffer tmpBuffer = cache[uuid];
        byte[] data=null;
        if(new Date(System.currentTimeMillis()).compareTo(tmpBuffer.getDeadline())==1){
            DirectByteBufferPool.removeBuffer(cache[uuid].setIsExpired(true));
            cache[uuid]=null;
            reAllocation.add(uuid);
        }else{
            data = new byte[tmpBuffer.getSize()];
            tmpBuffer.getByteBuffer().get(data).flip();
        }
        return data;
    }


}
