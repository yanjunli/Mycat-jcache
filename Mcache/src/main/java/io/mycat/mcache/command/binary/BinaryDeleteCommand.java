package io.mycat.mcache.command.binary;

import io.mycat.mcache.TCPNIOAcceptor;
import io.mycat.mcache.command.Command;
import io.mycat.mcache.conn.Connection;
import mycat.leaderus.lzy.cachesys.memcache_v5.ReadWritePool;
import mycat.leaderus.lzy.cachesys.memcache_v5.ReturnStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

import java.io.IOException;

/**
 * Created by qd on 2016/12/2.
 * @author  yanglinlin
 */
public class BinaryDeleteCommand implements  Command {
    private static final Logger logger = LoggerFactory.getLogger(BinaryDeleteCommand.class);

    @Override
    public void execute(Connection conn) throws IOException {
        logger.info("delete command");
        ByteBuffer key = readkey(conn);
        if(key==null || key.capacity()==0){
            writeResponse(conn, ProtocolCommand.PROTOCOL_BINARY_CMD_DELETE.getByte(), ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_KEY_ENOENT.getStatus(),0l);
        }
        String keys = new String(cs.decode(key).array());
        int result = ReadWritePool.delete(keys);
        logger.info("delete command result : "+result);
        System.out.println("delete command result :"+result);
        writeResponse(conn,ProtocolCommand.PROTOCOL_BINARY_CMD_DELETE.getByte(),ProtocolResponseStatus.PROTOCOL_BINARY_RESPONSE_SUCCESS.getStatus(),1l);
    }
}
