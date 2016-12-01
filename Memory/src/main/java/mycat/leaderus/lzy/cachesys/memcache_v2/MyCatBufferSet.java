package mycat.leaderus.lzy.cachesys.memcache_v2;

import java.util.LinkedList;

/**
 * Created by 行知道人 on 2016/11/10.
 */
public class MyCatBufferSet implements Comparable<MyCatBufferSet>{

    private LinkedList<MyCatBuffer> listMyCatBufferSets=null;
    private int capacity=0;
    private int size=0;
    private int usedSize=0;
    public boolean add(MyCatBuffer e){
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
    public MyCatBuffer getBuffer(boolean flag){
        MyCatBuffer tmp=null;
        if(flag) {
            if (usedSize < size) {
                tmp = listMyCatBufferSets.get(usedSize++);
            } else {
                tmp = new MyCatBuffer(capacity);
                listMyCatBufferSets.addLast(tmp);
                usedSize++;
                for (int i = 0; i < 10; i++) {
                    listMyCatBufferSets.addLast(new MyCatBuffer(capacity));
                }
                size = listMyCatBufferSets.size();
            }
        } else {
            if (usedSize < size) {
                tmp = listMyCatBufferSets.get(usedSize++);
            }
        }
        return tmp;
    }
    public boolean removeBuffer(MyCatBuffer e){
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
    public int compareTo(MyCatBufferSet o) {
        return this.capacity>o.capacity?1:this.capacity<o.capacity?-1:0;
    }

}
