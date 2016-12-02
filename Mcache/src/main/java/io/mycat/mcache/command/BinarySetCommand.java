package io.mycat.mcache.command;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.binary.ProtocolResponseStatus;
import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.handler.BinaryProtocol;
import mycat.leaderus.lzy.cachesys.memcache_v5.ReadWritePool;

import java.io.IOException;
import java.nio.ByteBuffer;

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
		//key not exists
		byte[] ds = new byte[key.remaining()];
		key.get(ds);
		if(ds.length>McacheGlobalConfig.KEY_MAX_LENGTH) {
			writeResponse(conn, BinaryProtocol.OPCODE_SET, ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_KEY_ENOENT.getStatus(), 0L);
		}

		//extras
		ByteBuffer extras = readExtras(conn);
		extras.limit(extras.position() + 4);
		ByteBuffer flags = extras.slice();
		extras.position(extras.limit());
		ByteBuffer expiry = extras.slice();
		ByteBuffer value = readValue(conn);
		//value too large
		ds = new byte[value.remaining()];
		if(ds.length> McacheGlobalConfig.VALUE_MAX_LENGTH){
			writeResponse(conn,BinaryProtocol.OPCODE_SET,ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_E2BIG.getStatus(),0l);
		}
		//存储
		String keyS = new String(cs.decode(key).array());
		System.out.println("执行set 命令   key: "+new String(cs.decode (key).array()));
		System.out.println("执行set 命令   value: "+new String(cs.decode (value).array()));
		int set = ReadWritePool.set(keyS,flags.getInt(),ds.length,expiry.getInt(),ds);
		System.out.println("set result:"+set);
		writeResponse(conn,BinaryProtocol.OPCODE_SET,ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_SUCCESS.getStatus(),1l);
	}
}
