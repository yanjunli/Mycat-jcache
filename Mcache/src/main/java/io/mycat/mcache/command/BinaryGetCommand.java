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
 * set 命令 
 * @author liyanjun
 *
 */
public class BinaryGetCommand implements Command{
	
	
	@Override
	public void execute(Connection conn) throws IOException {

		ByteBuffer key = readkey(conn);
		String keystr = new String(cs.decode(key).array());
		System.out.println("执行get 命令   key: "+keystr);
		if(keystr.length()>McacheGlobalConfig.KEY_MAX_LENGTH){
			writeResponse(conn,ProtocolCommand.PROTOCOL_BINARY_CMD_GET.getByte(),
					ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_KEY_ENOENT.getStatus(),0l);
		}else{
			byte[] value = "get command test".getBytes("UTF-8");
			int flags = Integer.parseUnsignedInt("22112");  //暂未实现， 
			byte[] extras = new byte[4];
			extras[0] = (byte) (flags <<24 &0xff);
			extras[1] = (byte) (flags <<16 &0xff);
			extras[2] = (byte) (flags <<8  &0xff);
			extras[3] = (byte) (flags      &0xff);
			BinaryResponseHeader header = buildHeader(null,value,extras);
			writeResponse(conn,header,extras,null,value);
		}
	}
	
	private BinaryResponseHeader buildHeader(byte[] key,byte[] value,byte[] extras){
		BinaryResponseHeader header = new BinaryResponseHeader();
		
		header.setMagic(BinaryProtocol.MAGIC_RESP);
		header.setOpcode(BinaryProtocol.OPCODE_GET);
		header.setKeylen(key!=null?(short)key.length:0);
		header.setExtlen((byte)extras.length);
		header.setDatatype(BinaryProtocol.PROTOCOL_BINARY_RAW_BYTES);
		header.setStatus(ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_SUCCESS.getStatus());
		header.setBodylen(value.length);
		header.setOpaque(0);
		header.setCas(1);
		return header;
	}
}
