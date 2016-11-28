package mycat.leaderus.lzy.cachesys.memcache;

import java.util.LinkedList;

/**
 * Created by 行知道人 on 2016/11/10.
 */
 class MyCatBufferSet implements Comparable<MyCatBufferSet>{

    private LinkedList<MyCatBuffer> listMyCatBufferSets=null;
    private int capacity=0;
    private int size=0;
    private int usedSize=0;
    public synchronized boolean add(MyCatBuffer e){
        if(listMyCatBufferSets==null) {
            listMyCatBufferSets = new LinkedList<MyCatBuffer>();
            capacity = e.getSize();
        }
        usedSize++;
        listMyCatBufferSets.addLast(e);
        size=listMyCatBufferSets.size();
        return true;
    }
    public int getCapacity(){
        return capacity;
    }
    public synchronized MyCatBuffer getBuffer(){
        MyCatBuffer tmp;
        if(usedSize<size){
            tmp=listMyCatBufferSets.get(usedSize++);
        }else{
            tmp=new MyCatBuffer(capacity);
            listMyCatBufferSets.addLast(tmp);
            usedSize++;
            for (int i = 0; i < 10; i++) {
                listMyCatBufferSets.addLast(new MyCatBuffer(capacity));
            }
            size=listMyCatBufferSets.size();
        }
        return tmp;
    }
    public synchronized boolean removeBuffer(MyCatBuffer e){
        e.getByteBuffer().clear();
        listMyCatBufferSets.remove(e);
        if(size-usedSize<10) {
            listMyCatBufferSets.addLast(e);
        }
        size=listMyCatBufferSets.size();
        usedSize--;
        return true;
    }
    @Override
    public synchronized int compareTo(MyCatBufferSet o) {
        return this.capacity>o.capacity?1:this.capacity<o.capacity?-1:0;
    }

}
