package io.mycat.mcache.command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.handler.BinaryProtocol;
import io.mycat.mcache.conn.handler.BinaryRequestHeader;
import io.mycat.mcache.conn.handler.BinaryResponseHeader;

/**
 * 命令接口
 * @author liyanjun
 *
 */
public interface Command {
	
	Charset cs = Charset.forName (McacheGlobalConfig.defaultCahrset);

	/**
	 * 执行命令
	 */
	void execute(Connection conn) throws IOException;
	
	/**
	 * 获取key
	 * @param conn
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	public default ByteBuffer readkey(Connection conn) throws IOException{
		BinaryRequestHeader header = conn.getBinaryRequestHeader();
		ByteBuffer buffer = conn.getReadDataBuffer();
		int keystart  = BinaryProtocol.memcache_packetHeaderSize+ header.getExtlen();
		return getBytes(buffer,keystart, header.getKeylen());
	}
	
	/**
	 * 获取value
	 * @param conn
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	public default ByteBuffer readValue(Connection conn) throws IOException{
		BinaryRequestHeader header = conn.getBinaryRequestHeader();
		ByteBuffer buffer = conn.getReadDataBuffer();
		int keystart  = BinaryProtocol.memcache_packetHeaderSize+ header.getExtlen();
		int valuestart = keystart + header.getKeylen();
		int totalBodylength = header.getBodylen();
		int valuelength = totalBodylength - header.getExtlen() - header.getKeylen();
		return getBytes(buffer,valuestart, valuelength);
	}
	
	/**
	 * 读取 extras 
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public default ByteBuffer readExtras(Connection conn) throws IOException{
		BinaryRequestHeader header = conn.getBinaryRequestHeader();
		ByteBuffer buffer = conn.getReadDataBuffer();
		return getBytes(buffer,BinaryProtocol.memcache_packetHeaderSize,header.getExtlen());
	}
	
	/**
	 * 获取指定长度 的bytebuffer
	 * @param mapBuf
	 * @param index
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public default ByteBuffer getBytes(ByteBuffer mapBuf,int index,int length) throws IOException {
		int oldPos=mapBuf.position();
		mapBuf.position(index);
		ByteBuffer copyBuf=mapBuf.slice();
		copyBuf.limit(length);
		mapBuf.position(oldPos);
		return copyBuf;
	}
	
	/**
	 *  response no body
	 * @param conn
	 * @param opcode
	 * @param status
	 * @param cas
	 */
	public default void writeResponse(Connection conn,byte opcode,short status,long cas){
		ByteBuffer write = conn.getWriteBuffer();
		write.put(BinaryProtocol.MAGIC_RESP);
		write.put(opcode);
		write.putShort((short)0x0000);
		write.put((byte)0x00);
		write.put(BinaryProtocol.PROTOCOL_BINARY_RAW_BYTES);
		write.putShort(status);
		write.putInt(0x00);
		write.putInt(0);
		write.putLong(cas);
		write.flip();
		conn.enableWrite(true);
	}
	
	/**
	 * 待优化
	 *  response no body
	 * @param conn
	 */
	public default void writeResponse(Connection conn,BinaryResponseHeader header,byte[] extras,byte[] key,byte[] value){
		ByteBuffer write = conn.getWriteBuffer();
		write.put(header.getMagic());
		write.put(header.getOpcode());
		write.putShort(header.getKeylen());
		write.put(header.getExtlen());
		write.put(header.getDatatype());
		write.putShort(header.getStatus());
		write.putInt(header.getBodylen());
		write.putInt(header.getOpaque());
		write.putLong(header.getCas());
		if(extras!=null){
			write.put(extras);
		}
		if(key!=null){
			write.put(key);
		}
		if(value!=null){
			write.put(value);
		}
		write.flip();
		conn.enableWrite(true);
	}
}
