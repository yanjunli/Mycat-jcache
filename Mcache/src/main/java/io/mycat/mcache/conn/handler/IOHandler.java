package io.mycat.mcache.conn.handler;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.Command;
import io.mycat.mcache.command.CommandContext;
import io.mycat.mcache.conn.ConDataBuffer;
import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.model.Protocol;

public class IOHandler{
	
    public static Logger logger = LoggerFactory.getLogger(Connection.class);
    
    public final static int memcache_packetHeaderSize = 24;  //二进制协议头 长度固定为 24字节

	public IOHandler() throws IOException {}
	
	public void onClosed(Connection conn,String reason) {}

	public void onConnected(Connection conn) throws IOException {
		logger.debug("onConnected(): {}", conn);
	}

	public void doReadHandler(Connection conn) throws IOException{
		logger.debug("handleReadEvent(): {}", conn);
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
    		if(length == memcache_packetHeaderSize){
    			// @todo handle empty packet
    			return;
    		}
    		
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
    		// 解析报文类型
    		int keystart  = memcache_packetHeaderSize+ header.getExtlen();
    		int valuestart = keystart + header.getKeylen();
    		int totalBodylength = header.getBodylen();
    		int valuelength = totalBodylength - header.getExtlen() - header.getKeylen();
    		System.out.println("keystart :"+keystart+" keylegth: "+header.getKeylen());
    		System.out.println("valuestart"+valuestart+":valuelength:"+valuelength);
    		//获取key 和value 
//    		byte[] key = new byte[limit];
//    		ByteBuffer  keybuffer = buffer.get(key,keystart, header.getKeylen());
//    		byte[] value = new byte[limit];
//    		buffer.get(value,valuestart, valuelength);
    		ByteBuffer key = getBytes(buffer,keystart, header.getKeylen());
    		ByteBuffer value = getBytes(buffer,valuestart, valuelength);
    		System.out.println("valuestart"+valuestart+":valuelength:"+valuelength);
    		//执行命令
    		command = CommandContext.getCommand(header.getOpcode());    		
    		command.execute(conn,key,value);
        }else{  //如果是文本协议
        	
        }


	}
	
	public ByteBuffer getBytes(ByteBuffer mapBuf,int index,int length) throws IOException {
		int oldPos=mapBuf.position();
		mapBuf.position(index);
		ByteBuffer copyBuf=mapBuf.slice();
		copyBuf.limit(length);
		mapBuf.position(oldPos);
		return copyBuf;
		
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
	private int getPacketLength(ByteBuffer buffer) throws IOException{
		int offset = 8;
		int length = buffer.get(offset) & 0xff << 24;
		length |= (buffer.get(++offset) & 0xff) << 16;
		length |= (buffer.get(++offset) & 0xff) << 8;
		length |= (buffer.get(++offset) & 0xff) ;
		return length + memcache_packetHeaderSize;
	}

}