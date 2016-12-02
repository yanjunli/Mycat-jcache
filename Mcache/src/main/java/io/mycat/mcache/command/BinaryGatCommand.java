package io.mycat.mcache.command;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.binary.ProtocolCommand;
import io.mycat.mcache.command.binary.ProtocolResponseStatus;
import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.handler.BinaryProtocol;
import io.mycat.mcache.conn.handler.BinaryResponseHeader;

/**
 * got 命令 
 * @author liyanjun
 *
 */
public class BinaryGatCommand implements Command{
	
	@Override
	public void execute(Connection conn) throws IOException {
//    TODO
	}
}
