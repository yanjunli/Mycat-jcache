package io.mycat.mcache.model;

import java.nio.channels.Pipe;
import java.nio.channels.Selector;

/**
 * 高仿 LIBEVENT_THREAD 模型对象 数据结构 
 * @author liyanjun
 *
 */
@Deprecated
public class ReactorSelectorModel {

	private int thread_id; /* unique ID of this thread */
	
	private Selector selector;  /* libevent handle this thread uses */
	
	Pipe.SinkChannel sink;  /* listen event for notify pipe */
	
	Pipe.SourceChannel source;
}
