**1.MapTask并行机度是由什么决定的？**

由切片数量决定的。

**2.MR是干什么的？**

MR将用户编写的业务逻辑代码和自带的默认组件结合起来组成一个完整的分布式应用程序放到hadoop集群上运行。

**3.combiner和partition的作用：**

combiner的意义就是对每一个maptask的输出进行局部汇总，以减小网络传输量 partition的默认实现是hashpartition，是map端将数据按照reduce个数取余，进行分区，不同的reduce来copy自己的数据。 partition的作用是将数据分到不同的reduce进行计算，加快计算效果。

**4.什么是shuffle**

map阶段处理的数据如何传递给reduce阶段，是mapreduce框架中最关键的一个流程，这个流程就叫shuffle； shuffle: 洗牌、发牌——（核心机制：数据分区，排序，缓存，分组）。

具体来说：就是将maptask输出的处理结果数据，分发给reducetask，并在分发的过程中，对数据按key进行了分区和排序，分组。

**5.列举几个hadoop 生态圈的组件并做简要描述**

1. Zookeeper:是一个开源的分布式应用程序协调服务,基于zookeeper 可以实现同步服务， 配置维
   护，命名服务。
2. Flume:一个高可用的，高可靠的，分布式的海量日志采集、聚合和传输的系统。
3. Hbase:是一个分布式的、面向列的开源数据库, 利用Hadoop HDFS 作为其存储系统。
4. Hive:基于Hadoop 的一个数据仓库工具，可以将结构化的数据档映射为一张数据库表， 并提供
   简单的sql 查询功能，可以将sql 语句转换为MapReduce 任务进行运行。

**6.什么是yarn？**

Yarn是一个资源调度平台，负责为运算程序提供服务器运算资源，相当于一个分布式的操作系统平台，而mapreduce等运算程序则相当于运行于操作系统之上的应用程序。

**7.namenode的safemode是怎么回事？如何才能退出safemode？**

namenode在刚启动的时候元数据只有文件块信息，没有文件所在datanode的信息，需要datanode自己向namenode汇报。如果namenode发现datanode汇报的文件块信息没有达到namenode内存中所有文件块的总阈值的一个百分比，namenode就会处于safemode。 只有达到这个阈值，namenode才会推出safemode。也可手动强制退出。

**8.secondarynamenode的主要职责是什么？简述其工作机制**

sn的主要职责是执行checkpoint操作 每隔一段时间，会由secondary namenode将namenode上积累的所有edits和一个最新的fsimage下载到本地，并加载到内存进行merge（这个过程称为checkpoint）

**9.一个datanode 宕机,怎么一个流程恢复？**

Datanode宕机了后，如果是短暂的宕机，可以实现写好脚本监控，将它启动起来。如果是长时间宕机了，那么datanode上的数据应该已经被备份到其他机器了， 那这台datanode就是一台新的datanode了，删除他的所有数据文件和状态文件，重新启动 。

**10.hadoop 的 namenode 宕机,怎么解决？**

先分析宕机后的损失，宕机后直接导致client无法访问，内存中的元数据丢失，但是硬盘中的元数据应该还存在，如果只是节点挂了， 重启即可，如果是机器挂了，重启机器后看节点是否能重启，不能重启就要找到原因修复了。 但是最终的解决方案应该是在设计集群的初期就考虑到这个问题，做namenode的HA。

**11.简述hadoop安装？**

1）使用 root 账户登录
2）修改 IP
3）修改 host 主机名
4）配置 SSH 免密码登录
5）关闭防火墙
6）安装 JDK
7）解压 hadoop 安装包
8）配置 hadoop 的核心文件 hadoop-env.sh，core-site.xml , mapred-site.xml ，
hdfs-site.xml
9）配置 hadoop 环境变量
10）格式化 hadoop namenode-format
11）启动节点 start-all.sh

**11.Hadoop 中需要哪些配置文件，其作用是什么？**

**1） core-site.xml：**

fs.defaultFS:hdfs://cluster1(域名)，这里的值指的是默认的HDFS 路径。
hadoop.tmp.dir:/export/data/hadoop_tmp,这里的路径默认是NameNode、DataNode、
secondaryNamenode 等存放数据的公共目录。用户也可以自己单独指定这三类节点的目录。
ha.zookeeper.quorum:hadoop101:2181,hadoop102:2181,hadoop103:2181,这里是
ZooKeeper 集群的地址和端口。注意，数量一定是奇数，且不少于三个节点。

**2）hadoop-env.sh:**

只需设置jdk 的安装路径，如：export JAVA_HOME=/usr/local/jdk。

