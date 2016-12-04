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
 * Touch is used to set a new expiration time for an existing item. 
 * GAT (Get and touch) and GATQ will return the value 
 * for the object if it is present in the cache.
 * 
 * Request:

	MUST have extras.
	MUST have key.
	MUST NOT have value.

	Extra data for set/add/replace:

     Byte/     0       |       1       |       2       |       3       |
        /              |               |               |               |
       |0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|0 1 2 3 4 5 6 7|
       +---------------+---------------+---------------+---------------+
      0| Expiration                                                    |
       +---------------+---------------+---------------+---------------+
       Total 4 bytes
       
 * @author liyanjun
 *
 */
public class BinaryTouchCommand implements Command{
	
	private static final Logger logger = LoggerFactory.getLogger(BinaryTouchCommand.class);
	
	@Override
	public void execute(Connection conn) throws IOException {
		int keylen = conn.getBinaryRequestHeader().getKeylen();
		int bodylen = conn.getBinaryRequestHeader().getBodylen();
		int extlen  = conn.getBinaryRequestHeader().getExtlen();
		
		if (extlen == 0 && bodylen == keylen && keylen > 0) {
			ByteBuffer key = readkey(conn);
			String keystr = new String(cs.decode(key).array());
			System.out.println("执行gat 命令   key: "+keystr);
			byte[] value = "This is a test String".getBytes("UTF-8");
			int flags = 0x00000020;
			byte[] extras = new byte[4];
			extras[0] = (byte) (flags <<24  &0xff);
			extras[1] = (byte) (flags <<16  &0xff);
			extras[2] = (byte) (flags <<8   &0xff);
			extras[3] = (byte) (flags       &0xff);
			BinaryResponseHeader header = buildHeader(conn.getBinaryRequestHeader(),BinaryProtocol.OPCODE_GET,null,value,extras,1l);
			writeResponse(conn,header,extras,null,value);
		} else {
			writeResponse(conn, BinaryProtocol.OPCODE_GET, ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_EINVAL.getStatus(), 0L);
		}
	}
}
