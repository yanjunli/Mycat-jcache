package io.mycat.mcache.command;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.binary.ProtocolCommand;
import io.mycat.mcache.command.binary.ProtocolResponseStatus;
import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.handler.BinaryProtocol;
import io.mycat.mcache.conn.handler.BinaryResponseHeader;

/**
 * gotk 命令 
 * @author liyanjun
 *
 */
public class BinaryGatKCommand implements Command{
	
	private static final Logger logger = LoggerFactory.getLogger(BinaryGatKCommand.class);
	
	
	@Override
	public void execute(Connection conn) throws IOException {
		//TODO
	}
}
