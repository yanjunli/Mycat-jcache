package io.mycat.mcache.conn;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author liyanjun
 *
 */
public abstract class Connection implements Closeable{
	
    public static Logger LOGGER = LoggerFactory.getLogger(Connection.class);
	
    protected final SocketChannel channel;
    
    private SelectionKey processKey;
    
    public Connection(SocketChannel channel) {
        this.channel = channel;
    }
    
	@Override
	public void close() throws IOException {
		closeSocket();
	}
    
    private void closeSocket() {

        if (channel != null) {
            boolean isSocketClosed = true;
            try {
                processKey.cancel();
                channel.close();
            } catch (Throwable e) {
            }
            boolean closed = isSocketClosed && (!channel.isOpen());
            if (!closed) {
                LOGGER.warn("close socket of connnection failed " + this);
            }

        }
    }

}
