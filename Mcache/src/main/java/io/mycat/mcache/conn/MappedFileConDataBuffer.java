package io.mycat.mcache.conn;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参考 mycat-core 代码
 * 实现 zeroCopy 借助 MappedByteBuffer 实现数据快速读取
 * 
 * @author liyanjun
 *
 */
public class MappedFileConDataBuffer implements ConDataBuffer {
	
    public static Logger logger = LoggerFactory.getLogger(MappedFileConDataBuffer.class);

	private FileChannel channel;
	private MappedByteBuffer mapBuf;
	private RandomAccessFile randomFile;
	private int readPos;
	private int totalSize;
	
	public MappedFileConDataBuffer(String fileName) throws IOException
	{
		randomFile = new RandomAccessFile(fileName, "rw");
		totalSize=1024*1024*5;
		randomFile.setLength(totalSize);
		channel = randomFile.getChannel();
		mapBuf=channel.map(FileChannel.MapMode.READ_WRITE, 0, totalSize);
		randomFile.close();
	}
	
	@Override
	public int transferFrom(SocketChannel socketChanel) throws IOException {
		final int position = mapBuf.position(); //缓冲区当前起始位置
		final int count    = totalSize - position;  //剩余可读字节
		final int tranfered = (int) channel.transferFrom(socketChanel, position, count);
		if(tranfered==0 && count > 0){  //transferFrom() always return 0 when client closed abnormally!
			return socketChanel.read(mapBuf);
		}
		return tranfered;
	}

	@Override
	public int transferTo(SocketChannel socketChanel) throws IOException {
	    int writeEnd=mapBuf.position();
		int writed=(int) channel.transferTo(readPos, writeEnd-readPos, socketChanel);
		this.readPos+=writed;
		return writed;
	}

	@Override
	public boolean isFull() {
		return mapBuf.position()==totalSize;
	}

	@Override
	public void recycle() {
		
		try {
			channel.close();
		} catch (IOException e) {
			 
		}
	}

	@Override
	public int readPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int writingPos() {
		// TODO Auto-generated method stub
		return 0;
	}

}
