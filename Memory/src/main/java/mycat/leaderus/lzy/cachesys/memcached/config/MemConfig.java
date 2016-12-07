package mycat.leaderus.lzy.cachesys.memcached.config;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by PigBrother(LZS/LZY) on 2016/12/7 10:02.
 */
public class MemConfig {
    public static final int SLAB_SIZE;
    public static final int PAGE_TOTAL_MEMORY_SIZE;
    public static final int CHUNK_SIZES;
    public static final double FACTOR;

    static {
        Properties properties = new Properties();
        int slab_size = 0, page_total_memory_size = 0, chunk_size = 0;
        double factor = 0;
        try {
            properties.load(MemConfig.class.getResourceAsStream("/memory_memcached.properties"));
            slab_size = Integer.parseInt(properties.getProperty("SLAB_SIZE"));
            page_total_memory_size = Integer.parseInt(properties.getProperty("PAGE_TOTAL_MEMORY_SIZE"));
            chunk_size = Integer.parseInt(properties.getProperty("CHUNK_SIZES"));
            factor = Double.parseDouble(properties.getProperty("FACTOR"));
            if (!(slab_size < page_total_memory_size >>> 3 && slab_size >>> 3 > chunk_size && chunk_size > 0 && factor > 1)) {
                throw new NumberFormatException();
            }
        } catch (IOException | NumberFormatException e) {
            slab_size = 1024 << 10;
            page_total_memory_size = 2047 << 20;
            chunk_size = 88;
            factor = 1.25;
        } finally {
            SLAB_SIZE = slab_size;
            PAGE_TOTAL_MEMORY_SIZE = page_total_memory_size;
            CHUNK_SIZES = chunk_size;
            FACTOR = factor;
        }
    }

}
