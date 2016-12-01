package mycat.leaderus.lzy.cachesys.memcache;

import java.util.HashMap;

/**
 * Created by 行知道人 on 2016/11/4.
 */
 class DirectByteBufferPool {

    private static HashMap<Integer,MyCatBufferSet> bufferPools = new HashMap<Integer,MyCatBufferSet>();
//    private static final int _4K = 4 * 1024;
//    private static final int _16K = 4 * _4K;
//    private static final int _256K = 16 * _16K;
//    private static final int _64M = 256 * _256K;

    public static synchronized MyCatBuffer allocation(int size) {
        MyCatBuffer tmp=bufferPools.get(size)!=null?bufferPools.get(size).getBuffer():null;
//        if(size < _4K){size = _4K;
//        } else if ( size < _16K ) {size = _16K;
//        } else if ( size < _256K) {size = _256K;
//        } else if ( size < _64M ) {size = _64M;
//        } else {throw new Exception("分配的大小过大，出错");}
        
//        if(!bufferPools.isEmpty()) {
//            MyCatBufferSet tmpSet = bufferPools.get(size);
//            while (iter.hasNext()) {
//                MyCatBufferSet tmpSet = iter.next();
//                if (tmpSet.getCapacity() == size) {
//                    return tmpSet.getBuffer();
//                }
//            }
//        }
        if(tmp==null){
            MyCatBufferSet tmpSet = new MyCatBufferSet();
            tmp = new MyCatBuffer(size);
            tmpSet.add(tmp);
            bufferPools.put(size,tmpSet);
        }
        return tmp;
    }

    public static synchronized boolean removeBuffer(MyCatBuffer e){
         return bufferPools.get(e.getSize()).removeBuffer(e);
    }
}
