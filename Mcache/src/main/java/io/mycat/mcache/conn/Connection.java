package io.mycat.mcache.conn;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.conn.handler.BinaryRequestHeader;
import io.mycat.mcache.conn.handler.BinaryResponseHeader;
import io.mycat.mcache.conn.handler.IOHandler;
import io.mycat.mcache.model.Protocol;

/**
 * 
 * @author liyanjun
 *
 */
public class Connection implements Closeable,Runnable{
	
    public static Logger logger = LoggerFactory.getLogger(Connection.class);
    
    private SelectionKey selectionKey;
	protected final SocketChannel channel;
	private ByteBuffer writeBuffer;  //读缓冲区
	protected ByteBuffer readBuffer; //写缓冲区
	private long id;
	private boolean isClosed;    
    private IOHandler ioHandler;  //io 协议处理类
    private Protocol protocol;  //协议类型
    
    /**
     * 二进制请求头
     */
    private BinaryRequestHeader binaryHeader = new BinaryRequestHeader();  //当前连接的多个请求 使用同一个 header 对象， 减少对象创建
    
    /**
     * 二进制 响应头
     */
    private BinaryResponseHeader binaryResponse = new BinaryResponseHeader();  //当前连接的多个请求 使用同一个 response对象，减少对象创建
    
    
    public Connection(SocketChannel channel){
        this.channel = channel;
    }
    
    public long getId(){
    	return this.id;
    }
    
    public void setId(long id){
    	this.id = id;
    }
    
    public void setProtocol(Protocol protocol){
    	this.protocol = protocol;
    }
    
    public Protocol getProtocol(){
    	return this.protocol;
    }
    
    public void setBinaryRequestHeader(BinaryRequestHeader binaryHeader){
    	this.binaryHeader = binaryHeader;
    }
    
    public BinaryRequestHeader getBinaryRequestHeader(){
    	return this.binaryHeader;
    }
    
    public void setBinaryResponseHeader(BinaryResponseHeader binaryResponse){
    	this.binaryResponse = binaryResponse;
    }
    
    public BinaryResponseHeader getBinaryResponseHeader(){
    	return this.binaryResponse;
    }
    
	@Override
	public void close() throws IOException {
		closeSocket();
	}
		
	public void register(Selector selector)  throws IOException {
		selectionKey = channel.register(selector, SelectionKey.OP_READ);
        readBuffer = ByteBuffer.allocate(1024); // 这里可以修改成从内存模块获取
        writeBuffer=ByteBuffer.allocate(1024);
        ioHandler = new IOHandler();
		// 绑定会话
		selectionKey.attach(this);  //会在 reactor 中被调用
        this.ioHandler.onConnected(this);
	}
	
	@Override
	public void run() {
		try {
			if (selectionKey.isValid()) {
				if (selectionKey.isReadable()) {
					asynRead();
				}
				if (selectionKey.isWritable()) {
					asynWrite();
				}
			} else {
				logger.debug("select-key cancelled");
				selectionKey.cancel();
			}
		} catch (final Throwable e) {
			if (e instanceof CancelledKeyException) {
				if (logger.isDebugEnabled()) {
					logger.debug(this + " socket key canceled");
				}
			} else {
				logger.warn(this + " " + e);
			}
			close("program err:" + e.toString());
			
		}
	}
	
	/**
	 * 异步读取,该方法在 reactor 中 被调用
	 * @throws IOException
	 */
	public void asynRead() throws IOException{
//		final ConDataBuffer buffer = readBuffer;
//		final int got =  buffer.transferFrom(channel);
		final int got = channel.read(readBuffer);
		switch (got) {
	        case 0: {
	            // 如果空间不够了，继续分配空间读取
//	            if (readBuffer.isFull()) {
//	                // @todo extends
//	            }
	            break;
	        }
	        case -1: {
	        	close("client closed");
	            break;
	        }
	        default: {
	        	logger.info(" read bytes {}.",got);
	        	// 处理指令
	        	readBuffer.flip();
				ioHandler.doReadHandler(this);
	        }
	 	}
	}
	
	/**
	 * 异步写
	 * @throws IOException
	 */
	public void asynWrite() throws IOException{
		int writed = channel.write(writeBuffer);
		
		logger.warn("writed data length:{}",writed);
		final int remains = writeBuffer.remaining();
		boolean noMoreData = remains==0;
		logger.warn("nodata :{}",noMoreData);
		if (noMoreData) {
		    if ((selectionKey.isValid() && (selectionKey.interestOps() & SelectionKey.OP_WRITE) != 0)) {
		    	writeBuffer.clear();
		        disableWrite();
		    }

		} else {
		    if ((selectionKey.isValid() && (selectionKey.interestOps() & SelectionKey.OP_WRITE) == 0)) {
		        enableWrite(false);
		    }
		}
	}
	
    public void enableWrite(boolean wakeup) {
        boolean needWakeup = false;
        SelectionKey key = this.selectionKey;
        try {
        	logger.info("enable write ");
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
            needWakeup = true;
        } catch (Exception e) {
            logger.warn("can't enable write " + e);
        }
        if (needWakeup && wakeup) {
        	key.selector().wakeup();
        }
    }
	
	private void disableWrite() {
        try {
            SelectionKey key = this.selectionKey;
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            logger.warn("disable write con " + this);
        } catch (Exception e) {
            logger.warn("can't disable write " + e + " con " + this);
        }
    }
	
    public void close(String reason) {
        if (!isClosed) {  
            closeSocket();
            this.cleanup();
            isClosed = true;
            logger.info("close connection,reason:" + reason + " ," + this.getClass());
            if (ioHandler != null) {
            	ioHandler.onClosed(this, reason);
            }
        }
    }
    
    /**
     * 清理资源
     */

    protected void cleanup() {
    	// 清理资源占用
        if(readBuffer!=null)
        {
//        	readBuffer.recycle();
        	readBuffer=null;
        }
        if(this.writeBuffer!=null)
        {
//        	writeBuffer.recycle();
        	writeBuffer=null;
        }
    }
    
    private void closeSocket() {

        if (channel != null) {
            boolean isSocketClosed = true;
            try {
            	selectionKey.cancel();
                channel.close();
            } catch (Throwable e) {
            }
            boolean closed = isSocketClosed && (!channel.isOpen());
            if (!closed) {
            	logger.warn("close socket of connnection failed " + this);
            }

        }
    }

	public ByteBuffer getReadDataBuffer() {
		return this.readBuffer;
	}
	
	public ByteBuffer getWriteBuffer(){
		return this.writeBuffer;
	}
}
