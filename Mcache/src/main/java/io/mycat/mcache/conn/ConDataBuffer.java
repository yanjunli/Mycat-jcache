package io.mycat.mcache.conn;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * 参考 mycat-core 代码
 * 实现 zeroCopy 借助 MappedByteBuffer 实现数据快速读取
 * @author liyanjun
 *
 */
public interface ConDataBuffer {
	
	/**
	 * read data from socketChnnell 
	 * @param socketChanel
	 * @return readed bytes
	 */
	public int transferFrom(SocketChannel socketChanel ) throws IOException;
	
	/**
	 * transfert inner datas to this socket
	 * @param socketChanel
	 * @return transferd data
	 */
	public int transferTo(SocketChannel socketChanel) throws IOException;

	/**
	 * 判断是否已经用完
	 * @return
	 */
	public boolean isFull();

	/**
	 * 回收buffer
	 */
	public void recycle();

	public int readPos();

	public int writingPos();

}
