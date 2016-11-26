package io.mycat.mcache.conn;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.conn.handler.AsciiIOHandler;
import io.mycat.mcache.conn.handler.BinaryIOHandler;
import io.mycat.mcache.conn.handler.IOHandler;

/**
 * 
 * @author liyanjun
 *
 */
public abstract class Connection implements Closeable,Runnable{
	
    public static Logger logger = LoggerFactory.getLogger(Connection.class);
    
    private SelectionKey selectionKey;
	protected final SocketChannel channel;
	private ConDataBuffer writeBuffer;
	protected ConDataBuffer readBuffer;
	private long id;
	private boolean isClosed;
//	private volatile LinkedList<ByteBuffer> writeQueue = new LinkedList<ByteBuffer>();
//	private AtomicBoolean writingFlag = new AtomicBoolean(false);
//	private final Charset charset = Charset.forName(McacheGlobalConfig.defaultCahrset);
    
    private IOHandler ioHandler;
    
    public Connection(SocketChannel channel){
        this.channel = channel;
    }
    
    public long getId(){
    	return this.id;
    }
    
    public void setId(long id){
    	this.id = id;
    }
    
	@Override
	public void close() throws IOException {
		closeSocket();
	}
	
	public void register(Selector selector)  throws IOException {
		selectionKey = channel.register(selector, SelectionKey.OP_READ);
        String maprFileName=id+".rtmp";
        String mapwFileName=id+".wtmp";
        logger.info("connection bytebuffer mapped "+maprFileName);
        readBuffer =new MappedFileConDataBuffer(maprFileName); // 这里还可以使用unsafe 大法...
        writeBuffer=new MappedFileConDataBuffer(mapwFileName);
		setIOHandler();
		// 绑定会话
		selectionKey.attach(this);  //会在 reactor 中被调用
        this.ioHandler.onConnected(this);
	}
	
	/**
	 * 根据协议 选择相应的io处理器
	 * @throws IOException
	 */
	private void setIOHandler()throws IOException  {
		switch (McacheGlobalConfig.prot) {
		case ascii:
			ioHandler = new AsciiIOHandler();
			break;
		case binary:
			ioHandler = new BinaryIOHandler();
			break;
		default:
			ioHandler = new BinaryIOHandler();
			break;
		}
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
		}
	}
	
	/**
	 * 异步读取,该方法在 reactor 中 被调用
	 * @throws IOException
	 */
	public void asynRead() throws IOException{
		final ConDataBuffer buffer = readBuffer;
		final int got =  buffer.transferFrom(channel);
		switch (got) {
	        case 0: {
	            // 如果空间不够了，继续分配空间读取
	            if (readBuffer.isFull()) {
	                // @todo extends
	            }
	            break;
	        }
	        case -1: {
	        	close("client closed");
	            break;
	        }
	        default: {
	        	// 处理指令
				ioHandler.doReadHandler(this);
	        }
	 	}
	}
	
	/**
	 * 异步写
	 * @throws IOException
	 */
	public void asynWrite() throws IOException{
		//TODO 
		ioHandler.doWriterHandler(this);
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
        	readBuffer.recycle();
        	readBuffer=null;
        }
        if(this.writeBuffer!=null)
        {
        	writeBuffer.recycle();
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

	public ConDataBuffer getReadDataBuffer() {
		return this.readBuffer;
	}
}
