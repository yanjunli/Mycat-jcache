# Mycat-jcache
memcached java 高仿版

1、协议处理部分 <br>
   报文结构，处理逻辑 哪几个指令或者协议哪些部分为一个任务 <br>
   1.1 UDP 协议.<br> 
   1.2 TCP 协议.<br> 
2、网络模型 <br>  
![image](https://github.com/mycat-j/Mycat-jcache/blob/master/images/network/%E7%BD%91%E7%BB%9C%E6%A8%A1%E5%9E%8B.png)<br> 

3、内存管理模型<br>

内存模块 与 nio 对接  在 ReadWritePool 里  现在 实现 put 方法 对应 set命令  和 get方法 对应 get命令<br>
内存高仿memcached 内存管理机制<br>
slabclass------------->slab----------------------------->  chunks<br>
(堆外内存最大2G)       相当于page（1M，可自定义）---------->  现在为256B的整数倍<br>
memcached里实现的是 chunks 的复用 对应已经分配了 chunks的 slab 无法重新分配<br>
现在用一线程监控 可实现 对 slab 重新利用  其内部chunks 大小可以重新分配。<br>
![image](https://github.com/mycat-j/Mycat-jcache/blob/master/Memory/%E5%86%85%E5%AD%98.png)<br> 
