package io.mycat.mcache.conn.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.Command;
import io.mycat.mcache.command.CommandContext;
import io.mycat.mcache.command.binary.ProtocolResponseStatus;
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
        	while(offset<limit){
                // 读取到了包头和长度
        		// 是否讀完一個報文
        		if(!validateHeader(offset, limit)) {
        			logger.debug("C#{}B#{} validate protocol packet header: too short, ready to handle next the read event offset{},limit{}",
        				conn.getId(), buffer.hashCode(),offset,limit);
        			return; 
        		}
        		int length = getPacketLength(buffer,offset);
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
        		readRequestHeader(conn,buffer,offset);
        		
        		int keylen = conn.getBinaryRequestHeader().getKeylen();
        		int bodylen = conn.getBinaryRequestHeader().getBodylen();
        		int extlen  = conn.getBinaryRequestHeader().getExtlen();
        	    if (keylen > bodylen || keylen + extlen > bodylen) {
        	        Command.writeResponseError(conn, 
        	        						   conn.getBinaryRequestHeader().getOpcode(),
        	        						   ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_UNKNOWN_COMMAND.getStatus());
        	        return;
        	    }
        	    
//              TODO    	    
//        	    if (settings.sasl && !authenticated(c)) {
//        	        write_bin_error(c, PROTOCOL_BINARY_RESPONSE_AUTH_ERROR, NULL, 0);
//        	        c->write_and_go = conn_closing;
//        	        return;
//        	    }
        	    if(keylen > McacheGlobalConfig.KEY_MAX_LENGTH) {
        	    	Command.writeResponseError(conn, 
    						   conn.getBinaryRequestHeader().getOpcode(),
    						   ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_EINVAL.getStatus());
        			return;
        	    }
        		//执行命令
        		command = CommandContext.getCommand(conn.getBinaryRequestHeader().getOpcode());
        		
        		if(command==null){
        			Command.writeResponseError(conn, 
    						   conn.getBinaryRequestHeader().getOpcode(),
    						   ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_UNKNOWN_COMMAND.getStatus());
        		}
        		command.execute(conn);
        		offset += length;
        		buffer.position(offset);
        	}
        }else{  //如果是文本协议
        	
        }
	}
	
	/**
	 * 解析请求头
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public void readRequestHeader(Connection conn,ByteBuffer buffer,int offset) throws IOException {
		BinaryRequestHeader header = conn.getBinaryRequestHeader();
		header.setMagic(buffer.get(0+offset));
		header.setOpcode(buffer.get(1+offset));
		header.setKeylen(buffer.getShort(2+offset));
		header.setExtlen(buffer.get(4+offset));
		header.setDatatype(buffer.get(5+offset));
		header.setReserved(buffer.getShort(6+offset));
		header.setBodylen(buffer.getInt(8+offset));
		header.setOpaque(buffer.getInt(12+offset));
		header.setCas(buffer.getLong(16+offset));
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
	private int getPacketLength(ByteBuffer buffer,int offset) throws IOException{
		offset += 8;
		int length = buffer.get(offset) & 0xff << 24;
		length |= (buffer.get(++offset) & 0xff) << 16;
		length |= (buffer.get(++offset) & 0xff) << 8;
		length |= (buffer.get(++offset) & 0xff) ;
		return length + BinaryProtocol.memcache_packetHeaderSize;
	}

}