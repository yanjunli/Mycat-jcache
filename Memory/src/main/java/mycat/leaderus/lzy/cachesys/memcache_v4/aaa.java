//package io.mycat.net2;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.ByteBuffer;
//import java.nio.MappedByteBuffer;
//import java.nio.channels.FileChannel;
//import java.nio.channels.SocketChannel;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * NIO的Buffer部分目前没有rewind，Connecction的Read与Write操作都始终在前移为止，没有回转，
// * 最好的做法是发现已经使用了1/2的容量（或2/3）后，以及不够写数据的情况下，rewind回去
// *
// * ----------------------------------------------------------------------------------------------
// *
// * redis client ------ ( r/w buffer ) ------>  front connection
// *
// * back connection  ------ ( r/w buffer )  ------> redis server
// *
// *
// * 1、read socket data, adjust writePos
// * 2、write byte buffer, adjust writePos
// * 3、write data to socket, adjust readPos
// *
// *
// * @author zhuam
// *
// */
//public class MappedFileConDataBuffer4 implements ConDataBuffer {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger( MappedFileConDataBuffer4.class );
//
//    private String fileName;
//    private RandomAccessFile randomFile;
//    private FileChannel channel;
//
//    private MappedByteBuffer mappedByteBuffer;
//
//    private int readPos;
//    private int writePos;
//    private int totalSize = 1024 * 1024 * 5;		//1024 * 1024 * 5;
//    private int marked = Math.round(totalSize * 0.6F);
//
//    public MappedFileConDataBuffer4(String fileName) throws IOException {
//        this.fileName = fileName;
//        this.randomFile = new RandomAccessFile(this.fileName, "rw");
//        this.randomFile.setLength(totalSize);
//        this.channel = randomFile.getChannel();
//        this.mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, totalSize);	//映射到内存中
//    }
//
//    /**
//     * 从 socketChannel 读取数据
//     */
//    @Override
//    public int transferFrom(final SocketChannel socketChannel) throws IOException {
//
//        if ( isOutOfBounds() )
//            rewind();
//
//        final int position = mappedByteBuffer.position();
//        final int count = totalSize - position;
//        final int tranfered = (int) channel.transferFrom(socketChannel, position, count);
//
//        // 写入位置
//        writePos = position + tranfered;
//        mappedByteBuffer.position(01 );
//
//        LOGGER.debug("#{} read date from socket, tranfered = {}, readPos = {}, writePos = {}",
//                fileName, tranfered, readPos, writePos);
//
//        // fixbug: transferFrom() always return 0 when client closed abnormally!
//        // --------------------------------------------------------------------
//        // So decide whether the connection closed or not by read()!
//        // @author little-pan
//        // @since 2016-09-29
//        if (tranfered == 0 && count > 0) {
//            return socketChannel.read( mappedByteBuffer );
//        }
//        return tranfered;
//    }
//
//
//    /**
//     * 写入缓冲区
//     */
//    @Override
//    public void putBytes(ByteBuffer buf) throws IOException {
//
//        if ( isOutOfBounds() )
//            rewind();
//
//        int position = mappedByteBuffer.position();
//        int writed = channel.write(buf, position);
//        if ( buf.hasRemaining() ) {
//            throw new IOException("can't write whole buffer ,writed " + writed + " remains " + buf.remaining());
//        }
//
//        // 写入位置
//        writePos = position + writed;
//        mappedByteBuffer.position( writePos );
//    }
//
//    @Override
//    public void putBytes(byte[] buf) throws IOException {
//        //this.mappedByteBuffer.put(buf);
//        putBytes( ByteBuffer.wrap(buf) );
//    }
//
//    /**
//     * 往 socketChannel 写入数据
//     */
//    @Override
//    public int transferTo(SocketChannel socketChanel) throws IOException {
//
//        if ( isOutOfBounds() )
//            rewind();
//
//        int writeEnd = mappedByteBuffer.position();
//        int writed = (int) channel.transferTo(readPos, writeEnd - readPos, socketChanel);
//        this.readPos += writed;
//
//        LOGGER.debug("#{} write date to socket, writed = {}, readPos = {}, writePos = {}",
//                fileName, writed, readPos, writePos);
//
//        return writed;
//    }
//
//    @Override
//    public int writingPos() {
//        return writePos;
//    }
//
//    @Override
//    public int readPos() {
//        return readPos;
//    }
//
//    @Override
//    public int totalSize() {
//        return totalSize;
//    }
//
//    @Override
//    public void setWritingPos(int writingPos) {
//        this.writePos = writingPos;
//    }
//
//    @Override
//    public void setReadingPos(int readingPos) {
//        this.readPos = readingPos;
//    }
//
//    @Override
//    public boolean isFull() {
//        return writePos == totalSize;
//    }
//
//    @Override
//    public void recycle() {
//        try {
//            MappedByteBufferUtil.clean(mappedByteBuffer);
//            randomFile.close();
//            channel.close();
//
//            // 删除文件
//            File file = new File( fileName );
//            file.delete();
//
//        } catch (IOException e) {
//            LOGGER.error(fileName + " buffer recycle err", e);
//        }
//    }
//
//    @Override
//    public byte getByte(int index) {
//        return mappedByteBuffer.get(index);
//    }
//
//    @Override
//    public ByteBuffer getBytes(int index, int length) {
//        int oldPos = mappedByteBuffer.position();
//        mappedByteBuffer.position(index);
//
//        ByteBuffer copyBuf = mappedByteBuffer.slice();
//        copyBuf.limit(length);
//
//        mappedByteBuffer.position(oldPos);
//        return copyBuf;
//    }
//
//    private boolean isOutOfBounds() {
//        if ( readPos > marked  || writePos > marked ) {
//            LOGGER.debug("#{} buffer is out of bounds, readPos = {}, writePos = {}, totalSize = {}, marked = {}  ",
//                    fileName, readPos, writePos, totalSize, marked);
//            return true;
//        }
//        return false;
//    }
//
//    //
//    public void rewind() {
//        int length = writePos - readPos;
//        ByteBuffer oldBuf = getBytes(readPos, length);
//        mappedByteBuffer.position(0);
//        mappedByteBuffer.put( oldBuf );
//
//        this.setReadingPos( 0 );
//        this.setWritingPos( length );
//    }
//
//    @Override
//    public ByteBuffer beginWrite(int length) {
//        ByteBuffer copyBuf = ByteBuffer.allocate(length);
//        return copyBuf;
//    }
//
//    @Override
//    public void endWrite(ByteBuffer buffer) throws IOException {
//        putBytes( buffer );
//    }
//}