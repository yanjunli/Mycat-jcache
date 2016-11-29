package io.mycat.mcache.conn.handler;

import java.io.IOException;

import io.mycat.mcache.conn.Connection;

public abstract class IOHandler{
	

	public IOHandler() throws IOException {}
	
	public void onClosed(Connection conn,String reason) {}

	public void onConnected(Connection conn)  throws IOException {};

	public abstract void doReadHandler(Connection conn) throws IOException;
	
	/**
	 * 后期优化 事件类型
	 * @param conn
	 * @param eventtype
	 * @throws IOException
	 */
	public abstract void doWriterHandler(Connection conn) throws IOException;

}