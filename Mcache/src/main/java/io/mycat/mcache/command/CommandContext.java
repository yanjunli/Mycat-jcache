package io.mycat.mcache.command;

import java.util.HashMap;
import java.util.Map;

import io.mycat.mcache.conn.handler.BinaryProtocol;


/**
 * 命令上下文
 * @author lyj
 *
 */
public class CommandContext {	
	
	private static Map<CommandType,Command> commandMap = new HashMap<>();
	
	static {
		commandMap.put(CommandType.set, new BinarySetCommand());
		commandMap.put(CommandType.get, new BinaryGetCommand());
		commandMap.put(CommandType.getk, new BinaryGetKCommand());
		commandMap.put(CommandType.getkq, new BinaryGetKQCommand());
		commandMap.put(CommandType.getq, new BinaryGetQCommand());
		commandMap.put(CommandType.noop, new BinaryNoopCommand());
	}
	
	private CommandContext(){}
	
	public static Command getCommand(Byte key){
		CommandType type = CommandType.getType(key);
		if(type==null){
			return null;
		}
		return commandMap.get(type);
	}
	
	public static Command getCommand(String key){
		CommandType type = CommandType.valueOf(key);
		if(type==null){
			return null;
		}
		return commandMap.get(type);
	}
}
