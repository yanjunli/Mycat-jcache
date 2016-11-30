package io.mycat.mcache.command;

import io.mycat.mcache.conn.handler.BinaryProtocol;

/**
 * 命令类型
 * @author lyj
 *
 */
public enum CommandType {
	
	get(BinaryProtocol.OPCODE_GET),
	set(BinaryProtocol.OPCODE_SET),
	add(BinaryProtocol.OPCODE_ADD),
	replace(BinaryProtocol.OPCODE_REPLACE),
	delete((byte)4),
	increment((byte)5),
	decrement((byte)6),
	quit((byte)7),
	flush((byte)8),
	getq((byte)9),
	noop((byte)10),
	version((byte)11),
	getk((byte)12),
	getkq((byte)13),
	append((byte)14),
	prepend((byte)15),
	stat((byte)16),
	auth_list((byte)32),
	start_auth((byte)33),
	auth_steps((byte)34);	
	
	CommandType(byte type){
		this.type = type;
	}
	
	private Byte type;
	
	public Byte getByte(){
		return this.type;
	}
	
	public static CommandType getType(Byte type){
		for(CommandType tp:CommandType.values()){
			if(tp.type.equals(type)){return tp;}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(1 << 1);
	}
}
