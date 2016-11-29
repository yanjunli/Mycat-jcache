package io.mycat.mcache.conn.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.Command;
import io.mycat.mcache.command.CommandContext;
import io.mycat.mcache.conn.ConDataBuffer;
import io.mycat.mcache.conn.Connection;

/**
 * 解析二进制命令
 * @author liyanjun
 *
 */
public class BinaryIOHandler extends IOHandler {
	
    public static Logger logger = LoggerFactory.getLogger(Connection.class);
    
    public final static int memcache_packetHeaderSize = 24;  //二进制协议头 长度固定为 24字节
    

	public BinaryIOHandler() throws IOException {
	}

	@Override
	public void onConnected(Connection conn) throws IOException {
		logger.debug("onConnected(): {}", conn);
	}

	@Override
	public void doReadHandler(Connection conn) throws IOException {
		
		logger.debug("handleReadEvent(): {}", conn);
        final ConDataBuffer buffer = conn.getReadDataBuffer();
        int offset = buffer.readPos(), limit = buffer.writingPos();
        // 读取到了包头和长度
		// 是否讀完一個報文
		if(!validateHeader(offset, limit)) {
			logger.debug("C#{}B#{} validate protocol packet header: too short, ready to handle next the read event",
				conn.getId(), buffer.hashCode());
			return; 
		}
		int length = getPacketLength(buffer);
		length = 68;
		if((length + offset)> limit) {
			logger.debug("C#{}B#{} nNot a whole packet: required length = {} bytes, cur total length = {} bytes, "
			 	+ "ready to handle the next read event", conn.getId(), buffer.hashCode(), length, limit);
			return;
		}
		if(length == memcache_packetHeaderSize){
			// @todo handle empty packet
			return;
		}
		// 解析报文类型
		final byte opcode = buffer.getByte(1);
		int keylength = buffer.getBytes(2, 2).getShort();
		int extraslength = (int)buffer.getByte(4);
		int keystart  = memcache_packetHeaderSize+ extraslength;
		int valuestart = keystart + keylength;
		int totalBodylength = buffer.getBytes(8, 4).getInt();
		int valuelength = totalBodylength - extraslength - keylength;
		//获取key 和value 
		ByteBuffer key = buffer.getBytes(keystart, keylength);
		ByteBuffer value = buffer.getBytes(valuestart, valuelength);
		//执行命令
		Command command = CommandContext.getCommand(opcode);
		command.execute(conn,buffer,key,value);
	}

	@Override
	public void doWriterHandler(Connection conn) throws IOException {
	}
	
	/**
	 * 校验报文头是否已经全部读取完成
	 * @param offset
	 * @param position
	 * @return
	 */
	private boolean validateHeader(final long offset, final long position){
		return position >= (offset + memcache_packetHeaderSize);
	}
	
	/**
	 * 获取报文长度 
	 * @param buffer
	 * @return （报文头+报文内容）
	 * @throws IOException
	 */
	private int getPacketLength(ConDataBuffer buffer) throws IOException{
		int offset = 8;
		int length = buffer.getByte(offset) & 0xff << 24;
		length |= (buffer.getByte(++offset) & 0xff) << 16;
		length |= (buffer.getByte(++offset) & 0xff) << 8;
		length |= (buffer.getByte(++offset) & 0xff) ;
		return length + memcache_packetHeaderSize;
	}
	
	

}
