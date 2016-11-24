package io.mycat.mcache;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mcache.conn.Connection;

/**
 * 
 * @author liyanjun
 */
public final class NIOReactor extends Thread{
	private static final Logger LOGGER = LoggerFactory.getLogger(NIOReactor.class);
	
	private final Selector selector;

	private final LinkedTransferQueue<Connection> registerQueue;

	public NIOReactor(String name) throws IOException {
		super.setName(name);
		this.selector = Selector.open();
		this.registerQueue = new LinkedTransferQueue<>();  // 这里不使用 ConcurrentLinkedQueue 的原因在于,可能acceptor 和reactor同时操作队列
	}
	
	/**
	 * 将新的连接请求 放到 reactor 的请求队列中，同时唤醒 reactor selector
	 * @param socketChannel
	 */
	final void registerNewClient(Connection conn) {
		registerQueue.offer(conn);
		selector.wakeup();
	}
	
	@Override
	public void run() {
		final Selector selector = this.selector;
		Set<SelectionKey> keys = null;
		int readys=0;
		for (;;) {
			try {
				readys=selector.select(400/(readys+1));  //借鉴mycat-core 代码
				if(readys==0)  //acceptor 线程唤醒 reactor 线程处理 新请求的事件
				{
					handlerEvents(selector);  // 处理新连接请求事件
					continue;
				}
				keys = selector.selectedKeys();
				for (final SelectionKey key : keys) {
					Connection con = null;
					try {
						final Object att = key.attachment();
						LOGGER.debug("select-key-readyOps = {}, attachment = {}", key.readyOps(), att);
						if (att != null && key.isValid()) {
							con = (Connection) att;
							if (key.isReadable()) {
								//TODO 
							}
							if(key.isValid() == false){
								LOGGER.debug("select-key cancelled");
								continue;
							}
							if (key.isWritable()) {
								//TODO 
							}
						} else {
							key.cancel();
						}
					} catch (final Throwable e) {
						if (e instanceof CancelledKeyException) {
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug(con + " socket key canceled");
							}
						} else {
							LOGGER.warn(con + " " + e);
						}
					}
				}
			} catch (Throwable e) {
				LOGGER.warn(getName(), e);
			} finally {
				if (keys != null) {
					keys.clear();
				}
			}
			handlerEvents(selector);
		}
	}

	/**
	 * 类似于pipeline 的设计 链式处理，设计成责任链模式？？？
	 */
	private void processEvents() {
// TODO 
//		if(events.isEmpty())
//		{
//			return;
//		}
//		Object[] objs=events.toArray();
//		if(objs.length>0)
//		{
//		for(Object obj:objs)
//		{
//			((Runnable)obj).run();
//		}
//		events.removeAll(Arrays.asList(objs));
//		}		
	}

	private void handlerEvents(Selector selector)
	{ //TODO 相应事件
		try
		{
		processEvents();     //TODO  处理事件   需要实现状态机了。
		register(selector);  //TODO 注册 selector  读写事件
		}catch(Exception e)
		{
			LOGGER.warn("caught user event err:",e);
		}
	}
	
	/**
	 * 注册 io 读写事件
	 * @param selector
	 */
	private void register(Selector selector) {
//TODO 
//		if (registerQueue.isEmpty()) {
//			return;
//		}
//		Connection c = null;
//		while ((c = registerQueue.poll()) != null) {
//			try {
//				c.register(selector, myBufferPool);
//			} catch (Throwable e) {
//				LOGGER.warn("register error ", e);
//				c.close("register err");
//			}
//		}
	}
}