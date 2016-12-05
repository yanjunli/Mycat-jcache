package io.mycat.mcache.command;

import java.util.HashMap;
import java.util.Map;

import io.mycat.mcache.command.binary.BinaryAddCommand;
import io.mycat.mcache.command.binary.BinaryDecrCommand;
import io.mycat.mcache.command.binary.BinaryDecrQCommand;
import io.mycat.mcache.command.binary.BinaryDeleteCommand;
import io.mycat.mcache.command.binary.BinaryFlushCommand;
import io.mycat.mcache.command.binary.BinaryGatCommand;
import io.mycat.mcache.command.binary.BinaryGatKCommand;
import io.mycat.mcache.command.binary.BinaryGatKQCommand;
import io.mycat.mcache.command.binary.BinaryGatQCommand;
import io.mycat.mcache.command.binary.BinaryGetCommand;
import io.mycat.mcache.command.binary.BinaryGetKCommand;
import io.mycat.mcache.command.binary.BinaryGetKQCommand;
import io.mycat.mcache.command.binary.BinaryGetQCommand;
import io.mycat.mcache.command.binary.BinaryIncrCommand;
import io.mycat.mcache.command.binary.BinaryIncrQCommand;
import io.mycat.mcache.command.binary.BinaryNoopCommand;
import io.mycat.mcache.command.binary.BinaryQuitCommand;
import io.mycat.mcache.command.binary.BinaryReplaceCommand;
import io.mycat.mcache.command.binary.BinarySetCommand;
import io.mycat.mcache.command.binary.BinaryTouchCommand;
import io.mycat.mcache.command.binary.BinaryVersionCommand;
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
		commandMap.put(CommandType.quit, new BinaryQuitCommand());
		commandMap.put(CommandType.quitq, new BinaryQuitCommand());
		commandMap.put(CommandType.touch, new BinaryTouchCommand());
		commandMap.put(CommandType.add, new BinaryAddCommand());
		commandMap.put(CommandType.flush, new BinaryFlushCommand());
		commandMap.put(CommandType.flushq, new BinaryFlushCommand());
		commandMap.put(CommandType.gat, new BinaryGatCommand());
		commandMap.put(CommandType.gatk, new BinaryGatKCommand());
		commandMap.put(CommandType.gatkq, new BinaryGatKQCommand());
		commandMap.put(CommandType.gatq, new BinaryGatQCommand());
		commandMap.put(CommandType.version, new BinaryVersionCommand());
		commandMap.put(CommandType.stat, new BinaryVersionCommand());
		commandMap.put(CommandType.increment, new BinaryIncrCommand());
		commandMap.put(CommandType.decrement, new BinaryDecrCommand());
		commandMap.put(CommandType.incrementq, new BinaryIncrQCommand());
		commandMap.put(CommandType.decrementq, new BinaryDecrQCommand());
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
