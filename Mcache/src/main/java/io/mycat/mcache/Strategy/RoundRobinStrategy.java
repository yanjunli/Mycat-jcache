package io.mycat.mcache.Strategy;

import io.mycat.mcache.NIOReactor;

/**
 *  轮询策略
 * @author liyanjun
 *
 */
public class RoundRobinStrategy implements ReactorStrategy{

	@Override
	public int getNextReactor(NIOReactor[] reactors,int lastreactor) {
		return (lastreactor + 1) % reactors.length;
	}
}
