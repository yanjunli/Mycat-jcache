package mycat.leaderus.lzy.cachesys.memcache_v5;

/**
 * Created by wadasiwaAcer01 on 2016/12/2.
 */
public class Results {
    public final int flags;
    public final int byteSizes;
    public final byte[] values;

    Results(int flags, int byteSizes, byte[] values) {
        this.flags = flags;
        this.byteSizes = byteSizes;
        this.values = values;
    }

}
