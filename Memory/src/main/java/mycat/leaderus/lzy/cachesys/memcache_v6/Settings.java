package mycat.leaderus.lzy.cachesys.memcache_v6;

/**
 * Created by qd on 2016/12/7.
 */
public class Settings {
    public static final int POWER_SMALLEST=1;
    public static final int POWER_LARGEST=256;

    public static final int CHUNK_SIZE=48;
    public static final double factor=1.25;

    public static final int ITEM_SIZE_MAX=2014 *1024; /* The famous 1MB upper limit. */
    public static final int SLAB_PAGE_SIZE=1024*1024;/* chunks are split from 1MB pages. */

    public static final int SLAB_CHUNK_SIZE_MAX=SLAB_PAGE_SIZE;

}
