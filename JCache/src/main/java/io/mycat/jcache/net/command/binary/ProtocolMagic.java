package io.mycat.jcache.net.command.binary;

/**
 * Created by qd on 2016/11/29.
 */
public enum ProtocolMagic {
    PROTOCOL_BINARY_REQ((byte)80),
    PROTOCOL_BINARY_RES((byte)81);

    ProtocolMagic(byte type) {
        this.type = type;
    }

    private byte type;
}
