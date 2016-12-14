package io.mycat.jcache.net.command.binary;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mycat.jcache.net.McacheGlobalConfig;
import io.mycat.jcache.net.command.Command;
import io.mycat.jcache.net.conn.Connection;
import io.mycat.jcache.net.conn.handler.BinaryProtocol;


/**
 * set 命令 
 * @author liyanjun
 * @author  yanglinlin
 *
 */
public class BinarySetCommand implements Command{

	@Override
	public void execute(Connection conn) throws IOException {
		ByteBuffer key = readkey(conn);
		//key not exists TODO
		if(key.remaining()>McacheGlobalConfig.KEY_MAX_LENGTH) {
			writeResponse(conn, BinaryProtocol.OPCODE_SET, ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_KEY_ENOENT.getStatus(), 1L);
		}
		ByteBuffer value = readValue(conn);
		//TODO
		//value too large
		if(value.remaining()> McacheGlobalConfig.VALUE_MAX_LENGTH){
			writeResponse(conn,BinaryProtocol.OPCODE_SET,ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_E2BIG.getStatus(),1l);
		}

		System.out.println("执行set 命令   key: "+new String(cs.decode (key).array()));
		System.out.println("执行set 命令   value: "+new String(cs.decode (value).array()));
		
		writeResponse(conn,BinaryProtocol.OPCODE_SET,ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_SUCCESS.getStatus(),1l);
	}
}
