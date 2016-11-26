package io.mycat.mcache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.core.util.Assert;

import io.mycat.mcache.Strategy.ReactorSelectEnum;
import io.mycat.mcache.Strategy.ReactorStrategy;
import io.mycat.mcache.Strategy.RoundRobinStrategy;
import io.mycat.mcache.model.Protocol;

/**
 * 
 *@author liyanjun
 */
public class McacheMain 
{
	/**
	 * 主线程 将新连接分派给 reactor 的策略
	 */
	private static Map<ReactorSelectEnum,ReactorStrategy> reactorStrategy = new HashMap<>();
	
    public static void main( String[] args ) throws IOException 
    {    	
    	reactorStrategy.put(ReactorSelectEnum.ROUND_ROBIN, new RoundRobinStrategy());
    	
    	initGlobalConfig();
    	/**
    	 * 后期可能变更为从环境变量获取
    	 */
    	ConfigLoader.loadProperties(null);
    	int port = ConfigLoader.getIntProperty("port",McacheGlobalConfig.defaultPort);
    	int poolsize = ConfigLoader.getIntProperty("reactor.pool.size",McacheGlobalConfig.defaulePoolSize);
    	String bindIp = ConfigLoader.getStringProperty("reactor.pool.bindIp", McacheGlobalConfig.defaultPoolBindIp);
    	String reaStrategy = ConfigLoader.getStringProperty("reactor.pool.selectStrategy", McacheGlobalConfig.defaultReactorSelectStrategy);
    	int backlog = ConfigLoader.getIntProperty("acceptor.max_connect_num", McacheGlobalConfig.defaultMaxAcceptNum);
    	
    	
    	NIOReactorPool reactorPool = new NIOReactorPool(poolsize,reactorStrategy.get(ReactorSelectEnum.valueOf(reaStrategy)));
    	
    	TCPNIOAcceptor acceptor=new TCPNIOAcceptor(bindIp,port, reactorPool,backlog);
		acceptor.start();
    }
    
    /**
     * 初始化全局配置，后期可能变更为从环境变量获取
     */
    public static void initGlobalConfig(){
    	String protStr = System.getProperty("B", "negotiating");  // -B 绑定协议 - 可能值：ascii,binary,auto（默认）
    	if("auto".equals(protStr)){
    		protStr = "negotiating";
    	}

    	McacheGlobalConfig.prot = Protocol.valueOf(protStr);
    }
}
