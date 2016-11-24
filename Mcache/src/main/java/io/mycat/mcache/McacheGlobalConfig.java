package io.mycat.mcache;

/**
 * Mcache 全局配置
 * @author 
 *
 */
public final class McacheGlobalConfig {
	
	/**
	 * 默认端口
	 */
	public static int defaultPort = 9000;
	
	/**
	 * 默认reactor pool 大小
	 */
	public static int defaulePoolSize = Runtime.getRuntime().availableProcessors();
	
	/**
	 * 默认 reactor pool 绑定地址
	 */
	public static final String defaultPoolBindIp = "0.0.0.0";
	
	/**
	 * 默认 reactor 选择策略
	 */
	public static final String defaultReactorSelectStrategy = "ROUND_ROBIN";
	
	/**
	 * 
	 */
	public static final int defaultMaxAcceptNum = 10000;

}
