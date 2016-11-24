package io.mycat.mcache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.mycat.mcache.Strategy.ReactorSelectEnum;
import io.mycat.mcache.Strategy.ReactorStrategy;
import io.mycat.mcache.Strategy.RoundRobinStrategy;

/**
 * 
 *@author 
 */
public class McacheMain 
{
	
	private static Map<ReactorSelectEnum,ReactorStrategy> reactorStrategy = new HashMap<>();	
	
    public static void main( String[] args ) throws IOException 
    {    	
    	reactorStrategy.put(ReactorSelectEnum.ROUND_ROBIN, new RoundRobinStrategy());
    	
    	ConfigLoader.loadProperties(null);
    	int port = ConfigLoader.getIntProperty("port",McacheGlobalConfig.defaultPort);
    	int poolsize = ConfigLoader.getIntProperty("reactor.pool.size",McacheGlobalConfig.defaulePoolSize);
    	String bindIp = ConfigLoader.getStringProperty("reactor.pool.bindIp", McacheGlobalConfig.defaultPoolBindIp);
    	String reaStrategy = ConfigLoader.getStringProperty("reactor.pool.selectStrategy", McacheGlobalConfig.defaultReactorSelectStrategy);
    	int backlog = ConfigLoader.getIntProperty("acceptor.max_connect_num", McacheGlobalConfig.defaultMaxAcceptNum);
    	NIOReactorPool reactorPool = new NIOReactorPool(poolsize,reactorStrategy.get(ReactorSelectEnum.valueOf(reaStrategy)));
    	NIOAcceptor acceptor=new NIOAcceptor(bindIp,port, reactorPool,backlog);
		acceptor.start();
		
		FileInputStream
    }
}
