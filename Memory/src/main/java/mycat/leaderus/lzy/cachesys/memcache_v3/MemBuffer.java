package mycat.leaderus.lzy.cachesys.memcache_v3;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 行知道人 on 2016/11/23.
 */
public class MemBuffer {
    private static ConcurrentHashMap<Integer,LinkedList<ByteBuffer>> empty = new ConcurrentHashMap<Integer,LinkedList<ByteBuffer>>();
    private static ConcurrentHashMap<Integer,LinkedList<ByteBuffer>> used = new ConcurrentHashMap<Integer,LinkedList<ByteBuffer>>();
    public static ByteBuffer getBuffer(int size){
        ByteBuffer tmpBuffer = null;
        LinkedList<ByteBuffer> tmpList = empty.get(size)!=null?empty.get(size):null;
        if(tmpList.size()>0)
            tmpBuffer = tmpList.getFirst()!=null?tmpList.removeFirst():null;
        if(tmpBuffer==null){
            tmpBuffer=used.get(size)!=null?used.get(size).removeFirst()!=null?used.get(size).removeFirst():null:null;
        }
        if(tmpBuffer==null){

        }
        if(tmpBuffer!=null){
            used.get(size).addLast(tmpBuffer);
        }
        return tmpBuffer;
    }
}
