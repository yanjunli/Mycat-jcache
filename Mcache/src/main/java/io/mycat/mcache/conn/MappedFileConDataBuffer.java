package io.mycat.mcache.conn;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
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
	}
	
	@Override
	public int transferFrom(final SocketChannel socketChanel) throws IOException {
		final int position = mapBuf.position();
		final int count    = totalSize - position;
		final int tranfered= (int) channel.transferFrom(socketChanel, position, count);
		mapBuf.position(position + tranfered);
		// fixbug: transferFrom() always return 0 when client closed abnormally!
		// --------------------------------------------------------------------
		// So decide whether the connection closed or not by read()! 
		// @author little-pan
		// @since 2016-09-29
		if(tranfered == 0 && count > 0){
			return (socketChanel.read(mapBuf));
		}
		return tranfered;
	}

	@Override
	public void putBytes(ByteBuffer buf) throws IOException {
		int position=mapBuf.position();
		int writed=channel.write(buf, position);
		if(buf.hasRemaining())
		{
			throw new IOException("can't write whole buf ,writed "+writed+" remains "+buf.remaining());
		}
		mapBuf.position(position+writed);
		
	}
	@Override
	public void putBytes(byte[] buf) throws IOException {
		this.mapBuf.put(buf);
		
	}

	@Override
	public int transferTo(SocketChannel socketChanel) throws IOException {
    int writeEnd=mapBuf.position();
	int writed=(int) channel.transferTo(readPos, writeEnd-readPos, socketChanel);
	this.readPos+=writed;
	//System.out.println("transferTo ,writed  "+writed+" read "+readPos+" pos " + "writepos "+writeEnd);
		return writed;
	}

	@Override
	public int writingPos() {
		return mapBuf.position();
	}

	@Override
	public int readPos() {
		return readPos;
	}

	@Override
	public int totalSize() {
		 		return totalSize;
	}

	@Override
	public void setWritingPos(int writingPos) {
		mapBuf.position(writingPos);
	}

	@Override
	public void setReadingPos(int readingPos) {
		this.readPos=readingPos;
	}
	@Override
	public boolean isFull() {
		return mapBuf.position()==this.totalSize;
	}
	@Override
	public void recycle() {
		try {
			randomFile.close();
			channel.close();
		} catch (IOException e) {
			 
		}
	}
	@Override
	public byte getByte(int index) {
		return mapBuf.get(index);
	}
	@Override
	public ByteBuffer getBytes(int index,int length) throws IOException {
		int oldPos=mapBuf.position();
		mapBuf.position(index);
		ByteBuffer copyBuf=mapBuf.slice();
		copyBuf.limit(length);
		mapBuf.position(oldPos);
		return copyBuf;
		
	}
	@Override
	public ByteBuffer beginWrite(int length) {
		ByteBuffer copyBuf=mapBuf.slice();
		copyBuf.limit(length);
		return copyBuf;
	}
	
	@Override
	public void endWrite(ByteBuffer buffer) {
		 mapBuf.position(mapBuf.position()+buffer.position());
		//System.out.println("end write ,total "+buffer.limit()+" writePos "+mapBuf.position()+" read pos "+this.readPos);
	}

}
