7:31 2016/11/30
ReadWritePool里的方法和 nio 命令对应
下一步将 抽象出接口
还没有抽象出接口，预计12月2日前完成，要修一下电脑，硬盘坏了
以后 nio 和 memory 对接 用接口
现在set命令对应 ReadWritePool.put
get命令对应 ReadWritePool.get
memcached 没有提供对于 slab的回收利用  下一步将把这块改进 
Slab 回收完成。