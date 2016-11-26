package io.mycat.mcache.command;


/**
 * 命令类型
 * @author lyj
 *
 */
public enum CommandType {
	
	SEARCHINFILES("1"),  //文件统计命令
	CLOSE("2");          //关闭连接命令
	
	CommandType(String type){
		this.type = type;
	}
	
	private String type;
	
	public static CommandType getType(String type){
		for(CommandType tp:CommandType.values()){
			if(tp.type.equals(type)){return tp;}
		}
		return null;
	}
}
