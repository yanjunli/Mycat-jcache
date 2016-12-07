package mycat.leaderus.lzy.cachesys.memcache_v6;

import java.util.List;

/**
 * Created by qd on 2016/12/7.
 * @author  yanglinlin init
 */
public class SlabClass {
    private int  size;//chunk size;
    private int perslab;

    private Slab[] slabList;

    private int listSize;
}