**3）hdfs-site.xml：**

dfs.replication:他决定着系统里面的文件块的数据备份个数，默认为3 个。
dfs.data.dir:datanode 节点存储在文件系统的目录。
dfs.name.dir:是namenode 节点存储hadoop 文件系统信息的本地系统路径。

**4）mapred-site.xml：**

mapreduce.framework.name: yarn 指定mr 运行在yarn 上。

**12.请列出hadoop正常工作时要启动那些进程，并写出各自的作用。**

namenode:管理集群并记录datanode的元数据，相应客户端的请求。

seconder namenode：对namenode一定范围内的数据做一份快照性备份。它不是namenode 的冗余守护进程，而是提供周期检查点和清理任务。帮 助NN 合并editslog，减少NN 启动时间。

datanode：存储数据。它负责管理连接到节点的存储（一个集群中可以有多个节点）。每个存储数据的节点运 行一个datanode 守护进程。

jobTracker：管理客户端提交的任务，并将任务分配给TaskTracker。 

ResourceManager（JobTracker）JobTracker 负责调度DataNode 上的工作。每个DataNode 有一
个TaskTracker，它们执行实际工作。

TaskTracker：执行各个Task。

DFSZKFailoverController 高可用时它负责监控NN 的状态，并及时的把状态信息写入ZK 。它
通过一个独立线程周期性的调用NN 上的一个特定接口来获取NN 的健康状态。FC 也有选择谁作
为Active NN 的权利，因为最多只有两个节点，目前选择策略还比较简单（先到先得，轮换）。

JournalNode 高可用情况下存放namenode 的editlog 文件。

**13.1用mapreduce怎么处理数据倾斜问题?**

数据倾斜：map /reduce程序执行时，reduce节点大部分执行完毕，但是有一个或者几个reduce节点运行很慢，导致整个程序的处理时间很长， 这是因为某一个key的条数比其他key多很多（有时是百倍或者千倍之多），这条key所在的reduce节点所处理的数据量比其他节点就大很多， 从而导致某几个节点迟迟运行不完，此称之为数据倾斜。

解决：自己实现partition类，用key和value相加取hash值。

数据的倾斜主要是两个的数据相差的数量不在一个级别上，在只想任务时就造成了数据的倾斜，可以通过分区的方法减少reduce数据倾斜性能的方法，例如;抽样和范围的分区、自定义分区、数据大小倾斜的自定义侧咯

**13.2简述Hadoop 的几个默认端口及其含义**

- dfs.namenode.http-address:50070
- SecondaryNameNode 辅助名称节点端口号：50090
- dfs.datanode.address:50010
- fs.defaultFS:8020 或者9000
- yarn.resourcemanager.webapp.address:8088

**13.3Mapreduce 的 map 数量 和 reduce 数量 怎么确定 ,怎么配置？**

map数量由处理数据分成的block数量决定。default_num = total_size / split_size;

reduce的数量由job.setNumReduceTasks(x),x为设定的reduce数量。不设置的话默认为1

**14.hdfs的体系结构**

hdfs有namenode、secondraynamenode、datanode组成。

namenode负责管理datanode和记录元数据

secondraynamenode:帮助 NameNode 合并编辑日志，减少 NameNode 启动时间

datanode负责存储数据

**15.说下对hadoop 的一些理解，包括哪些组件：**

详谈hadoop的应用，包括的组件分为三类，分别说明hdfs，yarn，mapreduce。

**16.一些传统的hadoop 问题,mapreduce 他就问shuffle 阶段,你怎么理解的 ?**

Shuffle意义在于将不同map处理后的数据进行合理分配，让reduce处理，从而产生了排序、分区、分组。

**17.NameNode 负责管理 metadata，client 端每次读写请求，它都会从磁盘中读取或则会写入 metadata信息并反馈client 端。（错误）**

**解析：**NameNode 不需要从磁盘读取 metadata，所有数据都在内存中，硬盘上的只是序列化的结果，只有每次 namenode 启动的时候才会读取。

\18. **Hadoop 中需要哪些配置文件，其作用是什么？**

1）core-site.xml：
  (1)fs.defaultFS:hdfs://cluster1(域名)，这里的值指的是默认的 HDFS 路径 。
  (2)hadoop.tmp.dir:/export/data/hadoop_tmp,这里的路径默认是 NameNode、DataNode、secondaryNamenode 等存放数据的公共目录。用户也可以自己单独指定这三类节点的目录。
 （3)ha.zookeeper.quorum:hadoop101:2181,hadoop102:2181,hadoop103:2181, 这 里 是
