package mycat.leaderus.lzy.cachesys.memcache;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 行知道人 on 2016/11/10.
 */
public class ReadWritePool {
    private HashMap<UUID,MyCatBuffer> cache = new HashMap<UUID,MyCatBuffer>(1000000);


    synchronized UUID put( byte[] data , long deadline){
        UUID tmpUuid = UUID.randomUUID();
        try {
            MyCatBuffer tmpBuffer = DirectByteBufferPool.allocation(data.length);
            tmpBuffer.getByteBuffer().put(data).flip();
            tmpBuffer.setDeadline(new Date(System.currentTimeMillis()+deadline));
            cache.put(tmpUuid,tmpBuffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpUuid;
    }

    public synchronized byte[] get(UUID uuid){

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
