package io.mycat.mcache.command;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.handler.BinaryProtocol;

/**
 * set 命令 
 * @author liyanjun
 *
 */
public class BinarySetCommand implements Command{

	@Override
	public void execute(Connection conn) throws IOException {
		ByteBuffer key = readkey(conn);
		ByteBuffer value = readValue(conn);

		System.out.println("执行set 命令   key: "+new String(cs.decode (key).array()));
		System.out.println("执行set 命令   value: "+new String(cs.decode (value).array()));
		
		writeResponse(conn,BinaryProtocol.OPCODE_SET,BinaryProtocol.PROTOCOL_BINARY_RESPONSE_SUCCESS,1l);
	}
}
