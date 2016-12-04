package io.mycat.mcache;

import java.io.IOException;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	MemCachedClient mcc = new MemCachedClient(true);  //true 代表 二进制协议，false 代表 文本协议
	
	@BeforeClass
    public static void setup() throws Exception{
		
//		McacheMain.main(null);
		
		
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

//    @Test
//    public void testReadWritePool() throws InterruptedException {
//        ReadWritePool.set("123", 0, "123".getBytes().length, System.currentTimeMillis() + 1000, "123".getBytes());
//        ReadWritePool.set("1234", 0, "1234".getBytes().length, System.currentTimeMillis() + 100, "123".getBytes());
//        ReadWritePool.set("1235", 0, "1235".getBytes().length, System.currentTimeMillis() + 100000, "123".getBytes());
//        ReadWritePool.set("1236", 0, "1236".getBytes().length, System.currentTimeMillis() + 100000, "123".getBytes());
//        ReadWritePool.set("1237", 0, "1237".getBytes().length, System.currentTimeMillis() + 100000, "123".getBytes());
//        System.out.println(new String(ReadWritePool.get(new String[]{"123"})[0].values));
//        System.out.println(new String(ReadWritePool.get(new String[]{"1235"})[0].values));
//        System.out.println(new String(ReadWritePool.get(new String[]{"1236"})[0].values));
//        System.out.println(new String(ReadWritePool.get(new String[]{"1237"})[0].values));
//        System.out.println(new String(ReadWritePool.get(new String[]{"1234"})[0].values));
//        Thread.sleep(1000);
//        //System.out.println(new String(ReadWritePool.get(new String[]{"123"})[0].values));
//    }

    @Test
    public void testAddCommand(){
        mcc.set("test","add command");
        Object str = mcc.get("test");;
        System.out.println(str);
        boolean result = mcc.add("test", "This is a add command");
        System.out.println(result);
    }
    
    @Test
    public void getMulti(){
    	 Map<String,Object> bars = mcc.getMulti(new String[]{"foo","foo1"});
         System.out.println(">>> " + bars);
    }
    
    @Test
    public void teadGets(){
    	 Object bars = mcc.gets("foo");
         System.out.println(">>> " + bars);
    }
    
    @Test
    public void testgetMultiArray(){
    	 Object bars = mcc.getMultiArray(new String[]{"foo","foo1"});
         System.out.println(">>> " + bars);
    }
    
    @Test
    public void testgetMultiArray1(){
    	 Object bars = mcc.keyExists("foo");
    	 System.out.println(">>> " + bars);
    }

}
