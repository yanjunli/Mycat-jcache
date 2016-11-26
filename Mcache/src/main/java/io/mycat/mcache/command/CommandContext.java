package io.mycat.mcache.command;

import java.util.HashMap;
import java.util.Map;

import io.mycat.mcache.conn.handler.IOHandler;


/**
 * 命令上下文
 * @author lyj
 *
 */
public class CommandContext {
	
	private Map<CommandType,Command> commandMap = new HashMap<>();
	
	public CommandContext(IOHandler ioHandler){
//		commandMap.put(CommandType.SEARCHINFILES, new SearchInFilesCommand(ioHandler));
//		commandMap.put(CommandType.CLOSE, new CloseCommand(ioHandler));
	}
	
	public Map<CommandType,Command> getCommandMap(){
		return commandMap;
	}
	
}
