package io.mycat.mcache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.conn.Connection;
import io.mycat.mcache.conn.MemConnection;

/**
 * @author liyajun
 */
public final class NIOAcceptor extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(NIOAcceptor.class);
	private final Selector selector;
	private final ServerSocketChannel serverChannel;
	private final NIOReactorPool reactorPool;

	public NIOAcceptor(String bindIp,int port,NIOReactorPool reactorPool,int backlog)
			throws IOException {
		super.setName("nioacceptor");
		this.selector = Selector.open();
		this.serverChannel = ServerSocketChannel.open();
		this.serverChannel.configureBlocking(false);
		/** 设置TCP属性 */
		serverChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		serverChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1024 * 16 * 2);
		serverChannel.bind(new InetSocketAddress(bindIp, port), backlog);
		this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		this.reactorPool = reactorPool;
	}

	@Override
	public void run() {
		final Selector selector = this.selector;
		for (;;) {
			try {
				selector.select(500L);
				Set<SelectionKey> keys = selector.selectedKeys();
				try {
					for (SelectionKey key : keys) {
						if (key.isValid() && key.isAcceptable()) {
							accept();
						} else {
							key.cancel();
						}
					}
				} finally {
					keys.clear();
				}
			} catch (Throwable e) {
				LOGGER.warn(getName(), e);
			}
		}
	}

	/**
	 * 接受新连接
	 */
	private void accept() {
		SocketChannel channel = null;
		try {
			channel = serverChannel.accept();
			channel.configureBlocking(false);
			// 派发此连接到某个Reactor处理
			NIOReactor reactor = reactorPool.getNextReactor();
			Connection conn = new MemConnection(channel);
			reactor.registerNewClient(conn);
		} catch (Throwable e) {
			closeChannel(channel);
			LOGGER.warn(getName(), e);
		}
	}

	private static void closeChannel(SocketChannel channel) {
		if (channel == null) {
			return;
		}
		Socket socket = channel.socket();
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		try {
			channel.close();
		} catch (IOException e) {
		}
	}

}