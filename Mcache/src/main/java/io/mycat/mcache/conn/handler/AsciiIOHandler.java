package io.mycat.mcache.conn.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.conn.Connection;

/**
 * 解析文本协议命令
 * @author liyanjun
 *
 */
public class AsciiIOHandler extends IOHandler {
	
	public static Logger logger = LoggerFactory.getLogger(Connection.class);

	public AsciiIOHandler() throws IOException {
	}

	@Override
	public void onConnected(Connection conn) throws IOException {
		logger.debug("onConnected(): {}", conn);
	}

	@Override
	public void doReadHandler(Connection conn) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doWriterHandler(Connection conn) throws IOException {
		// TODO Auto-generated method stub		
	}

}
