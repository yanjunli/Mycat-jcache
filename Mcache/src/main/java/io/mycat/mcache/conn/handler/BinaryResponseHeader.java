package io.mycat.mcache.conn.handler;

/**
 * 二进制相应头
 * @author liyanjun
 *
 */
public class BinaryResponseHeader {
	
	byte magic;
    byte opcode;
    short keylen;
    byte extlen;
    byte datatype;
    short status;
    int bodylen;
    int opaque;
    long cas;
}