ZooKeeper 集群的地址和端口。注意，数量一定是奇数，且不少于三个节点 。
2）hadoop-env.sh: 只需设置 jdk 的安装路径，如：export JAVA_HOME=/usr/local/jdk。

3）hdfs-site.xml：
  (1) dfs.replication:他决定着系统里面的文件块的数据备份个数，默认为 3 个。
  (2) dfs.data.dir:datanode 节点存储在文件系统的目录 。
  (3) dfs.name.dir:是 namenode 节点存储 hadoop 文件系统信息的本地系统路径 。
4）mapred-site.xml：
mapreduce.framework.name: yarn 指定 mr 运行在 yarn 上

19.**hdfs中的block默认保存几份？**

不管是hadoop1.x 还是hadoop2.x 都是默认的保存三份，可以通过参数dfs.replication就行修改，副本的数目要根据机器的个数来确定。

**20.NameNode 与SecondaryNameNode 的区别与联系？**

1）机制流程同上；
2）区别
（1）NameNode 负责管理整个文件系统的元数据，以及每一个路径（文件）所对应的
数据块信息。
（2）SecondaryNameNode 主要用于定期合并命名空间镜像和命名空间镜像的编辑日志。
3）联系：
（1）SecondaryNameNode 中保存了一份和 namenode 一致的镜像文件（fsimage）和编
辑日志（edits）。
（2）在主 namenode 发生故障时（假设没有及时备份数据），可以从 SecondaryNameNode
恢复数据。

21。**怎样快速的杀死一个job？**

1、执行hadoop  job -list  拿到job-id

2、Hadoop job kill hadoop-id

**22。 HAnamenode 是如何工作的?**

