package io.mycat.mcache.conn;

import java.nio.channels.SocketChannel;

/**
 * mcache 连接处理器
 * @author liyanjun
 *
 */
public class MemConnection extends Connection {

	public MemConnection(SocketChannel channel) {
		super(channel);
	}

}
