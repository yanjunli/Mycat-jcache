package mycat.leaderus.lzy.cachesys.memcache_v6;

import java.nio.ByteBuffer;

import static java.lang.System.arraycopy;

/**
 * Created by qd on 2016/12/7.
 */
public class slabs {
    private static final int POWER_SMALLEST=1;
    private static final int POWER_LARGEST =200;
    private static final int CHUNK_ALIGN_BYTES= 8;
    private static final int MAX_NUMBER_OF_SLAB_CLASSES=POWER_LARGEST + 1;

    private SlabClass[]  slabClasses = new SlabClass[MAX_NUMBER_OF_SLAB_CLASSES];
    private static  int powerLargest;//slabclass数组中,已经使用了的元素个数.

    //用户设置的最大内存限制
    private long memLimit =64;//default 64M
    private long memMalloced=0;

    private ByteBuffer memBase;
    private ByteBuffer memCurrent;
    private int memAvail = 0;

    public void initSlabs(int limit,double factor,boolean prealloc){
        int i=POWER_SMALLEST-1;
        memLimit = limit;
        if(prealloc){
            //TODO预分配内存
           memBase = ByteBuffer.allocateDirect(limit);
            if(memBase!=null){
                memCurrent = memBase;
                memLimit = limit;
            }
        }
        initSlabClass();

        int size = Settings.CHUNK_SIZE;
        while(++i<MAX_NUMBER_OF_SLAB_CLASSES-1){
            if(Settings.CHUNK_SIZE%CHUNK_ALIGN_BYTES==0){
                size+=CHUNK_ALIGN_BYTES-(Settings.CHUNK_SIZE%CHUNK_ALIGN_BYTES);
            }
            slabClasses[i].setSize(size);
            slabClasses[i].setPerslab(Settings.ITEM_SIZE_MAX/slabClasses[i].getSize());
            size *=factor;
        }
        powerLargest = i;
        slabClasses[powerLargest].setSize(Settings.ITEM_SIZE_MAX);
        slabClasses[powerLargest].setPerslab(1);

        if(prealloc){
            //TODO预分配内存
            preallocateSlabs(powerLargest);
        }

    }

    //参数值为使用到的slabclass数组元素个数
    //为slabclass数组的每一个元素(使用到的元素)分配内存
    private void preallocateSlabs(int maxSlabs) {
        int prealloc = 0;
        for(int i=POWER_SMALLEST;i<POWER_LARGEST;i++){
            if(++prealloc>maxSlabs){
                return;
            }
            if(doSlabsNewSlab(i)){
                //为每一个slabclass_t分配一个内存页
                //如果分配失败，将退出程序.因为这个预分配的内存是后面程序运行的基础
                //如果这里分配失败了，后面的代码无从执行。所以就直接退出程序
                System.exit(-1);
            }
        }
    }

    //slabclass_t中slab的数目是慢慢增多的。该函数的作用就是为slabclass_t申请多一个slab
    //参数id指明是slabclass数组中的那个slabclass_t
    private boolean doSlabsNewSlab(int id) {
        SlabClass p = slabClasses[id];
        int len = Settings.SLAB_REASSIGN?Settings.ITEM_SIZE_MAX:p.getSize() * p.getPerslab();
        Slab slab = null;
        if((memLimit >0 && memMalloced+len>memLimit && p.getSlabs()>0)
                || (growSlabList(id))
                || (slab = allocateMemory(len))!=null){
            return false;
        }
        splitSlabPageIntoFreeList(id);
        p.getSlabList()[p.getListSize()+1]=slab;
        memMalloced+=len;

        return true;
    }

    private void splitSlabPageIntoFreeList(int id) {
    }

    private Slab allocateMemory(int len) {

        return null;
    }

    //增加slab_list成员指向的内存，也就是增大slab_list数组。使得可以有更多的slab分配器
//除非内存分配失败，否则都是返回1,无论是否真正增大了
    public boolean growSlabList(int id){
        SlabClass p = slabClasses[id];
        if(p.getSlabs()==p.getListSize()){//用完了之前申请到的slab_list数组的所有元素
            int newSize = p.getListSize()!=0?p.getListSize()*2:16;
            Slab[] newList = new Slab[newSize];
            System.arraycopy(p.getSlabList(),0,newList,0,p.getListSize());
            p.setListSize(newSize);
            p.setSlabList(newList);
        }
        return true;
    }

    private void initSlabClass() {
        for(int i=0;i<slabClasses.length;i++){
            slabClasses[i] = new SlabClass();
        }
    }
}
