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
public class BinarySetCommand implements Command{

	@Override
	public void execute(Connection conn,ByteBuffer key,ByteBuffer value) throws IOException {
		

		System.out.println("执行set 命令   key: "+new String(key.array(),"UTF-8"));
		System.out.println("执行set 命令   value: "+new String(value.array(),"UTF-8"));
		
		ByteBuffer write = conn.getWriteBuffer();
		write.put(BinaryProtocol.MAGIC_RESP);
		write.put(BinaryProtocol.OPCODE_SET);
		write.putShort((short)0x0000);
		write.put((byte)0x00);
		write.put(BinaryProtocol.PROTOCOL_BINARY_RAW_BYTES);
		write.putShort(BinaryProtocol.noerror);
		write.putInt(0x00);
		write.putInt(0);
		write.putLong(1l);
		write.flip();
		System.out.println(1111);
		conn.enableWrite(true);
	}
}
