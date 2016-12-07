package mycat.leaderus.lzy.cachesys.memcache_v6;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by qd on 2016/12/7.
 * @author  yanglinlin init
 */
public class SlabClass {
    private int size;//slab分配器分配的item的大小
    private int perslab;//每一个slab分配器能分配多少个item

    private LinkedList slots;//指向空闲item链表
    private int slCurr;//空闲item的个数

    //这个是已经分配了内存的slabs个数。list_size是这个slabs数组(slab_list)的大小
    private int slabs;//本slabclass可用的slab分配器个数

    //slab数组，数组的每一个元素就是一个slab分配器，这些分配器都分配相同尺寸的内存
    private Slab[] slabList;

    private int listSize;//slab数组的大小, list_size >= slabs

    //用于reassign，指明slabclass_t中的哪个块内存要被其他slabclass_t使用
    private int killing;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPerslab() {
        return perslab;
    }

    public void setPerslab(int perslab) {
        this.perslab = perslab;
    }

    public LinkedList getSlots() {
        return slots;
    }

    public void setSlots(LinkedList slots) {
        this.slots = slots;
    }

    public int getSlCurr() {
        return slCurr;
    }

    public void setSlCurr(int slCurr) {
        this.slCurr = slCurr;
    }

    public int getSlabs() {
        return slabs;
    }

    public void setSlabs(int slabs) {
        this.slabs = slabs;
    }

    public Slab[] getSlabList() {
        return slabList;
    }

    public void setSlabList(Slab[] slabList) {
        this.slabList = slabList;
    }

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public int getKilling() {
        return killing;
    }

    public void setKilling(int killing) {
        this.killing = killing;
    }
}