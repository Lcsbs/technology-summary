## [阿里，头条，美团，快手大数据开发岗面试总结](https://www.cnblogs.com/xiaodf/p/12841476.html)

从3月份开始，陆续面了阿里，头条，美团，快手四家公司的大数据开发岗位，近20场面试面下来挺耗费脑力的，不过结果还行，除了头条外，目前拿到了其他三家的offer，今天把还能记住的题目做个整理，整理是按技术分类的，因为确实记不太清哪一场问了哪些题了。

先说一下这几场面试的整体感受，头条和快手风格相似，每一面基本都有算法或实现题，算法主要是leetcode easy, middle难度的题，这跟你面试情况有关，可能项目上没有亮点的算法题会出的难一点。笔者刷题不多，也就100多道吧，还好没有遇到太难的算法，不过刷题还是平时没事多刷刷的好，毕竟每次面试才突击刷有点累，刷题也有助于开阔思维。实现题主要是让你实现下HashMap，LRU，生产消费者模型，单例模式等，面试官通过这些可以看出你对数据结构的理解和代码实现能力。阿里和美团的话，算法题不是很多，但会比较注重问项目里的亮点，我理解的亮点包括你开发或优化过比较有价值的功能，解决过复杂或有难度的问题等，这个需要自己根据做过的项目总结好。数仓的话SQL，建模理论问的比较多。

建议刚开始面试不要海投简历，可以先找一家练练手，面试过程中查漏补缺，不断完善知识盲点，这些公司，你只要表现不是太差，一个部门面不过还可以面其他部门的。投简历可以在脉脉，拉钩，boss直聘上找HR，内部员工内推，也可以通过猎头投，不过猎头参差不齐，好的猎头会对公司在招的岗位给出详细的介绍，这个尤其在后面你有多个offer做选择时比较方便，你可以通过猎头了解这些offer的利弊，不过好猎头不好碰。

好了，下面开始罗列这次面试中遇到的面试问题，这些题没有答案，不过会先给出一些我看过的书籍和课程。后续的文章中会对这些知识点进行总结，敬请关注。

### 1 Java基础

1. 说说Java里的多态什么意思
2. Java 里的final关键用过吗
3. 讲讲volatile关键字的作用，与synchronized关键字的不同
4. 了解HashMap的内部结构吗？自己实现一个HashMap
5. HashMap、Hashtable、ConcurrentHashMap的原理与区别
6. 用Java实现一个生产者消费者模型，可以用BlockingQueue阻塞队列
7. 了解哪些设计模式，实现一个单例模式

感言：做大数据开发，java基础是必须的，一般一面，二面都会问到，java基础答的不好，一般都不会给过。

推荐阅读：

