package io.mycat.mcache.command.binary;

/**
 * Created by qd on 2016/11/29.
 */
public enum ProtocolResponseStatus {
    PROTOCOL_BINARY_RESPONSE_SUCCESS((short)0),
    PROTOCOL_BINARY_RESPONSE_KEY_ENOENT((short)1),
    PROTOCOL_BINARY_RESPONSE_KEY_EEXISTS((short)2),
    PROTOCOL_BINARY_RESPONSE_E2BIG((short)3),
    PROTOCOL_BINARY_RESPONSE_EINVAL((short)4),
    PROTOCOL_BINARY_RESPONSE_NOT_STORED((short)5),
    PROTOCOL_BINARY_RESPONSE_DELTA_BADVAL((short)6),
    PROTOCOL_BINARY_RESPONSE_AUTH_ERROR((short)20),
    PROTOCOL_BINARY_RESPONSE_AUTH_CONTINUE((short)21),
    PROTOCOL_BINARY_RESPONSE_UNKNOWN_COMMAND((short)81),
    PROTOCOL_BINARY_RESPONSE_ENOMEM((short)82);

    private short status;

    public short getStatus(){
        return status;
    }



     ProtocolResponseStatus(short status){
        this.status = status;
    }

	public short getStatus(){
    	return this.status;
    }
}
