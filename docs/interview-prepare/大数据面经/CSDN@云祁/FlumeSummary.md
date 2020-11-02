### 文章目录

- - [一、Flume 采集数据会丢失吗?](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume__1)
  - [二、Flume 与 Kafka 的选取？](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume__Kafka__7)
  - [三、数据怎么采集到 Kafka，实现方式？](https://blog.csdn.net/BeiisBei/article/details/104232864#_Kafka_18)
  - [四、flume 管道内存，flume 宕机了数据丢失怎么解决？](https://blog.csdn.net/BeiisBei/article/details/104232864#font_colorFF0000flume_flume__21)
  - [五、flume 和 kafka 采集日志区别，采集日志时中间停了，怎么记录之前的日志？](https://blog.csdn.net/BeiisBei/article/details/104232864#flume__kafka__25)
  - [六、flume 有哪些组件，flume 的 source、channel、sink 具体是做什么的？](https://blog.csdn.net/BeiisBei/article/details/104232864#font_colorFF0000flume_flume__sourcechannelsink__30)
  - [七、为什么使用Flume？](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume_46)
  - [八、Flume组成架构？](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume_48)
  - [九、FlumeAgent内部原理？](https://blog.csdn.net/BeiisBei/article/details/104232864#FlumeAgent_68)
  - [十、Flume Event 是数据流的基本单元。](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume_Event__70)
  - [十一、Flume agent](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume_agent_75)
  - [十二、Flume channel](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume_channel_85)
  - [十三、Flume sink](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume_sink_114)
  - [十四、你是如何实现Flume数据传输的监控的](https://blog.csdn.net/BeiisBei/article/details/104232864#Flume_124)
  - [十五、flume 调优](https://blog.csdn.net/BeiisBei/article/details/104232864#flume__127)
  - [十六、flume 选择器](https://blog.csdn.net/BeiisBei/article/details/104232864#flume__142)



## 一、Flume 采集数据会丢失吗?

不会，Channel 存储可以存储在 File 中，数据传输自身有事务。

> 不会，因为 channel 可以存储在 file 中，而且 flume 本身是有事务的。
> 可以做 sink 组，一个坏掉了，就用另一个。

## 二、Flume 与 Kafka 的选取？

采集层主要可以使用 Flume、Kafka 两种技术。

- Flume：Flume 是管道流方式，提供了很多的默认实现，让用户通过参数部署，及扩展 API。
- Kafka：Kafka 是一个可持久化的分布式的消息队列。

Kafka 是一个非常通用的系统。你可以有许多生产者和很多的消费者共享多个主题Topics。相比之下，Flume 是一个专用工具被设计为旨在往 HDFS，HBase 发送数据。它对HDFS 有特殊的优化，并且集成了 Hadoop 的安全特性。所以，Cloudera 建议如果数据被多个系统消费的话，使用 kafka；如果数据被设计给 Hadoop 使用，使用 Flume。正如你们所知 Flume 内置很多的 source 和 sink 组件。然而，Kafka 明显有一个更小的生产消费者生态系统，并且 Kafka 的社区支持不好。希望将来这种情况会得到改善，但是目前：使用 Kafka 意味着你准备好了编写你自己的生产者和消费者代码。如果已经存在的 Flume Sources 和 Sinks 满足你的需求，并且你更喜欢不需要任何开发的系统，请使用 Flume。Flume 可以使用拦截器实时处理数据。这些对数据屏蔽或者过量是很有用的。Kafka 需要外部的流处理系统才能做到。

Kafka 和 Flume 都是可靠的系统,通过适当的配置能保证零数据丢失。然而，Flume 不支持副本事件。于是，如果 Flume 代理的一个节点奔溃了，即使使用了可靠的文件管道方式，你也将丢失这些事件直到你恢复这些磁盘。如果你需要一个高可靠行的管道，那么使用Kafka 是个更好的选择。

Flume 和 Kafka 可以很好地结合起来使用。如果你的设计需要从 Kafka 到 Hadoop 的流数据，使用 Flume 代理并配置 Kafka 的 Source 读取数据也是可行的：你没有必要实现自己的消费者。你可以直接利用Flume 与HDFS 及HBase 的结合的所有好处。你可以使用ClouderaManager 对消费者的监控，并且你甚至可以添加拦截器进行一些流处理。

## 三、数据怎么采集到 Kafka，实现方式？

使用官方提供的 flumeKafka 插件，插件的实现方式是自定义了 flume 的 sink，将数据从channle 中取出，通过 kafka 的producer 写入到 kafka 中，可以自定义分区等。

## 四、flume 管道内存，flume 宕机了数据丢失怎么解决？

1）Flume 的 channel 分为很多种，可以将数据写入到文件。
2）防止非首个 agent 宕机的方法数可以做集群或者主备。

## 五、flume 和 kafka 采集日志区别，采集日志时中间停了，怎么记录之前的日志？

Flume 采集日志是通过流的方式直接将日志收集到存储层，而 kafka 是将缓存在 kafka集群，待后期可以采集到存储层。
Flume 采集中间停了，可以采用文件的方式记录之前的日志，而 kafka 是采用 offset 的方式记录之前的日志。

## 六、flume 有哪些组件，flume 的 source、channel、sink 具体是做什么的？

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209114402113.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)
1）source：用于采集数据，Source 是产生数据流的地方，同时 Source 会将产生的数据
流传输到 Channel，这个有点类似于 Java IO 部分的 Channel。
2）channel：用于桥接 Sources 和 Sinks，类似于一个队列。
3）sink：从 Channel 收集数据，将数据写到目标源(可以是下一个 Source，也可以是 HDFS
或者 HBase)。

> source ：搜集数据
> channel ：数据缓存
> sink ：把数据发送到目的地
> 常用 source 类型 ：
> 1、 监控文件 ：exec
> 2、监控目录 ：spooldir

## 七、为什么使用Flume？

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209114553590.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

## 八、Flume组成架构？

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209114641643.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)
**关于flume事务**

flume要尽可能的保证数据的安全性，其在source推送数据到channel以及sink从channel拉取数据时都是以事务方式进行的。因为在agent内的两次数据传递间都会涉及到数据的传送、从数据上游删除数据的问题；就比如sink从channel拉取数据并提交到数据下游之后需要从channel中删除已获取到的批次数据，其中跨越了多个原子事件，故而需要以事务的方式将这些原子事件进一步绑定在一起，以便在其中某个环节出错时进行回滚防止数据丢失。所以在选用file channel时一般来说是不会丢失数据的。

> channel ： 是位于 source 和 sink 之间的缓冲区。
> 1 ，flume 自带两种缓冲区，file channel 和 memory channel
> 2 ，file channel ： 硬盘缓冲区，性能低，但是安全。系统宕机也不会丢失数据。
> 3 ，memory channel ：内存缓冲区，性能高，但是有可能丢数据，在不关心数据有可能丢失的情况下使用。
> put 事务流程 ： 源将数据给管道
> 1 ，doPut ：把数据写入临时缓冲区 putList 。
> 2 ，doCommit ：检查 channel 内存队列是否足够合并。
> 3 ，doRollBack ： 如果 channel 不行，我们就回滚数据。
> take 事务流程 ：
> 1 ，先将数据取到临时缓冲区 takeList。
> 2 ，doCommit ：如果数据全部发送成功，就清除临时缓冲区。
> 3 ，doRollBack ：如果数据发送过程中出现异常，doRollBack 将临时缓冲区的数据还给 channel 队列

## 九、FlumeAgent内部原理？

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209115141828.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

## 十、Flume Event 是数据流的基本单元。

它由一个装载数据的字节数组(byte payload)和一系列可选的字符串属性来组成(可选头部)。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209115611537.png#pic_center)

## 十一、Flume agent

Flume source 消耗从类似于 web 服务器这样的外部源传来的 events.

外部数据源以一种 Flume source 能够认识的格式发送 event 给 Flume source.

Flume source 组件可以处理各种类型、各种格式的日志数据，包括 avro、thrift、exec、jms、spooling directory、netcat、sequence generator、syslog、http、legacy.

flume source 是负责接收数据到 Flume Agent 的组件

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209115746305.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

## 十二、Flume channel

当 Flume source 接受到一个 event 的时, Flume source 会把这个 event 存储在一个或多个 channel 中.

Channel 是连接Source和Sink的组件, 是位于 Source 和 Sink 之间的数据缓冲区。

Flume channel 使用被动存储机制. 它存储的数据的写入是靠 Flume source 来完成的, 数据的读取是靠后面的组件 Flume sink 来完成的.

Channel 是线程安全的，可以同时处理几个 Source 的写入操作和几个 Sink 的读取操作。

Flume 自带两种 Channel：

- Memory Channel

Memory Channel是内存中的队列。

Memory Channel在不需要关心数据丢失的情景下适用。

如果需要关心数据丢失，那么Memory Channel就不应该使用，因为程序死亡、机器宕机或者重启都会导致数据丢失。

- File Channel

File Channel将所有事件写到磁盘。

因此在程序关闭或机器宕机的情况下不会丢失数据。

还可以有其他的 channel: 比如 JDBC channel。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209122426243.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

## 十三、Flume sink

Sink 不断地轮询 Channel 中的事件且批量地移除它们，并将这些事件批量写入到存储或索引系统、或者发送到另一个Flume Agent。

Sink 是完全事务性的。

在从 Channel 批量删除数据之前，每个 Sink 用 Channel 启动一个事务。批量事件一旦成功写出到存储系统或下一个Flume Agent，Sink 就利用 Channel 提交事务。事务一旦被提交，该 Channel 从自己的内部缓冲区删除事件。如果写入失败，将缓冲区takeList中的数据归还给Channel。

Sink组件目的地包括hdfs、logger、avro、thrift、ipc、file、null、HBase、solr、自定义。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200209122608536.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

## 十四、你是如何实现Flume数据传输的监控的

使用第三方框架Ganglia实时监控Flume。

## 十五、flume 调优

source ：

1. 增加 source 个数，可以增大 source 读取能力。
2. 具体做法 ： 如果一个目录下生成的文件过多，可以将它拆分成多个目录。每个目录都配置一个 source 。
3. 增大 batchSize ： 可以增大一次性批处理的 event 条数，适当调大这个参数，可以调高
   source 搬运数据到 channel 的性能。

channel ：

1. memory ：性能好，但是，如果发生意外，可能丢失数据。
2. 使用 file channel 时，dataDirs 配置多个不同盘下的目录可以提高性能。
3. transactionCapacity 需要大于 source 和 sink 的 batchSize 参数

sink ：

1. 增加 sink 个数可以增加消费 event 能力

## 十六、flume 选择器

包括两种 ：
1 ，每个通道都复制一份文件，replicating 。
2 ，选择性发往某个通道，Multiplexing 。

> Flume 企业真实面试经验

### 文章目录

- - [一、你是如何实现 Flume 数据传输的监控的](https://blog.csdn.net/BeiisBei/article/details/104347414#_Flume__1)
  - [二、Flume 的 Source，Sink，Channel 的作用？你们 Source 是什么类型？](https://blog.csdn.net/BeiisBei/article/details/104347414#Flume__SourceSinkChannel__Source__5)
  - [三、Flume 的 Channel Selectors](https://blog.csdn.net/BeiisBei/article/details/104347414#Flume__Channel_Selectors_14)
  - [四、Flume 参数调优](https://blog.csdn.net/BeiisBei/article/details/104347414#Flume__16)
  - [五、Flume 的事务机制](https://blog.csdn.net/BeiisBei/article/details/104347414#Flume__37)
  - [六、Flume 采集数据会丢失吗?](https://blog.csdn.net/BeiisBei/article/details/104347414#Flume__40)



## 一、你是如何实现 Flume 数据传输的监控的

使用第三方框架 Ganglia 实时监控 Flume。

## 二、Flume 的 Source，Sink，Channel 的作用？你们 Source 是什么类型？

**1、作用**
（1）**Source** 组件是专门用来收集数据的，可以处理各种类型、各种格式的日志数据，包括 avro、thrift、exec、jms、spooling directory、netcat、sequence generator、syslog、http、legacy
（2）**Channel** 组件对采集到的数据进行缓存，可以存放在 Memory 或 File 中。
（3）**Sink** 组件是用于把数据发送到目的地的组件，目的地包括 HDFS、Logger、avro、thrift、ipc、file、Hbase、solr、自定义。
2、我公司采用的 **Source** 类型为
（1）监控后台日志：exec
（2）监控后台产生日志的端口：netcat Exec spooldir

## 三、Flume 的 Channel Selectors

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200216193725885.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_1,color_FFFFFF,t_70#pic_center)

## 四、Flume 参数调优

1. Source

增加 Source 个（使用 Tair Dir Source 时可增加 FileGroups 个数）可以增大 Source 的读取数据的能力。例如：当某一个目录产生的文件过多时需要将这个文件目录拆分成多个文件目录，同时配置好多个 Source 以保证 Source 有足够的能力获取到新产生的数据。

batchSize 参数决定 Source 一次批量运输到 Channel 的 event 条数，适当调大这个参数可以提高 Source 搬运 Event 到 Channel 时的性能。

1. Channel

type 选择 memory 时 Channel 的性能最好，但是如果 Flume 进程意外挂掉可能会丢失数据。type 选择 file 时 Channel 的容错性更好，但是性能上会比 memory channel 差。

使用 file Channel 时 dataDirs 配置多个不同盘下的目录可以提高性能。

Capacity 参数决定 Channel 可容纳最大的 event 条数。transactionCapacity 参数决定每次 Source 往 channel 里面写的最大 event 条数和每次 Sink 从 channel 里面读的最大 event条数。transactionCapacity 需要大于 Source 和 Sink 的 batchSize 参数。

1. Sink

增加 Sink 的个数可以增加 Sink 消费 event 的能力。Sink 也不是越多越好够用就行，过多的 Sink 会占用系统资源，造成系统资源不必要的浪费。

batchSize 参数决定 Sink 一次批量从 Channel 读取的 event 条数，适当调大这个参数可以提高 Sink 从 Channel 搬出 event 的性能。

## 五、Flume 的事务机制

Flume 的事务机制（类似数据库的事务机制）：Flume 使用两个独立的事务分别负责从Soucrce 到 Channel，以及从 Channel 到 Sink 的事件传递。比如 spooling directory source 为文件的每一行创建一个事件，一旦事务中所有的事件全部传递到 Channel 且提交成功，那么 Soucrce 就将该文件标记为完成。同理，事务以类似的方式处理从 Channel 到 Sink 的传递过程，如果因为某种原因使得事件无法记录，那么事务将会回滚。且所有的事件都会保持到 Channel 中，等待重新传递。

## 六、Flume 采集数据会丢失吗?

根据Flume的架构原理，Flume是不可能丢失数据的，其内部有完善的事务机制， Source到Channel是事务性的，Channel到Sink是事务性的，因此这两个环节不会出现数据的丢失，唯一可能丢失数据的情况是Channel采用 memoryChannel，agent宕机导致数据丢失，或者Channel存储数据已满，导致 Source不再写入，未写入的数据丢失。

Flume不会丢失数据，但是有可能造成数据的重复，例如数据已经成功由 Sink发出，但是没有接收到响应，Sink会再次发送数据，此时可能会导致数据的重复。