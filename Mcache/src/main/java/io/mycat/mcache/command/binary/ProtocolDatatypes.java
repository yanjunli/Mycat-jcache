package io.mycat.mcache.command.binary;

/**
 * Created by qd on 2016/11/29.
 */
/**
 * Definition of the data types in the packet
 * See section 3.4 Data Types
 */
public enum ProtocolDatatypes {
    PROTOCOL_BINARY_RAW_BYTES((byte)0);

    ProtocolDatatypes(byte type){
        this.type =  type;
    }

    private byte type;
}
