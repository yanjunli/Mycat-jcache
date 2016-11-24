package io.mycat.mcache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.mycat.mcache.Strategy.ReactorStrategy;


/**
 * rector 线程池  也是acceptor线程分配 连接给 具体某一个rector 线程的策略上下文
 * @author liyanjun
 */
public class NIOReactorPool {

	private final NIOReactor[] reactors;
	
	private final String rectorname = "rector";
	
	private final ReactorStrategy startegy;
	
	private int lastreactor; //上一次处理连接的reactor
	
	public NIOReactorPool(int poolSize,ReactorStrategy startegy) throws IOException{
		this.reactors = new NIOReactor[poolSize];
		this.startegy = startegy;
		Map<String,NIOReactor> reactorMap=new HashMap<String,NIOReactor>();
		for (int i = 0; i < poolSize; i++) {
			NIOReactor reactor = new NIOReactor(rectorname + "-" + i);
			reactors[i] = reactor;
			reactor.start();
			reactorMap.put(reactor.getName(), reactor);
		}
	}

	/**
	 * 获取下一个处理连接的reactor
	 * @return
	 */
	public NIOReactor getNextReactor(){
		lastreactor = startegy.getNextReactor(reactors,lastreactor);
		return reactors[lastreactor];
	}
}
