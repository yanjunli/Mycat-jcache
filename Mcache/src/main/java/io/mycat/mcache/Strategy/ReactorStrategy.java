package io.mycat.mcache.Strategy;

import io.mycat.mcache.NIOReactor;

/**
 * acceptor 线程分派连接给rector 线程的策略
 * @author liyanjun
 *
 */
public interface ReactorStrategy {

	/**
	 * 获取下一个reactor
	 * @param reactors
	 * @return
	 */
	int getNextReactor(NIOReactor[] reactors,int lastreactor);
	
}
