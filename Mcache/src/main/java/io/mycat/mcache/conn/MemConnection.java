package io.mycat.mcache.conn;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import io.mycat.mcache.conn.handler.IOHandler;

/**
 * mcache 连接处理器
 * @author liyanjun
 *
 */
public class MemConnection extends Connection {

	public MemConnection(SocketChannel channel)throws IOException  {
		super(channel);
	}
	
	

}
