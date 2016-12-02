package mycat.leaderus.lzy.cachesys.memcache_v5;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by 行知道人 on 2016/11/29.
 */
class MemConfig {
    public static final int SLAB_SIZE;
    public static final int TOTAL_MEMORY_SIZE;
    public static final int CHUNK_SIZES;

    static {
        Properties properties = new Properties();
        try {
            properties.load(MemConfig.class.getResourceAsStream("/memory.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int slab_size = Integer.parseInt(properties.getProperty("SLAB_SIZE"));
        int total_memory_size = Integer.parseInt(properties.getProperty("TOTAL_MEMORY_SIZE"));
        int chunk_size = Integer.parseInt(properties.getProperty("CHUNK_SIZES"));
        if (slab_size < total_memory_size >>> 3 && slab_size >>> 3 > chunk_size && chunk_size > 0) {
            SLAB_SIZE = slab_size;
            TOTAL_MEMORY_SIZE = total_memory_size;
            CHUNK_SIZES = chunk_size;
        } else {
            SLAB_SIZE = 1048576;
            TOTAL_MEMORY_SIZE = 134217728;
            CHUNK_SIZES = 256;
        }
    }

}
