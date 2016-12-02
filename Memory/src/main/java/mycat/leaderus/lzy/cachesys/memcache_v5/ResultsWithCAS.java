package mycat.leaderus.lzy.cachesys.memcache_v5;

/**
 * Created by wadasiwaAcer01 on 2016/12/2.
 */
public class ResultsWithCAS {
    public final int flags;
    public final int byteSizes;
    public final byte[] values;
    public final long CAS;

    ResultsWithCAS(int flags, int byteSizes, byte[] values, long CAS) {
        this.flags = flags;
        this.byteSizes = byteSizes;
        this.values = values;
        this.CAS = CAS;
    }

}
