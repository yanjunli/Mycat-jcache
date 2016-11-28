package mycat.leaderus.lzy.cachesys.memcache;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by 行知道人 on 2016/11/10.
 */
public class ReadWritePool_v2 {
    private static HashMap<String,MyCatBuffer> cache = new HashMap<String,MyCatBuffer>(1000000);


    private static synchronized boolean put(String key, byte[] data , long deadline){
        try {
            MyCatBuffer tmpBuffer = DirectByteBufferPool.allocation(data.length);
            tmpBuffer.getByteBuffer().put(data).flip();
            tmpBuffer.setDeadline(new Date(System.currentTimeMillis()+deadline));
            cache.put(key,tmpBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static synchronized byte[] get(String uuid){

        MyCatBuffer tmpBuffer = cache.get(uuid);
        byte[] data;
        if(new Date(System.currentTimeMillis()).compareTo(tmpBuffer.getDeadline())==1){
            DirectByteBufferPool.removeBuffer(cache.get(uuid).setIsExpired(true));
            cache.remove(uuid);
            data = null;
        }else{
            data = new byte[tmpBuffer.getSize()];
            tmpBuffer.getByteBuffer().get(data).flip();
        }
        return data;
    }


}
