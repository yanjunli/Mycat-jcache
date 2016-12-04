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
	
	private static Map<Object,Command> commandMap = new HashMap<>();
	
	//TODO 注册过程待优化.暂时先这样写
	static {
		commandMap.put(CommandType.set, new BinarySetCommand());
		commandMap.put(CommandType.get, new BinaryGetCommand());
		commandMap.put(CommandType.getk, new BinaryGetKCommand());
		commandMap.put(CommandType.getkq, new BinaryGetKQCommand());
		commandMap.put(CommandType.getq, new BinaryGetQCommand());
		commandMap.put(CommandType.noop, new BinaryNoopCommand());
		commandMap.put(CommandType.delete, new BinaryDeleteCommand());
		commandMap.put(CommandType.replace, new BinaryReplaceCommand());
		commandMap.put(CommandType.quit, new BinaryReplaceCommand());
		commandMap.put(CommandType.touch, new BinaryTouchCommand());
		commandMap.put(CommandType.add, new BinaryAddCommand());
		commandMap.put(CommandType.flush, new BinaryFlushCommand());
		commandMap.put(CommandType.get, new BinaryGatCommand());
		commandMap.put(CommandType.getk, new BinaryGatKCommand());
		commandMap.put(CommandType.getkq, new BinaryGatKQCommand());
		commandMap.put(CommandType.getq, new BinaryGatQCommand());
		commandMap.put(CommandType.version, new BinaryVersionCommand());
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