![img](https://img-blog.csdnimg.cn/20190401204704121.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NodWp1ZWxpbg==,size_16,color_FFFFFF,t_70)

那么下面我再详细介绍一下：

hdfs 是一个分布式文件系统，有namenode和datanode，我们都知道，一旦namenode荡机，整个集群就会瘫痪，那么这个问题怎么处理：

一般我们都会有两个namenode，我们知道有一个secondary namenode，但是我们知道这个namenode并不能执行namenode的功能，他只是帮namenode做操作日志的合并，所以我们需要另一种部署模式，即HA部署模式

HA部署模式，是一种高可用部署模式，也就是一天24小时都在工作，他有两个namenode。namenode记录的是元数据，这个元数据放在内存中， 在磁盘上有一个镜像文件，这个镜像文件是fsimage+编号，还有大量的操作日志叫做edits+编号，两个编号都是对应起来的，而且内存里面的元数据都是齐全的，两个namenode只有一个是对客户端服务的，另外一个用来备份，对外服务的状态成为active，备份的是standby，如果有一天active namenode荡机了，standby要接管对外服务，但是它还没有元数据，那么这个问题怎么解决的。

如果active namenode荡机，standby要立马接管，意味着这两个的元数据必须要时刻同步，如果是standby namenode经常性的去active拷贝元数据信息，那么这样对active namenode的压力是很大的，所以首先，一开始格式化的时候，生成一个最初的元数据，先给standby拷贝一份，在运行的过程中，日志不仅在自己的磁盘上，还放在一个 日志存储 系统中，standby定期的去从日志存储系统中拿取日志文件，并且和最初的元数据fsimage进行合并，生成一个新的镜像，如果差下那么一点日志没有合并到，就在这一瞬间，active namenode荡机了，然后standby namenode会从日志存储系统拿取缺少的那一块日志，与原来的元数据进行合并，进行更新，这样状态就和active namenode的状态是一致的，这样就可以很快的接手对外服务。

日志存储系统：

这是个很重要的，这个系统是不能挂掉的，这个系统不是一个单节点，这个系统也是一个集群，里面有很多台机器，这个集群也是基数台，而且每台之间会同步日志，这样一来，日志存储系统的可用性就会很高了，数据同步的算法和zookeeper是一样的，即数据在多个节点之间同步，采用的是paxos算法，多数成立则成立，所以这个日志存储系统最多可以挂掉半数以下的机器，这个系统叫做QJournal，底层的功能依赖zookeeper集群，这两个集群在业务上没关系，只是利用zookeeper，就像hbase依赖zookeeper一样。

但是现在有一个问题，就是active namenode这台机器挂掉之后，standby namenode这台机器是怎么知道的，active namenode 可以在zookeeper上记载东西，然后standby去监听，一旦这个active namenode不见了，那么就说明挂了，这是一种方法。

官方是这么做的，提供了一个额外的程序，叫做zkfc，就是基于zookeeper实现的failover controller，故障控制器，运行在namenode机器上监控namenode的进程并且把监控信息记录在zookeeper中，standby 机器也会运行zkfc，监控自己机器上的进程，也会监听zookeeper里面的另一个zkfc写的东西，一旦发生变化，得到zookeeper的通知，就可以调用方法，将自己的状态从standby切换成active状态，然后开始对外服务，但是问题没有那么简单，有时候JVM会冻结这个namenode，zkfc以为namenode挂掉了，其实只是清理以及维护，但是这样的话，zkfc将将状态提交给zookeeper，然后standby namenode会收到zookeeper的通知，那就切换状态了，这就完了，就将存在两个active namenode，这样系统会错乱。

这里还有一个机制，就是当standby namenode收到通知切换状态的时候，先不着急切换，而是先采取措施确保防止这种系统的紊乱，首先会做两件事。

1.通过SSH远程指令，杀掉active namenode 的进程，但是如果不仅仅是namenode挂了，而是整个机器挂了，那发送的指令

就不会有反应，也不会有反馈信息，

2.那么如果SSH没有响应，则帮用户调用一个用户所指定的脚本，脚本运行成功，则切换状态

做完这两件事，状态就切换成功了，这就是HA高可用集群运行机制。


23.**hdfs 的数据压缩算法？**

(1) Gzip 压缩

优点：压缩率比较高，而且压缩/解压速度也比较快； hadoop 本身支持，在应用中处理gzip 格式的文件就和直接处理文本一样；大部分 linux 系统都自带 gzip 命令，使用方便.

缺点：不支持 split。

应用场景： 当每个文件压缩之后在 130M 以内的（1 个块大小内），都可以考虑用 gzip压缩格式。 例如说一天或者一个小时的日志压缩成一个 gzip 文件，运行 mapreduce 程序的时候通过多个 gzip 文件达到并发。 hive 程序， streaming 程序，和 java 写的 mapreduce 程序完全和文本处理一样，压缩之后原来的程序不需要做任何修改。

(2) Bzip2 压缩

优点：支持 split；具有很高的压缩率，比 gzip 压缩率都高； hadoop 本身支持，但不支持 native；在 linux 系统下自带 bzip2 命令，使用方便。

缺点：压缩/解压速度慢；不支持 native。

应用场景： 适合对速度要求不高，但需要较高的压缩率的时候，可以作为 mapreduce 作业的输出格式； 或者输出之后的数据比较大，处理之后的数据需要压缩存档减少磁盘空间并且以后数据用得比较少的情况；或者对单个很大的文本文件想压缩减少存储空间，同时又需要支持 split，而且兼容之前的应用程序（即应用程序不需要修改）的情况。

(3) Lzo 压缩

优点：压缩/解压速度也比较快，合理的压缩率；支持 split，是 hadoop 中最流行的压缩格式；可以在 linux 系统下安装 lzop 命令，使用方便。

缺点：压缩率比 gzip 要低一些； hadoop 本身不支持，需要安装；在应用中对 lzo 格式的文件需要做一些特殊处理（为了支持 split 需要建索引，还需要指定 inputformat 为 lzo 格式）。

应用场景： 一个很大的文本文件，压缩之后还大于 200M 以上的可以考虑，而且单个文件越大， lzo 优点越越明显。

(4) Snappy 压缩

优点：高速压缩速度和合理的压缩率。

缺点：不支持 split；压缩率比 gzip 要低； hadoop 本身不支持，需要安装；

应用场景： 当 Mapreduce 作业的 Map 输出的数据比较大的时候，作为 Map 到 Reduce的中间数据的压缩格式；或者作为一个 Mapreduce 作业的输出和另外一个Mapreduce 作业的输入。


**24.hadoop的调度？**

Hadoop 的调度有三种其中fifo的调度hadoop的默认的，这种方式是按照作业的优先级的高低与到达时间的先后执行的。

公平调度器：名字见起意就是分配用户的公平获取共享集群呗!

容量调度器:让程序都能货到执行的能力，在队列中获得资源。

25.**datanode 在什么情况下不会备份？**

Hadoop保存的三个副本如果不算备份的话，那就是在正常运行的情况下不会备份，也是就是在设置副本为1的时候不会备份，说白了就是单台机器呗！！还有datanode 在强制关闭或者非正常断电不会备份。

**26.hadoop flush 的过程？**

Flush 就是把数据落到磁盘，把数据保存起来呗!

27.**三个 datanode，当有一个 datanode 出现错误会怎样？**

第一不会给储存带来影响，因为有其他的副本保存着，不过建议尽快修复，第二会影响运算的效率，机器少了，reduce在保存数据时选择就少了，一个数据的块就大了所以就会慢。

28.**文件大小默认为 64M，改为 128M 有啥影响？**

增加文件块大小，需要增加磁盘的传输速率

29.**datanode 首次加入 cluster 的时候，如果 log 报告不兼容文件版本，那需要namenode 执行格式化操作，这样处理的原因是？**

 这样处理是不合理的，因为 namenode 格式化操作，是对文件系统进行格式

化，namenode 格式化时清空 dfs/name 下空两个目录下的所有文件，之后，会在目

录 dfs.name.dir 下创建文件。

文本不兼容，有可能时 namenode 与 datanode 的 数据里的 namespaceID、

clusterID 不一致，找到两个 ID 位置，修改为一样即可解决。

30.**MapReduce 中排序发生在哪几个阶段？这些排序是否可以避免？为什么？**

1）排序的分类：
（1）部分排序：
MapReduce 根据输入记录的键对数据集排序。保证输出的每个文件内部排序。
（2）全排序：
如何用 Hadoop 产生一个全局排序的文件？最简单的方法是使用一个分区。但该方法在
处理大型文件时效率极低，因为一台机器必须处理所有输出文件，从而完全丧失了
MapReduce 所提供的并行架构。
替代方案：首先创建一系列排好序的文件；其次，串联这些文件；最后，生成一个全局
排序的文件。主要思路是使用一个分区来描述输出的全局排序。例如：可以为待分析文件创
建 3 个分区，在第一分区中，记录的单词首字母 a-g，第二分区记录单词首字母 h-n, 第三分
区记录单词首字母 o-z。
（3）辅助排序：（GroupingComparator 分组）
Mapreduce 框架在记录到达 reducer 之前按键对记录排序，但键所对应的值并没有被排
序。甚至在不同的执行轮次中，这些值的排序也不固定，因为它们来自不同的 map 任务且
这些 map 任务在不同轮次中完成时间各不相同。一般来说，大多数 MapReduce 程序会避免
让 reduce 函数依赖于值的排序。但是，有时也需要通过特定的方法对键进行排序和分组等
以实现对值的排序。
（4）二次排序：
在自定义排序过程中，如果 compareTo 中的判断条件为两个即为二次排序。
2）自定义排序 WritableComparable
bean 对象实现 WritableComparable 接口重写 compareTo 方法，就可以实现排序。

31.**简单概述一下hadoop1与hadoop2的区别？**

Hadoop2与hadoop1最大的区别在于HDFS的架构与mapreduce的很大的区别，而且速度上有很大的提升，hadoop2最主要的两个变化是：namenode可以集群的部署了，hadoop2中的mapreduce中的jobTracker中的资源调度器与生命周期管理拆分成两个独立的组件，并命名为YARN 。

32.**YARN的新特性?**

YARN是hadoop2.x之后才出的，主要是hadoop的HA(也就是集群)，磁盘的容错，资源调度器 。

33.**hadoop join的原理?**

实现两个表的join首先在map端需要把表标示一下，把其中的一个表打标签，到reduce端再进行笛卡尔积的运算，就是reduce进行的实际的链接操作。

33.**hadoop的二次排序?**

Hadoop默认的是HashPartitioner排序，当map端一个文件非常大另外一个文件非常小时就会产生资源的分配不均匀，既可以使用setPartitionerClass来设置分区，即形成了二次分区。

34.**hadoop的mapreduce的排序发生在几个阶段？**

发生在两个阶段即使map与reduce阶段

**35.使用mr，spark ,spark sql编写word count程序?**

**37.Hadoop中job和Tasks之间的区别是什么？**

job是工作的入口，负责控制、追踪、管理任务，也是一个进程 包含map task和reduce task

Tasks是map和reduce里面的步骤，主要用于完成任务，也是线程.

**38.Hadoop中的RecordReader的作用是什么？**

属于split和mapper之间的一个过程 将inputsplit输出的行为一个转换记录，成为key-value的记录形式提供给mapper 。

![img](https://img-blog.csdn.net/20160618112128473?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

**39.Map阶段结束后，Hadoop框架会处理：Partitioning ,shuffle 和sort,在这个阶段都会发生了什么？**

MR一共有四个阶段，split map shuff reduce 在执行完map之后，可以对map的输出结果进行分区， 分区：这块分片确定到哪个reduce去计算(汇总) 排序：在每个分区中进行排序，默认是按照字典顺序。 Group：在排序之后进行分组 。

**40.mapreduce的优化方法？**

数据输入

map阶段

reduce阶段

IO传输

数据倾斜问题

常用的调优参数

**41.如果没有定义的partitioner，那么数据在被送达reducer前是如何被分区？**

如果没有自定义的 partitioning，则默认的 partition 算法，即根据每一条数据的 key

的 hashcode 值摸运算（%）reduce 的数量，得到的数字就是“分区号“。

**42. MapReduce 怎么实现 TopN？**

可以自定义groupingcomparator，对结果进行最大值排序，然后再reduce输出时，控制只输出前n个数。就达到了topn输出的目的。

**43.HDFS的存储机制？**

HDFS存储机制，包括HDFS的写入过程和读取过程两个部分

写数据过程：

1）客户端向namenode请求上传文件，namenode检查目标文件是否已存在，父目录是否存在。

2）namenode返回是否可以上传。

3）客户端请求第一个 block上传到哪几个datanode服务器上。

4）namenode返回3个datanode节点，分别为dn1、dn2、dn3。

5）客户端请求dn1上传数据，dn1收到请求会继续调用dn2，然后dn2调用dn3，将这个通信管道建立完成

6）dn1、dn2、dn3逐级应答客户端

7）客户端开始往dn1上传第一个block（先从磁盘读取数据放到一个本地内存缓存），以packet为单位，dn1收到一个packet就会传给dn2，dn2传给dn3；dn1每传一个packet会放入一个应答队列等待应答

8）当一个block传输完成之后，客户端再次请求namenode上传第二个block的服务器。（重复执行3-7步）

读数据过程：

1）客户端向namenode请求下载文件，namenode通过查询元数据，找到文件块所在的datanode地址。

2）挑选一台datanode（就近原则，然后随机）服务器，请求读取数据。

3）datanode开始传输数据给客户端（从磁盘里面读取数据放入流，以packet为单位来做校验）。

4）客户端以packet为单位接收，先在本地缓存，然后写入目标文件。

**44.列出的Hadoop 1和Hadoop 2之间的差异?**

在Hadoop的1.x中，“Namenode”有单点问题。在Hadoop的2.x中，我们有主动和被动“Namenodes”。如果主动“的Namenode”失败，则被动“的Namenode”负责。正因为如此，高可用性可以Hadoop中2.x中来实现

此外，在Hadoop的2.X，YARN提供了一个中央资源管理器。通过YARN,你现在可以在Hadoop中运行多个应用程序，共享公共资源。 MR2是一种特殊类型的运行于YARN MapReduce框架之上的分布式应用。其他工具也可以通过YARN执行数据处理。

45.**简答说一下hadoop的map-reduce编程模型？**

首先map task会从本地文件系统读取数据，转换成key-value形式的键值对集合。
将键值对集合输入mapper进行业务处理过程，将其转换成需要的key-value在输出。
之后会进行一个partition分区操作，默认使用的是hashpartitioner，可以通过重写hashpartitioner的getpartition方法来自定义分区规则。
之后会对key进行进行sort排序，grouping分组操作将相同key的value合并分组输出。
在这里可以使用自定义的数据类型，重写WritableComparator的Comparator方法来自定义排序规则，重写RawComparator的compara方法来自定义分组规则。
之后进行一个combiner归约操作，其实就是一个本地段的reduce预处理，以减小后面shufle和reducer的工作量。
reduce task会通过网络将各个数据收集进行reduce处理，最后将数据保存或者显示，结束整个job。

46.**hadoop的TextInputFormat作用是什么，如何自定义实现？**

InputFormat会在map操作之前对数据进行两方面的预处理。
1是getSplits，返回的是InputSplit数组，对数据进行split分片，每片交给map操作一次 。
2是getRecordReader，返回的是RecordReader对象，对每个split分片进行转换为key-value键值对格式传递给map。
常用的InputFormat是TextInputFormat，使用的是LineRecordReader对每个分片进行键值对的转换，以行偏移量作为键，行内容作为值。
自定义类继承InputFormat接口，重写createRecordReader和isSplitable方法 。
在createRecordReader中可以自定义分隔符。

47.列举几个 hadoop 生态圈的组件并做简要描述

1）Zookeeper:是一个开源的分布式应用程序协调服务,基于 zookeeper 可以实现同步服务，配置维护，命名服务。

2）Flume:一个高可用的，高可靠的，分布式的海量日志采集、聚合和传输的系统。

3）Hbase:是一个分布式的、面向列的开源数据库, 利用 Hadoop HDFS 作为其存储系统

4）Hive:基于 Hadoop 的一个数据仓库工具，可以将结构化的数据档映射为一张数据库表，
并提供简单的 sql 查询功能，可以将 sql 语句转换为 MapReduce 任务进行运行。
5）Sqoop:将一个关系型数据库中的数据导进到 Hadoop 的 HDFS 中，也可以将 HDFS 的数
据导进到关系型数据库中。

50.谈谈 Hadoop 序列化和反序列化及自定义 bean 对象实现序列化?

1）序列化和反序列化
序列化就是把内存中的对象，转换成字节序列（或其他数据传输协议）以便于存储（持久化）和网络传输。
反序列化就是将收到字节序列（或其他数据传输协议）或者是硬盘的持久化数据，转换成内存中的对象。


Java 的序列化是一个重量级序列化框架（Serializable），一个对象被序列化后，会附带很多额外的信息（各种校验信息，header，继承体系等），不便于在网络中高效传输。所以，hadoop 自己开发了一套序列化机制（Writable），精简、高效。

51.在Hadoop 中定义的 InputFormat 中，默认是哪一个？

TextInputFormat

\52. 两个类TextInputFormat 和KeyValueInputFormat 的区别是什么？

1）相同点：
TextInputformat 和 KeyValueTextInputFormat 都继承了 FileInputFormat 类，都是每一行作
为一个记录；
2）区别：
TextInputformat 将每一行在文件中的起始偏移量作为 key，每一行的内容作为 value。
默认以\n 或回车键作为一行记录。
KeyValueTextInputFormat 适合处理输入数据的每一行是两列，并用 tab 分离的形式

53.请描述 mapReduce 中 shuffle 阶段的工作流程？

1）maptask 收集我们的 map()方法输出的 kv 对，放到内存缓冲区中
2）从内存缓冲区不断溢出本地磁盘文件，可能会溢出多个文件
3）多个溢出文件会被合并成大的溢出文件
4）在溢出过程中，及合并的过程中，都要调用 partitioner 进行分区和针对 key 进行排
序
5）reducetask 根据自己的分区号，去各个 maptask 机器上取相应的结果分区数据
6）reducetask 会取到同一个分区的来自不同 maptask 的结果文件，reducetask 会将这些
文件再进行合并（归并排序）
7）合并成大文件后，shuffle 的过程也就结束了，后面进入 reducetask 的逻辑运算过程
（从文件中取出一个一个的键值对 group，调用用户自定义的 reduce()方法）

54.请描述 mapReduce 中 combiner 的作用是什么，一般使用情景，哪些情况不需要，及和 reduce 的区别？

1）Combiner 的意义就是对每一个 maptask 的输出进行局部汇总，以减小网络传输量。
2）Combiner 能够应用的前提是不能影响最终的业务逻辑，而且，Combiner 的输出 kv 应该跟 reducer 的输入 kv 类型要对应起来。
3）Combiner 和 reducer 的区别在于运行的位置。Combiner 是在每一个 maptask 所在的节点运行；Reducer 是接收全局所有 Mapper 的输出结果。

55.如何使用mapReduce 实现两个表的join?

1）reduce side join : 在 map 阶段，map 函数同时读取两个文件 File1 和 File2，为了区分两种来源的 key/value 数据对，对每条数据打一个标签（tag）,比如：tag=0 表示来自文件 File1，tag=2 表示来自文件 File2。
2）map side join : Map side join 是针对以下场景进行的优化：两个待连接表中，有一个表非常大，而另一个表非常小，以至于小表可以直接存放到内存中。这样，我们可以将小表复制多份，让每个 map task 内存中存在一份（比如存放到 hash table 中），然后只扫描大表：对于大表中的每一条记录 key/value，在 hash table 中查找是否有相同的 key 的记录，如果有，则连接后输出即可。

56.有可能使 Hadoop 任务输出到多个目录中么？如果可以，怎么做？

 1）可以输出到多个目录中，采用自定义 OutputFormat。 
2）实现步骤：
（1）自定义 outputformat，
（2）改写 recordwriter，具体改写输出数据的方法 write()

 57.为什么会产生 yarn,它解决了什么问题，有什么优势？

Yarn 最主要的功能就是解决运行的用户程序与 yarn 框架完全解耦。Yarn 上可以运行各种类型的分布式运算程序（mapreduce 只是其中的一种），比如 mapreduce、storm 程序，spark 程序……

58.yarn工作流程?

![img](https://img-blog.csdnimg.cn/20190401214315992.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NodWp1ZWxpbg==,size_16,color_FFFFFF,t_70)

 运行流程
  1、客户端向RM中提交程序 
  2、RM向NM中分配一个container，并在该container中启动AM 
  3、AM向RM注册，这样用户可以直接通过RM査看应用程序的运行状态(然后它将为各个任务申请资源，并监控它的运行状态，直到运行结束) 
  4、AM采用轮询的方式通过RPC协议向RM申请和领取资源，资源的协调通过异步完成 
  5、AM申请到资源后，便与对应的NM通信，要求它启动任务 
  6、NM为任务设置好运行环境(包括环境变量、JAR包、二进制程序等)后，将任务启动命令写到一个脚本中，并通过运行该脚本启动任务 
  7、各个任务通过某个RPC协议向AM汇报自己的状态和进度，以让AM随时掌握各个任务的运行状态，从而可以在任务失败时重新启动任务 
  8、应用程序运行完成后，AM向RM注销并关闭自己

59.MapReduce 2.0 容错性?

1）MRAppMaster 容错性
一旦运行失败，由 YARN 的 ResourceManager 负责重新启动，最多重启次数可由用户设
置，默认是 2 次。一旦超过最高重启次数，则作业运行失败。
2）Map Task/Reduce Task
Task 周期性向 MRAppMaster 汇报心跳；一旦 Task 挂掉，则 MRAppMaster 将为之重新申请资源，并运行之。最多重新运行次数可由用户设置，默认 4 次。

60.优化（☆☆☆☆☆）

 mapreduce 跑的慢的原因?

Mapreduce 程序效率的瓶颈在于两点：
1）计算机性能
CPU、内存、磁盘健康、网络
2）I/O 操作优化

（1）数据倾斜
（2）map 和 reduce 数设置不合理
（3）reduce 等待过久
（4）小文件过多
（5）大量的不可分块的超大文件
（6）spill 次数过多
（7）merge 次数过多等。

 

 mapreduce 优化方法:

1）数据输入：
（1）合并小文件：在执行 mr 任务前将小文件进行合并，大量的小文件会产生大量的map 任务，增大 map 任务装载次数，而任务的装载比较耗时，从而导致 mr 运行较慢。
（2）采用 ConbinFileInputFormat 来作为输入，解决输入端大量小文件场景。
2）map 阶段
（1）减少 spill 次数：通过调整 io.sort.mb 及 sort.spill.percent 参数值，增大触发 spill 的内存上限，减少 spill 次数，从而减少磁盘 IO。
（2）减少 merge 次数：通过调整 io.sort.factor 参数，增大 merge 的文件数目，减少 merge的次数，从而缩短 mr 处理时间。
（3）在 map 之后先进行 combine 处理，减少 I/O。
3）reduce 阶段
（1）合理设置 map 和 reduce 数：两个都不能设置太少，也不能设置太多。太少，会导致 task 等待，延长处理时间；太多，会导致 map、reduce 任务间竞争资源，造成处理超时等错误。
（2）设置 map、reduce 共存：调整 slowstart.completedmaps 参数，使 map 运行到一定程度后，reduce 也开始运行，减少 reduce 的等待时间。
（3）规避使用 reduce，因为 Reduce 在用于连接数据集的时候将会产生大量的网络消耗。
4）IO 传输
（1）采用数据压缩的方式，减少网络 IO 的的时间。安装 Snappy 和 LZOP 压缩编码器。
（2）使用 SequenceFile 二进制文件
5）数据倾斜问题
（1）数据倾斜现象
数据频率倾斜——某一个区域的数据量要远远大于其他区域。
数据大小倾斜——部分记录的大小远远大于平均值。
  (2)减少数据倾斜的方法

方法 1：抽样和范围分区

可以通过对原始数据进行抽样得到的结果集来预设分区边界值。
方法 2：自定义分区
另一个抽样和范围分区的替代方案是基于输出键的背景知识进行自定义分区。例如，如map 输出键的单词来源于一本书。其中大部分必然是省略词（stopword）。那么就可以将自定义分区将这部分省略词发送给固定的一部分 reduce 实例。而将其他的都发送给剩余的 reduce 实例。
方法 3：Combine
使用 Combine 可以大量地减小数据频率倾斜和数据大小倾斜。在可能的情况下，combine 的目的就是聚合并精简数据。

6）常用的调优参数

61.HDFS 小文件优化方法（☆☆☆☆☆）

1）HDFS 小文件弊端：

HDFS 上每个文件都要在 namenode 上建立一个索引，这个索引的大小约为 150byte，这样当小文件比较多的时候，就会产生很多的索引文件，一方面会大量占用 namenode 的内存空间，另一方面就是索引文件过大是的索引速度变慢。
2）解决的方式：
（1）Hadoop 本身提供了一些文件压缩的方案。
（2）从系统层面改变现有 HDFS 存在的问题，其实主要还是小文件的合并，然后建立比较
快速的索引。