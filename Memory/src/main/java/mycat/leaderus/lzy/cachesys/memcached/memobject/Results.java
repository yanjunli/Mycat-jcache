package mycat.leaderus.lzy.cachesys.memcached.memobject;

/**
 * Created by wadasiwaAcer01 on 2016/12/2.
 */
public class Results {
    public final int flags;
    public final int byteSizes;
    public final long timeout;
    public final byte[] values;

    Results(int flags, long timeout, int byteSizes, byte[] values) {
        this.flags = flags;
        this.timeout = timeout;
        this.byteSizes = byteSizes;
        this.values = values;
    }

}
