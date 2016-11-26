package io.mycat.mcache.conn.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import io.mycat.mcache.McacheGlobalConfig;
import io.mycat.mcache.command.CommandContext;
import io.mycat.mcache.conn.Connection;

public abstract class IOHandler{
	
	private CommandContext context;

	public IOHandler() throws IOException {
		context = new CommandContext(this);
	}
	
	public CommandContext getContext(){
		return context;
	}
	
	public void onClosed(Connection conn,String reason) {}

	public void onConnected(Connection conn)  throws IOException {};

	public abstract void doReadHandler(Connection conn) throws IOException;
	
	public abstract void doWriterHandler(Connection conn) throws IOException;

//	public void run() {
//		try {
//			if (selectionKey.isReadable()) {
////				doHandler();
//			} else if (selectionKey.isWritable()) {
//				doWriteData();
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			selectionKey.cancel();
//			try {
//				socketChannel.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
//
//	private void doWriteData() throws IOException {
//		try {
//			while (!writingFlag.compareAndSet(false, true)) {
//				// wait until release
//			}
//			ByteBuffer theWriteBuf = writeBuffer;
//			writeToChannel(theWriteBuf);
//		} finally {
//			// release
//			writingFlag.lazySet(false);
//
//		}
//	}
//
//	private void writeToChannel(ByteBuffer curBuffer) throws IOException {
//		int writed = socketChannel.write(curBuffer);
//		System.out.println("writed " + writed);
//		if (curBuffer.hasRemaining()) {
//			System.out.println("writed " + writed + " not write finished ,remains " + curBuffer.remaining());
//			selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
//			if (curBuffer != this.writeBuffer) {
//				writeBuffer=curBuffer;
//			}
//		} else {
//			System.out.println(" block write finished ");
//			writeBuffer=null;
//			if (writeQueue.isEmpty()) {
//				System.out.println(" .... write finished  ,no more data ");
//				selectionKey.interestOps((selectionKey.interestOps() & ~SelectionKey.OP_WRITE)|SelectionKey.OP_READ);
//			} else {
//				ByteBuffer buf = writeQueue.removeFirst();
//				buf.flip();
//				writeToChannel(buf);
//
//			}
//		}
//	}

}