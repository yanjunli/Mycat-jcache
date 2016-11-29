package io.mycat.mcache;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	MemCachedClient mcc = new MemCachedClient(true);  //true 代表 二进制协议，false 代表 文本协议
	
	@Before
    public void setup() {
		// 设置缓存服务器列表，当使用分布式缓存的时，可以指定多个缓存服务器。这里应该设置为多个不同的服务，我这里将两个服务设置为一样的，大家不要向我学习，呵呵。
        String[] servers =
                {
                        "127.0.0.1:9000"
                };

        // 设置服务器权重
        Integer[] weights = {3};

        // 创建一个Socked连接池实例
        SockIOPool pool = SockIOPool.getInstance();

      // 向连接池设置服务器和权重
        pool.setServers(servers);
        pool.setWeights(weights);

        // set some TCP settings
        // disable nagle
        // set the read timeout to 3 secs
        // and don't set a connect timeout
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);

       // initialize the connection pool
        pool.initialize();
    }
	
	@Test
	public void testConfigLoader(){
		try {
			ConfigLoader.loadProperties(null);
			System.out.println(ConfigLoader.getProperty("DB_SERVER"));
			ConfigLoader.forEach();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testsetCommand(){
        boolean result = mcc.set("foo", "This is a test String");
        System.out.println(result);
        String bar = mcc.get("foo").toString();
        System.out.println(">>> " + bar);
	}
}
