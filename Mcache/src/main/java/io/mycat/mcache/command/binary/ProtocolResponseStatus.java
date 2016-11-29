package io.mycat.mcache.command.binary;

/**
 * Created by qd on 2016/11/29.
 */
public enum ProtocolResponseStatus {
    PROTOCOL_BINARY_RESPONSE_SUCCESS((byte)0),
    PROTOCOL_BINARY_RESPONSE_KEY_ENOENT((byte)1),
    PROTOCOL_BINARY_RESPONSE_KEY_EEXISTS((byte)2),
    PROTOCOL_BINARY_RESPONSE_E2BIG((byte)3),
    PROTOCOL_BINARY_RESPONSE_EINVAL((byte)4),
    PROTOCOL_BINARY_RESPONSE_NOT_STORED((byte)5),
    PROTOCOL_BINARY_RESPONSE_DELTA_BADVAL((byte)6),
    PROTOCOL_BINARY_RESPONSE_AUTH_ERROR((byte)20),
    PROTOCOL_BINARY_RESPONSE_AUTH_CONTINUE((byte)21),
    PROTOCOL_BINARY_RESPONSE_UNKNOWN_COMMAND((byte)81),
    PROTOCOL_BINARY_RESPONSE_ENOMEM((byte)82);

    private Byte status;

     ProtocolResponseStatus(Byte status){
        this.status = status;
    }
}
