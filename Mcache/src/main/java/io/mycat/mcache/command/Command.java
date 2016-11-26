package io.mycat.mcache.command;

import java.io.IOException;

/**
 * 命令接口
 * @author lyj
 *
 */
public interface Command {

	/**
	 * 执行命令
	 */
	void execute(String readedLine) throws IOException;
}
