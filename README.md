# SeckillEvolution

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/dolyw/SeckillEvolution/pulls)
[![GitHub stars](https://img.shields.io/github/stars/dolyw/SeckillEvolution.svg?style=social&label=Stars)](https://github.com/dolyw/SeckillEvolution)
[![GitHub forks](https://img.shields.io/github/forks/dolyw/SeckillEvolution.svg?style=social&label=Fork)](https://github.com/dolyw/SeckillEvolution)

> 一个简单的秒杀架构的演变

#### 项目介绍

从零开始搭建一个简单的秒杀后台，以及持续优化性能

* 文章: [https://note.dolyw.com/seckill-evolution/](https://note.dolyw.com/seckill-evolution/)
* Github：[https://github.com/dolyw/SeckillEvolution](https://github.com/dolyw/SeckillEvolution)
* Gitee(码云)：[https://gitee.com/dolyw/SeckillEvolution](https://gitee.com/dolyw/SeckillEvolution)

#### 项目目录

* [0. 整体流程](https://note.dolyw.com/seckill-evolution/00-Preparation.html)
* [1. 传统方式](https://note.dolyw.com/seckill-evolution/01-Tradition-Process.html)
* [2. 使用乐观锁](https://note.dolyw.com/seckill-evolution/02-Optimistic-Lock.html)
* [3. 使用缓存](https://note.dolyw.com/seckill-evolution/03-Optimistic-Lock-Redis.html)
* [4. 使用分布式限流](https://note.dolyw.com/seckill-evolution/04-Distributed-Limit.html)
* [5. 使用队列异步下单](https://note.dolyw.com/seckill-evolution/05-MQ-Async.html)

**其他**

* [JMeter的安装使用](https://note.dolyw.com/command/06-JMeter-Install.html)
* [MySQL那些锁](http://note.dolyw.com/database/01-MySQL-Lock.html)
* [Redis与数据库一致性](https://note.dolyw.com/cache/00-DataBaseConsistency.html)
* [高并发下的限流分析](http://note.dolyw.com/seckill/02-Distributed-Limit.html)

#### 软件架构

1. SpringBoot + Mybatis核心框架
2. PageHelper插件 + 通用Mapper插件
3. Redis(Jedis)缓存框架
4. 消息队列

#### 安装教程

1. 数据库帐号密码默认为root，如有修改，请自行修改配置文件application.yml
2. 解压后执行src\main\resources\sql\MySQL.sql脚本创建数据库和表
3. Redis需要自行安装Redis服务，端口密码默认
4. SpringBoot直接启动即可，测试工具PostMan，JMeter
5. JMeter测试计划文件在src\main\resources\jmx下

#### 搭建参考

* 感谢杨冠标的流量削峰: [https://www.cnblogs.com/yanggb/p/11117400.html](https://www.cnblogs.com/yanggb/p/11117400.html)
* 感谢mikechen优知的高并发架构系列：什么是流量削峰？如何解决秒杀业务的削峰场景: [https://www.jianshu.com/p/6746140bbb76](https://www.jianshu.com/p/6746140bbb76)
* 感谢crossoverjie的SSM(十八) 秒杀架构实践: [https://crossoverjie.top/2018/05/07/ssm/SSM18-seconds-kill/](https://crossoverjie.top/2018/05/07/ssm/SSM18-seconds-kill/)
* 感谢crossoverjie的设计一个秒杀系统思路以及限流: [https://github.com/crossoverJie/JCSprout/blob/master/MD/Spike.md](https://github.com/crossoverJie/JCSprout/blob/master/MD/Spike.md)
* 感谢qiurunze123的秒杀系统设计与实现: [https://github.com/qiurunze123/miaosha](https://github.com/qiurunze123/miaosha)

#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request