package io.mycat.mcache.command;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mycat.mcache.conn.ConDataBuffer;
import io.mycat.mcache.conn.Connection;

/**
 * 命令接口
 * @author lyj
 *
 */
public interface Command {

	/**
	 * 执行命令
	 */
	void execute(Connection conn,ByteBuffer key,ByteBuffer value) throws IOException;
}
