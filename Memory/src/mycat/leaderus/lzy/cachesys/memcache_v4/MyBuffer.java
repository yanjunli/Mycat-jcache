package mycat.leaderus.lzy.cachesys.memcache_v4;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by 行知道人 on 2016/11/10.
 */
public class MyBuffer {
    private ByteBuffer byteBuffer;
    private int size;
    private Date deadline;
    private boolean isExpired=false;

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public MyBuffer(int size){
        this.size=size;
        byteBuffer = ByteBuffer.allocateDirect(size);
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }
    public int getSize() {
        return  this.size;
    }

    public MyBuffer setIsExpired(boolean b) {
        isExpired=b;
        return this;
    }
}
