package mycat.leaderus.lzy.cachesys.memcache_v2;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by 行知道人 on 2016/11/10.
 */
public class MyCatBuffer {
    private int pos;
    private ByteBuffer byteBuffer;
    private int size;
    private Date deadline;
    private boolean isExpired=false;
    public int getPos(){
        return this.pos;
    }
    public MyCatBuffer setPos(int pos){
        this.pos=pos;
        return this;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public MyCatBuffer(int size){
        this.size=size;
        byteBuffer = ByteBuffer.allocateDirect(size);
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }
    public int getSize() {
        return  this.size;
    }

    public MyCatBuffer setIsExpired(boolean b) {
        isExpired=b;
        return this;
    }
}
