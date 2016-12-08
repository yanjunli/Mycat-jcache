package mycat.leaderus.lzy.cachesys.memcached.memobject;

/**
 * Created by wadasiwaAcer01 on 2016/12/2.
 */
public class ResultsWithCAS {
    public final int flags;
    public final int byteSizes;
    public final long timeout;
    public final byte[] values;
    public final long CAS;

    public ResultsWithCAS(int flags, long timeout, long CAS, int byteSizes, byte[] values) {
        this.flags = flags;
        this.timeout = timeout;
        this.byteSizes = byteSizes;
        this.values = values;
        this.CAS = CAS;
    }

    public Results getResults() {
        return new Results(flags, timeout, byteSizes, values);
    }


}
