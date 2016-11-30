package io.mycat.mcache.conn.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.Command;
import io.mycat.mcache.command.CommandContext;
import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.model.Protocol;

public class IOHandler{
	
    public static Logger logger = LoggerFactory.getLogger(Connection.class);

	public IOHandler() throws IOException {}
	
	public void onClosed(Connection conn,String reason) {}

	public void onConnected(Connection conn) throws IOException {
		logger.debug("onConnected(): {}", conn);
	}

	public void doReadHandler(Connection conn) throws IOException{
		logger.debug("handleReadEvent(): {}", this);
        final ByteBuffer buffer = conn.getReadDataBuffer();
        Command command = null;

        if(Protocol.negotiating.equals(McacheGlobalConfig.prot)){
        	byte magic = buffer.get(0);
        	if((magic & 0xff)==(BinaryProtocol.MAGIC_REQ & 0xff)){
        		conn.setProtocol(Protocol.binary);
        	}else{
        		conn.setProtocol(Protocol.ascii);
        	}
        }
        
        /**
         * 如果是二进制协议
         */
        if(Protocol.binary.equals(conn.getProtocol())){  	
        	int offset = buffer.position();
        	int limit  = buffer.limit();
            // 读取到了包头和长度
    		// 是否讀完一個報文
    		if(!validateHeader(offset, limit)) {
    			logger.debug("C#{}B#{} validate protocol packet header: too short, ready to handle next the read event offset{},limit{}",
    				conn.getId(), buffer.hashCode(),offset,limit);
    			return; 
    		}
    		int length = getPacketLength(buffer);
    		if((length + offset)> limit) {
    			logger.debug("C#{}B#{} nNot a whole packet: required length = {} bytes, cur total length = {} bytes, "
    			 	+ "ready to handle the next read event", conn.getId(), buffer.hashCode(), length, limit);
    			return;
    		}
    		if(length == BinaryProtocol.memcache_packetHeaderSize){
    			// @todo handle empty packet
    			return;
    		}
    		/**
    		 * 解析 request header
    		 */
    		readRequestHeader(conn,buffer);
    		//执行命令
    		command = CommandContext.getCommand(conn.getBinaryRequestHeader().getOpcode());    		
    		command.execute(conn);
        }else{  //如果是文本协议
        	
        }
	}
	
	/**
	 * 解析请求头
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public void readRequestHeader(Connection conn,ByteBuffer buffer) throws IOException {
		BinaryRequestHeader header = conn.getBinaryRequestHeader();
		header.setMagic(buffer.get(0));
		header.setOpcode(buffer.get(1));
		header.setKeylen(buffer.getShort(2));
		header.setExtlen(buffer.get(4));
		header.setDatatype(buffer.get(5));
		header.setReserved(buffer.getShort(6));
		header.setBodylen(buffer.getInt(8));
		header.setOpaque(buffer.getInt(12));
		header.setCas(buffer.getLong(16));
	}
	
	/**
	 * 校验报文头是否已经全部读取完成
	 * @param offset
	 * @param position
	 * @return
	 */
	private boolean validateHeader(final long offset, final long position){
		return position >= (offset + BinaryProtocol.memcache_packetHeaderSize);
	}
	
	/**
	 * 获取报文长度 
	 * @param buffer
	 * @return （报文头+报文内容）
	 * @throws IOException
	 */
	private int getPacketLength(ByteBuffer buffer) throws IOException{
		int offset = 8;
		int length = buffer.get(offset) & 0xff << 24;
		length |= (buffer.get(++offset) & 0xff) << 16;
		length |= (buffer.get(++offset) & 0xff) << 8;
		length |= (buffer.get(++offset) & 0xff) ;
		return length + BinaryProtocol.memcache_packetHeaderSize;
	}

}