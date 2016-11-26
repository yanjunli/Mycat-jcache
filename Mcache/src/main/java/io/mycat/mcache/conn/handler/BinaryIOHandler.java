package io.mycat.mcache.conn.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.conn.ConDataBuffer;
import io.mycat.mcache.conn.Connection;

/**
 * 解析二进制命令
 * @author liyanjun
 *
 */
public class BinaryIOHandler extends IOHandler {
	
    public static Logger logger = LoggerFactory.getLogger(Connection.class);

	public BinaryIOHandler() throws IOException {
	}

	@Override
	public void onConnected(Connection conn) throws IOException {
		logger.debug("onConnected(): {}", conn);
	}

	@Override
	public void doReadHandler(Connection conn) throws IOException {
		// TODO 
	}

	@Override
	public void doWriterHandler(Connection conn) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
