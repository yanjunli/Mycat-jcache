package io.mycat.mcache.command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.handler.BinaryProtocol;

/**
 * set 命令 
 * @author liyanjun
 *
 */
public class BinaryGetCommand implements Command{

	@Override
	public void execute(Connection conn) throws IOException {

		ByteBuffer key = readkey(conn);
		System.out.println("执行get 命令   key: "+new String(cs.decode (key).array()));
		
	}
}
