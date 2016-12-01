package mycat.leaderus.lzy.cachesys.memcache_v2;

/**
 * Created by 行知道人 on 2016/11/4.
 */
public class DirectByteBufferPool {

    private static MyCatBufferSet[] bufferPools = new MyCatBufferSet[1024*3];
//    private static final int _4K = 4 * 1024;
//    private static final int _16K = 4 * _4K;
//    private static final int _256K = 16 * _16K;
//    private static final int _64M = 256 * _256K;

    public static MyCatBuffer allocation(int size,boolean flag) {
        MyCatBuffer tmp = bufferPools[size] != null ? bufferPools[size].getBuffer(flag) : null;
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
        if(flag) {
            if (tmp == null) {
                MyCatBufferSet tmpSet = new MyCatBufferSet();
                tmp = new MyCatBuffer(size);
                tmpSet.add(tmp);
                bufferPools[size] = tmpSet;
            }
        }else{
            if(tmp==null)
                System.err.println("缓存耗尽");
        }
        return tmp;
    }

    public static boolean removeBuffer(MyCatBuffer e){
         return bufferPools[e.getSize()].removeBuffer(e);
    }
}