[最新整理JAVA面试题附答案](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484372&idx=1&sn=832f3ac6852f7cc97a4e369c7d6ec49d&chksm=eb8790dbdcf019cded6a7c1814187da1139fe2d416455216ebe53c88c50491ec5982cea8b809&scene=21#wechat_redirect)

### 2 数据结构与算法

算法：

1. 搜索旋转排序数组，leetcode 33，中等难度
2. 实现一个LRU缓存，leetcode 146 ，中等难度
3. 用两个栈实现一个队列，leetcode 232 , 简单难度
4. 给定一个非空的整数数组，返回其中出现频率前 k 高的元素。Leetcode 347 , 中等难度
5. 二叉树的最近公共祖先， Leetcode 236 , 中等难度

感言：就记住了这几个，大家也能看出来，基本都是leetcode上的题，刷题还是很有必要的。
刷题时建议先按分类刷，像二分查找，动态规划都有一些固定的模式的。

数据结构：

1. 布隆过滤器
2. Bitmap
3. B+树
4. LSM Tree
5. 跳表
6. Hyperloglog

推荐阅读：
强烈推荐极客时间王争老师的《数据结构与算法之美》课程，目前已有8万多人购买，应该是极客时间购买人数最多的课程，有需要的可以扫下面二维码购买。质量绝对优质，反正我读了受益匪浅。比如讲Redis的有序集合底层数据结构为什么用跳表时，老师会从二叉搜索树，B+树开始讲起，让你同时了解了这三种数据结构的异同和应用场景。

![img](https://user-gold-cdn.xitu.io/2020/5/6/171e98f314720e99?w=213&h=379&f=png&s=109652)

### 3 Hive

做大数据，Hive SQL也是必问的，大厂的ETL任务很多都是Hive SQL，主要问题如下：

1. Hive row_number，rank两个函数的区别
2. Hive窗口函数怎么设置窗口大小
3. Hive order by,sort by,distribute by,cluster by 区别
4. Hive map,reduce数怎么设置
5. Hive SQL数据倾斜有哪些原因？怎么优化
6. parquet数据格式内部结构了解吗
7. Hive数据选择的什么压缩格式
8. Hive SQL 如何转化成MR任务的
9. Hive 分桶了解吗
10. Hive的udf、udaf和udtf了解过吗？自己有没有写过udf
11. 怎么验证Hive SQL 的正确性
12. lateral view explode关键字来拆分数组
13. join操作底层的MapReduce是怎么去执行的

SQL应用题：

1. 一个login_in表，userid、login_time、ip，数据量很大，一个人可能有多条登录数据，取出最近10个登录的用户。
2. 还是login_in表，统计登录的总条数( PV )和登录的总人数 (UV)。
3. 一个用户好友表 userid , follow_list (该用户的好友id数组)
   A [B, C, D]
   B [A, C]
   C [D]
   统计这个表有多少对好友

感言：这个主要考察你平时写SQL多不多，数仓开发岗会比较在意SQL能力

推荐阅读：

[为什么我们选择 Parquet](https://mp.weixin.qq.com/s/r0N8LOTmONAgoqFklznhgg)

### 4 MapReduce&Spark

1. MapReduce的作业流程，涉及到几次排序
2. Spark任务执行过程
3. MapReduce Shuffle 和 Spark Shuffle的区别
4. Spark的内存管理模型
5. 讲讲Spark Shuffle
6. Spark Shuffle bypass模型了解吗
7. Spark使用中遇到什么问题，怎么解决的

感言：MapReduce&Spark是主要的离线计算引擎，需要对任务调度流程和可能出现性能瓶颈的点熟悉，
懂得组件原理和调优，如果工作中遇到并解决过大数据工程性能问题会有加分

推荐阅读：

[MapReduce Shuffle 和 Spark Shuffle 原理概述](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483861&idx=1&sn=da90622bcd18aa5a1b6884a444da94a7&chksm=eb8792dadcf01bccdba0002c60e6aa2e0c4c849edaf0004f174f1854acae87dae06c2afd5470&scene=21#wechat_redirect)

[Spark 内存管理的基本原理](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483849&idx=1&sn=3a53d18d44a0c272e570ddafe1cd904d&chksm=eb8792c6dcf01bd04110e20459718cac96295213802d0dda066c350c4486206865664978714b&scene=21#wechat_redirect)

[Spark性能调优-Shuffle相关参数配置](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483841&idx=1&sn=9db8162f5993b6d3a0b7658d747fedc3&chksm=eb8792cedcf01bd8d33268388c884749dd6e908af1bc279dfd9a9020106130e49e49298f36da&scene=21#wechat_redirect)

### 5 Spark Streaming&Flink

1. Spark Streaming 与Flink的对比
2. Flink怎么做到Exactly Once
3. Spark Streaming能做到Exactly Once语义吗
4. Flink的StateStore有哪些，工作中用过哪些
5. 做过Flink内存调优吗
6. 遇到过OOM的情况吗，怎么处理的
7. 讲讲Spark Streaming 与Flink的反压机制
8. Flink的窗口函数，时间机制，CheckPoint机制，两阶段提交
9. Flink 双流Join
10. Flink State TTL怎么设置
11. Flink 维表关联有哪些方式，数据量大时怎么处理

其他一些应用题：

1. 实时PV，UV统计
2. 实时TOP N 统计
3. 广告曝光流和点击流实时join

感言：不仅要对组件的原理清楚，还要实际做过实时相关的业务开发，面试官也会出一些他们场景的业务让你说下你会怎么设计，所以需要多关注些实时业务场景的应用实现。

推荐阅读：

[Flink DataStream 关联维表实战](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484356&idx=1&sn=41eb2f5dac365c29e6d8239cc3ebb453&chksm=eb8790cbdcf019dd5edc0f71aed94380465c564cf6c922f986db773ca71d5a21e5274ff5e504&scene=21#wechat_redirect)

[基于 Kafka + Flink + Redis 实现电商大屏实时计算](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484348&idx=1&sn=51420612df9e861f2f62a1ac160c6bc9&chksm=eb8790b3dcf019a52c5a844b9a97863f1b68384a9ba30ba9dcc7b805864e59fa1ffaac35dac1&scene=21#wechat_redirect)

[阿里蒋晓伟谈计算引擎Flink和Spark的异同与优势](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484212&idx=1&sn=c97d53f4a4dbac7bcd2a5b4762060cc7&chksm=eb87903bdcf0192de2e9335ef4a3ebef198c1b80fb723ac148432fe1121a7e208e1f79d00a18&scene=21#wechat_redirect)

[Flink基础 | 深入理解Apache Flink核心技术](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483973&idx=1&sn=824257064bc7467cf8974832a28d3eb2&chksm=eb87914adcf0185c35ae148ab8a8c06a43ecc31d42bdd541af865f4c54fd388c92a239e5e07c&scene=21#wechat_redirect)

### 6 数据仓库

1. 你们公司数仓是怎么构建的，怎么做的分层
2. 说说范式建模和维度建模的区别
3. 说说星型模型和雪花模型的区别
4. 设计一个统计各渠道用户留存的模型
5. 缓慢变化维怎么处理
6. 你们数据怎么同步到数仓的，怎么保证数据不丢失
7. 数据质量怎么控制
8. 数据规范怎么定义的
9. 如果进行元数据管理

感言：数仓方法论的东西需要了解，数仓整体的构建理念要对，给一个业务需求，能够给出合理的数仓构建模型。

推荐阅读：

[辨析BI、数据仓库、数据湖和数据中台内涵及差异点(建议收藏)](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484136&idx=1&sn=9d573a3722986d3894521e1039b56b2a&chksm=eb8791e7dcf018f1114e8973fed6c82837fe7fc2dfd3d08e1aeb790d052958caefa5d310cc2a&scene=21#wechat_redirect)

[【漫谈数据仓库】数据仓库的分层理论](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483810&idx=1&sn=cad381bb1d95767f4e54756f38d85d68&chksm=eb8792addcf01bbb9b49cd448946d4491011ffdddf9a3350fb444e8e855da08afb0b23b5d96a&scene=21#wechat_redirect)

### 7 Kafka

1. 说说对kafka的了解
2. Kafka基本原理说一下，和其他的MQ相比的优势
3. 讲讲Kafka的高阶，低阶消费者API的区别
4. Kafka的ack有哪几种
5. Kafka 消费者怎么从Kafka取数据的
6. Kafka生产消费怎么保证Exactly Once
7. Kafka怎么保证有序性的
8. Kafka Controller是做什么用的
9. Kafka 多副本leader如何选举
10. Kafka 消费者组重平衡流程是怎样的
11. 新版和老版Kafka offset的管理有什么不同
12. 如何查看消费者的消费进度

推荐阅读：

[Kafka常见面试知识点总结](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484231&idx=1&sn=143ac54919e67db69d0482ad5ed3fb64&chksm=eb879048dcf0195e3f4c72a2986beb989dbe40ec1ab72329fa24425127cd3b35130128cc9b18&scene=21#wechat_redirect)

[Kafka高可用性实现原理](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483677&idx=1&sn=4f997ccba30f6b0c22f95047c4d21e85&chksm=eb879212dcf01b04c0cfbcf03e24e565c88b766493aa25c300e09a0cfdb194ab8b97774c1d44&scene=21#wechat_redirect)

### 8 HBase

1. HBase rowkey如何设计的
2. 谈谈热点问题以及如何解决
3. 说下HBase的读写流程
4. HBase使用过程中做过哪些优化
5. HBase的Compaction机制作用

推荐阅读：

[HBase: 为高效可扩展的分布式系统而设计](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483740&idx=1&sn=967863b48e1fa2dc3e52b4afc4e5e7a1&chksm=eb879253dcf01b459dbfcfbab9cf54ac6d894238a0de9e7c2c04b52882f24d1bb190bb186abf&scene=21#wechat_redirect)

[数据存储检索之B+树和LSM-Tree](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484181&idx=1&sn=f6bbed1706cf4e2f3c080be209f6a1c4&chksm=eb87901adcf0190c8e5f89beeca914e724e8ca49ed7d18456cf8497afe6e970cc5ceb5a885a0&scene=21#wechat_redirect)

[HBase Compaction流程详解](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483769&idx=1&sn=febb2e9c69f1a1f22a43d1e473957e4d&chksm=eb879276dcf01b6012bc595a1dfdf07b2bf1a82bc4b92b12036c480fff9d706a89962a112819&scene=21#wechat_redirect)

[一张图看懂HBase架构](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484176&idx=1&sn=1d5947a03ca75be32e5a7499aa0d491b&chksm=eb87901fdcf01909be6def4d8148a99e16e3afed552addb57d32fd779f6639be9ea16c260e2e&scene=21#wechat_redirect)

### 9 Redis

1. Redis 包含哪些数据结构
2. Redis 有序集合的底层实现
3. Redis 有哪几种数据持久化方式及各自优缺点
4. 一致性哈希了解吗

推荐阅读：

[为什么我们要使用Redis](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483778&idx=1&sn=a0bd4d9a079387696b804556b5b58625&chksm=eb87928ddcf01b9b59c9bc762fbc838cef932e393c0f3bd5e0083073c4dd2cefca9cf1b581d6&scene=21#wechat_redirect)

[如何用 Redis 统计独立用户访问量？](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247484202&idx=1&sn=198d0af978fb9a7987c31f0a30a3df5b&chksm=eb879025dcf01933855089ac6e45aaabada816ed6d72667f93f35cbd1f38d7631886f5f69f80&scene=21#wechat_redirect)

[Redis架构进化论](http://mp.weixin.qq.com/s?__biz=MzI4MzY5MDU5Mg==&mid=2247483919&idx=1&sn=98bca29d65b8be6ae816bfd30aefca2c&chksm=eb879100dcf018164606453bb7346669054a2c071c3c81b6f9c92bbecacbad99f47edb8740b1&scene=21#wechat_redirect)

### 总结

平时工作中一定要注意总结和积累，查漏补缺，不断完善自己的知识体系。