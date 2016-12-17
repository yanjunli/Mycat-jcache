package io.mycat.jcache.net.command.binary;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.mycat.jcache.context.JcacheContext;
import io.mycat.jcache.net.JcacheGlobalConfig;
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

		String keystr = new String(cs.decode(key).array());
		ByteBuffer value = readValue(conn);
				
		ByteBuffer extras = readExtras(conn);
		
		int flags = extras.getInt();
		int exptime = extras.getInt(4);
		
		long addr = JcacheContext.getItemsAccessManager().item_alloc(keystr, flags, exptime, readValueLength(conn)+2);
		
		if(addr==0){
			//TODO
		}
		
		//TODO
		//value too large
		if(value.remaining()> JcacheGlobalConfig.VALUE_MAX_LENGTH){
			writeResponse(conn,BinaryProtocol.OPCODE_SET,ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_E2BIG.getStatus(),1l);
		}

		System.out.println("执行set 命令   key: "+new String(cs.decode (key).array()));
		System.out.println("执行set 命令   value: "+new String(cs.decode (value).array()));
		
		writeResponse(conn,BinaryProtocol.OPCODE_SET,ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_SUCCESS.getStatus(),1l);
	}
}
