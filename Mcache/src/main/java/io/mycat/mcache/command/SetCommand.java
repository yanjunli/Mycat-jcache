package io.mycat.mcache.command;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mycat.mcache.conn.ConDataBuffer;
import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.handler.BinaryProtocol;

/**
 * set 命令 
 * @author liyanjun
 *
 */
public class SetCommand implements Command{

	@Override
	public void execute(Connection conn,ConDataBuffer buffer,ByteBuffer key,ByteBuffer value) throws IOException {
		

		System.out.println("执行set 命令   key: ");
		System.out.println("执行set 命令   value: ");
		
		ByteBuffer write = conn.getWriteBuffer().beginWrite(24);
		write.put(BinaryProtocol.MAGIC_RESP);
		write.put(BinaryProtocol.OPCODE_SET);
		write.putShort((short)0x0000);
		write.put((byte)0x00);
		write.put(BinaryProtocol.PROTOCOL_BINARY_RAW_BYTES);
		write.putShort(BinaryProtocol.noerror);
		write.putInt(0x00);
		write.putInt(0);
		write.putLong(1l);
		conn.getWriteBuffer().endWrite(write);
		conn.enableWrite(true);
	}

}
