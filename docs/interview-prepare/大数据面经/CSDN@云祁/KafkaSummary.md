### 文章目录

- - [一、请说明什么是Apache Kafka?](https://blog.csdn.net/BeiisBei/article/details/104342066#Apache_Kafka_1)
  - [二、Kafka的设计是什么样的呢？](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_4)
  - [三、请说明什么是传统的消息传递方法?](https://blog.csdn.net/BeiisBei/article/details/104342066#_11)
  - [四、请说明Kafka相对传统技术有什么优势?](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_16)
  - [五、在 Kafka 中 broker 的意义是什么？](https://blog.csdn.net/BeiisBei/article/details/104342066#_Kafka__broker__25)
  - [六、什么是broker？作用是什么?](https://blog.csdn.net/BeiisBei/article/details/104342066#broker_32)
  - [七、Kafka服务器能接收到的最大信息是多少?](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_36)
  - [八、解释Kafka的Zookeeper是什么?我们可以在没有Zookeeper的情况下使用Kafka吗?](https://blog.csdn.net/BeiisBei/article/details/104342066#KafkaZookeeperZookeeperKafka_39)
  - [九、解释Kafka的用户如何消费信息?](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_50)
  - [十、解释如何提高远程用户的吞吐量?](https://blog.csdn.net/BeiisBei/article/details/104342066#_55)
  - [十一、解释一下，在数据制作过程中，你如何能从Kafka得到准确的信息?](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_58)
  - [十二、解释如何减少ISR中的扰动?broker什么时候离开ISR?](https://blog.csdn.net/BeiisBei/article/details/104342066#ISRbrokerISR_67)
  - [十三、Kafka为什么需要复制?](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_70)
  - [十四、如果副本在ISR中停留了很长时间表明什么?](https://blog.csdn.net/BeiisBei/article/details/104342066#ISR_73)
  - [十五、请说明如果首选的副本不在ISR中会发生什么?](https://blog.csdn.net/BeiisBei/article/details/104342066#ISR_76)
  - [十六、如何保证 Kafka 的消息有序（☆☆☆☆☆）？](https://blog.csdn.net/BeiisBei/article/details/104342066#_Kafka__79)
  - [十七、有可能在生产后发生消息偏移吗?](https://blog.csdn.net/BeiisBei/article/details/104342066#_82)
  - [十八、kafka主要特征](https://blog.csdn.net/BeiisBei/article/details/104342066#kafka_86)
  - [十九、列举kafka的应用场景](https://blog.csdn.net/BeiisBei/article/details/104342066#kafka_93)
  - [二十、kafka主题分区的作用](https://blog.csdn.net/BeiisBei/article/details/104342066#kafka_115)
  - [二十一、consumer水平扩展如何实现](https://blog.csdn.net/BeiisBei/article/details/104342066#consumer_118)
  - [二十二、消息的顺序](https://blog.csdn.net/BeiisBei/article/details/104342066#_121)
  - [二十三、为了避免磁盘被占满，kafka会周期性的删除陈旧的消息，删除策略是什么?](https://blog.csdn.net/BeiisBei/article/details/104342066#kafka_124)
  - [二十四、什么是日志压缩](https://blog.csdn.net/BeiisBei/article/details/104342066#_128)
  - [二十五、同一分区的多个副本包括的消息是否一致？](https://blog.csdn.net/BeiisBei/article/details/104342066#_131)
  - [二十六、数据传输的事物定义有哪三种？](https://blog.csdn.net/BeiisBei/article/details/104342066#_134)
  - [二十七、Kafka判断一个节点是否还活着有哪两个条件？](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_140)
  - [二十八、producer是否直接将数据发送到broker的leader(主节点)？](https://blog.csdn.net/BeiisBei/article/details/104342066#producerbrokerleader_145)
  - [二十九、Kafa consumer是否可以消费指定分区消息？](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafa_consumer_148)
  - [三十、Kafka消息是采用Pull模式，还是Push模式？](https://blog.csdn.net/BeiisBei/article/details/104342066#KafkaPullPush_151)
  - [三十一、Kafka存储在硬盘上的消息格式是什么？](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_157)
  - [三十二、Kafka高效文件存储设计特点](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_164)
  - [三十三、Kafka 与传统消息系统之间有三个关键区别](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka__173)
  - [三十四、Kafka创建Topic时如何将分区放置到不同的Broker中](https://blog.csdn.net/BeiisBei/article/details/104342066#KafkaTopicBroker_178)
  - [三十五、Kafka新建的分区会在哪个目录下创建](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_184)
  - [三十六、partition的数据如何保存到硬盘](https://blog.csdn.net/BeiisBei/article/details/104342066#partition_190)
  - [三十七、kafka的ack机制](https://blog.csdn.net/BeiisBei/article/details/104342066#kafkaack_196)
  - [三十八、Kafka的消费者如何消费数据](https://blog.csdn.net/BeiisBei/article/details/104342066#Kafka_202)
  - [三十八、kafka 的消费者方式？](https://blog.csdn.net/BeiisBei/article/details/104342066#kafka__206)
  - [三十九、消费者负载均衡策略](https://blog.csdn.net/BeiisBei/article/details/104342066#_214)
  - [四十、数据有序](https://blog.csdn.net/BeiisBei/article/details/104342066#_217)
  - [四十一、kafaka生产数据时数据的分组策略](https://blog.csdn.net/BeiisBei/article/details/104342066#kafaka_221)
  - [四十二、kafka 数据丢失问题，及如何保证？](https://blog.csdn.net/BeiisBei/article/details/104342066#kafka__228)



## 一、请说明什么是Apache Kafka?

Apache Kafka是由Apache开发的一种发布订阅消息系统，它是一个分布式的、分区的和重复的日志服务。

## 二、Kafka的设计是什么样的呢？

- Kafka将消息以topic为单位进行归纳
- 将向Kafka topic发布消息的程序成为producers
- 将预订topics并消费消息的程序成为consumer
- Kafka以集群的方式运行，可以由一个或多个服务组成，每个服务叫做一个broker
- producers通过网络将消息发送到Kafka集群，集群向消费者提供消息

## 三、请说明什么是传统的消息传递方法?

传统的消息传递方法包括两种：

- 排队：在队列中，一组用户可以从服务器中读取消息，每条消息都发送给其中一个人。
- 发布-订阅：在这个模型中，消息被广播给所有的用户。

## 四、请说明Kafka相对传统技术有什么优势?

- 快速：单一的Kafka代理可以处理成千上万的客户端，每秒处理数兆字节的读写操作
- 可伸缩：在一组机器上对数据进行分区和简化，以支持更大的数据
- 持久：消息是持久性的，并在集群中进行复制，以防止数据丢失
- 设计：它提供了容错保证和持久性

## 五、在 Kafka 中 broker 的意义是什么？

- 在 Kafka 集群中，broker 指 Kafka 服务器。
- 接收Producer发过来的数据，并且将它持久化，同时提供给Consumer去订阅
- 组成Kafka集群节点，之间没有主从关系，依赖ZooKeeper来协调，broker负责消息的读取和存储，一个broker可以管理多个partition

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200216142141399.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

## 六、什么是broker？作用是什么?

一个单独的kafka server就是一个broker，broker主要工作就是接收生产者发过来的消息，分配offset，之后保存到磁盘中。同时，接收消费者、其他broker的请求，根据请求类型进行相应的处理并返回响应，在一般的生产环境中，一个broker独占一台物理服务器。

## 七、Kafka服务器能接收到的最大信息是多少?

Kafka服务器可以接收到的消息的最大大小是1000000字节。

## 八、解释Kafka的Zookeeper是什么?我们可以在没有Zookeeper的情况下使用Kafka吗?

- Zookeeper是一个开放源码的、高性能的协调服务，它用于Kafka的分布式应用。
- **作用**：协调Kafka Broker，存储原数据：consumer的offset+broker信息+topic信息+partition个信息。
- 不，不可能越过Zookeeper，直接联系Kafka broker。一旦Zookeeper停止工作，它就不能服务客户端请求。
  - Zookeeper主要用于在集群中不同节点之间进行通信
  - 在Kafka中，它被用于提交偏移量，因此如果节点在任何情况下都失败了，它都可以从之前提交的偏移量中获取
  - 除此之外，它还执行其他活动，如: leader检测、分布式同步、配置管理、识别新节点何时离开或连接、集群、节点实时状态等等。

## 九、解释Kafka的用户如何消费信息?

- 在Kafka中传递消息是通过使用sendfile【**零拷贝**】 API完成的。它支持将字节从套接口转移到磁盘，通过内核空间保存副本，并在内核用户之间调用内核。
- **零拷贝**：用户向内核去发送一个命令，我要操作那些数据，然后直接从磁盘转成Socket Buffer，再从Socket Buffer到网卡Buffer，再传出去【**少了两次的copy**】

## 十、解释如何提高远程用户的吞吐量?

如果用户位于与broker不同的数据中心，则可能需要调优套接口缓冲区大小，以对长网络延迟进行摊销。

## 十一、解释一下，在数据制作过程中，你如何能从Kafka得到准确的信息?

- 在数据中，为了精确地获得Kafka的消息，你必须遵循两件事: 在数据消耗期间避免重复，在数据生产过程中避免重复。
- 这里有两种方法，可以在数据生成时准确地获得一个语义
  - 每个分区使用一个单独的写入器，每当你发现一个网络错误，检查该分区中的最后一条消息，以查看您的最后一次写入是否成功
  - 在消息中包含一个主键(UUID或其他)，并在用户中进行反复制

## 十二、解释如何减少ISR中的扰动?broker什么时候离开ISR?

ISR是一组与leaders完全同步的消息副本，也就是说ISR中包含了所有提交的消息。ISR应该总是包含所有的副本，直到出现真正的故障。如果一个副本从leader中脱离出来，将会从ISR中删除。

## 十三、Kafka为什么需要复制?

Kafka的信息复制确保了任何已发布的消息不会丢失，并且可以在机器错误、程序错误或更常见些的软件升级中使用。

## 十四、如果副本在ISR中停留了很长时间表明什么?

如果一个副本在ISR中保留了很长一段时间，那么它就表明，跟踪器无法像在leader收集数据那样快速地获取数据。

## 十五、请说明如果首选的副本不在ISR中会发生什么?

如果首选的副本不在ISR中，控制器将无法将leadership转移到首选的副本。

## 十六、如何保证 Kafka 的消息有序（☆☆☆☆☆）？

Kafka 对于消息的重复、丢失、错误以及顺序没有严格的要求。Kafka 只能保证一个partition 中的消息被某个consumer 消费时是顺序的，事实上，从Topic角度来说，当有多个 partition 时，消息仍然不是全局有序的。

## 十七、有可能在生产后发生消息偏移吗?

- 在大多数队列系统中，作为生产者的类无法做到这一点，它的作用是触发并忘记消息。broker将完成剩下的工作，比如使用id进行适当的元数据处理、偏移量等。
- 作为消息的用户，你可以从Kafka broker中获得补偿。如果你注视SimpleConsumer类，你会注意到它会获取包括偏移量作为列表的MultiFetchResponse对象。此外，当你对Kafka消息进行迭代时，你会拥有包括偏移量和消息发送的MessageAndOffset对象。

## 十八、kafka主要特征

- kafka具有近乎实时性的消息处理能力，面对海量数据，高效的存储消息和查询消息。kafka将消息保存在磁盘中，以顺序读写的方式访问磁盘，从而避免了随机读写磁盘导致的性能瓶颈
- kafka支持批量读写消息，并且对消息批量压缩，提高了网络利用率和压缩效率
- kafka支持消息分区，每个分区中的消息保证顺序传输，而分区之间可以并发操作，提高了kafka的并发能力
- kafka支持在线增加分区，支持在线水平扩展
- kafka支持为每个分区创建多个副本，其中只会有一个leader副本负责读写，其他副本只负责与leader副本同步，这种方式提高了数据的容灾能力，kafka会将leader副本均匀的分布在集群中的服务器上，实现性能最大化

## 十九、列举kafka的应用场景

> - **日志收集：一个公司可以用Kafka可以收集各种服务的log，通过kafka以统一接口服务的方式开放给各种consumer，例如Hadoop、Hbase、Solr等**
> - **消息系统：解耦和生产者和消费者、缓存消息等**
> - **用户活动跟踪：Kafka经常被用来记录web用户或者app用户的各种活动，如浏览网页、搜索、点击等活动，这些活动信息被各个服务器发布到kafka的topic中，然后订阅者通过订阅这些topic来做实时的监控分析，或者装载到Hadoop、数据仓库中做离线分析和挖掘**
> - **运营指标：Kafka也经常用来记录运营监控数据。包括收集各种分布式应用的数据，生产各种操作的集中反馈，比如报警和报告**
> - **流式处理：比如spark streaming和storm**
> - **事件源**

## 二十、kafka主题分区的作用

kafka的每个topic都可以分为多个partition，每个partition都有多个replica（副本），每个分区中的消息是不同的，提高了并发读写的能力，而同一分区的不同副本中保存的是相同的消息，副本之间是一主多从关系，其中leader副本处理读写请求，follower副本只与leader副本进行消息同步，当leader副本出现故障时，则从follower副本中重新选举leader副本对外提供服务。这样，通过提高分区的数量，就可以实现水平扩展，通过提高副本数量，就可以提高容灾能力

## 二十一、consumer水平扩展如何实现

kafka支持consumer水平扩展，可以让多个consumer加入一个consumer group，在一个consumer group中，每个分区只能分配给一个consumer，当kafka服务端增加分区数量进行水平扩展后，可以向consumer group中增加新的consumer来提高整个consumer group的消费能力，当consumer group 中的一个consumer出现故障下线时，会通过rebalance操作下线consumer，它负责处理的分区将分配给其他consumer

## 二十二、消息的顺序

kafka保证一个partition内消息是有序的，但是并不保证多个partition之间的数据有顺序，每个topic可以划分成多个分区，同一个topic下的不同分区包含的消息是不同的，每个消息在被添加到分区时，都会被分配一个offset，它是此消息在分区中的唯一编号，kafka通过offset保证消息在分区内的顺序，offset顺序不跨分区，即kafka只保证在同一个分区内的消息是有序的

## 二十三、为了避免磁盘被占满，kafka会周期性的删除陈旧的消息，删除策略是什么?

- 一种是根据消息保留的时间
- 一种是根据topic存储的数据大小

## 二十四、什么是日志压缩

在很多场景中，消息的key与value之间的对应关系是不断变化的，消费者只关心key对应的最新value，此时，可以开启kafka的日志压缩功能，kafka会在后台启动一个线程，定期将相同key的消息进行合并，只保留最新的value值。

## 二十五、同一分区的多个副本包括的消息是否一致？

每个副本中包含的消息是一样的，但是再同一时刻，副本之间并不是完全一样的。

## 二十六、数据传输的事物定义有哪三种？

数据传输的事务定义通常有以下三种级别：
（1）最多一次：消息不会被重复发送，最多被传输一次，但也有可能一次不传输
（2）最少一次：消息不会被漏发送，最少被传输一次，但也有可能被重复传输.
（3）精确的一次（Exactly once）：不会漏传输也不会重复传输,每个消息都传输被一次而且仅仅被传输一次，这是大家所期望的

## 二十七、Kafka判断一个节点是否还活着有哪两个条件？

- 节点必须可以维护和ZooKeeper的连接，Zookeeper通过心跳机制检查每个节点的连接
- 如果节点是个follower,他必须能及时的同步leader的写操作，延时不能太久

## 二十八、producer是否直接将数据发送到broker的leader(主节点)？

producer直接将数据发送到broker的leader(主节点)，不需要在多个节点进行分发，为了帮助producer做到这点，所有的Kafka节点都可以及时的告知：哪些节点是活动的，目标topic目标分区的leader在哪。这样producer就可以直接将消息发送到目的地了。

## 二十九、Kafa consumer是否可以消费指定分区消息？

Kafa consumer消费消息时，向broker发出"fetch"请求去消费特定分区的消息，consumer指定消息在日志中的偏移量（offset），就可以消费从这个位置开始的消息，customer拥有了offset的控制权，可以向后回滚去重新消费之前的消息，这是很有意义的。

## 三十、Kafka消息是采用Pull模式，还是Push模式？

- Kafka最初考虑的问题是，customer应该从brokes拉取消息还是brokers将消息推送到consumer，也就是pull还push。在这方面，Kafka遵循了一种大部分消息系统共同的传统的设计：producer将消息推送到broker，consumer从broker拉取消息
- 一些消息系统比如Scribe和Apache Flume采用了push模式，将消息推送到下游的consumer。这样做有好处也有坏处：由broker决定消息推送的速率，对于不同消费速率的consumer就不太好处理了。消息系统都致力于让consumer以最大的速率最快速的消费消息，但不幸的是，push模式下，当broker推送的速率远大于consumer消费的速率时，consumer恐怕就要崩溃了。**最终Kafka还是选取了传统的pull模式**
- Pull模式的另外一个好处是consumer可以自主决定是否批量的从broker拉取数据。Push模式必须在不知道下游consumer消费能力和消费策略的情况下决定是立即推送每条消息还是缓存之后批量推送。如果为了避免consumer崩溃而采用较低的推送速率，将可能导致一次只推送较少的消息而造成浪费。Pull模式下，consumer就可以根据自己的消费能力去决定这些策略
- Pull有个缺点是，如果broker没有可供消费的消息，将导致consumer不断在循环中轮询，直到新消息到t达。为了避免这点，Kafka有个参数可以让consumer阻塞知道新消息到达(当然也可以阻塞知道消息的数量达到某个特定的量这样就可以批量发

## 三十一、Kafka存储在硬盘上的消息格式是什么？

消息由一个固定长度的头部和可变长度的字节数组组成。头部包含了一个版本号和CRC32校验码。

- 消息长度: 4 bytes (value: 1+4+n)
- 版本号: 1 byte
- CRC校验码: 4 bytes
- 具体的消息: n bytes

## 三十二、Kafka高效文件存储设计特点

Kafka把topic中一个parition大文件分成多个小文件段，通过多个小文件段，就容易定期清除或删除已经消费完文件，减少磁盘占用。

通过索引信息可以快速定位message和确定response的最大大小。

通过index元数据全部映射到memory，可以避免segment file的IO磁盘操作。

通过索引文件稀疏存储，可以大幅降低index文件元数据占用空间大小。

## 三十三、Kafka 与传统消息系统之间有三个关键区别

- Kafka 持久化日志，这些日志可以被重复读取和无限期保留
- Kafka 是一个分布式系统：它以集群的方式运行，可以灵活伸缩，在内部通过复制数据提升容错能力和高可用性
- Kafka 支持实时的流式处理

## 三十四、Kafka创建Topic时如何将分区放置到不同的Broker中

- 副本因子不能大于 Broker 的个数；
- 第一个分区（编号为0）的第一个副本放置位置是随机从 brokerList 选择的；
- 其他分区的第一个副本放置位置相对于第0个分区依次往后移。也就是如果我们有5个 Broker，5个分区，假设第一个分区放在第四个 Broker 上，那么第二个分区将会放在第五个 Broker 上；第三个分区将会放在第一个 Broker 上；第四个分区将会放在第二个 Broker 上，依次类推；
- 剩余的副本相对于第一个副本放置位置其实是由 nextReplicaShift 决定的，而这个数也是随机产生的

## 三十五、Kafka新建的分区会在哪个目录下创建

- 在启动 Kafka 集群之前，我们需要配置好 log.dirs 参数，其值是 Kafka 数据的存放目录，这个参数可以配置多个目录，目录之间使用逗号分隔，通常这些目录是分布在不同的磁盘上用于提高读写性能。
- 当然我们也可以配置 log.dir 参数，含义一样。只需要设置其中一个即可。
- 如果 log.dirs 参数只配置了一个目录，那么分配到各个 Broker 上的分区肯定只能在这个目录下创建文件夹用于存放数据。
- 但是如果 log.dirs 参数配置了多个目录，那么 Kafka 会在哪个文件夹中创建分区目录呢？答案是：Kafka 会在含有分区目录最少的文件夹中创建新的分区目录，分区目录名为 Topic名+分区ID。注意，是分区文件夹总数最少的目录，而不是磁盘使用量最少的目录！也就是说，如果你给 log.dirs 参数新增了一个新的磁盘，新的分区目录肯定是先在这个新的磁盘上创建直到这个新的磁盘目录拥有的分区目录不是最少为止。

## 三十六、partition的数据如何保存到硬盘

- topic中的多个partition以文件夹的形式保存到broker，每个分区序号从0递增，且消息有序
- Partition文件下有多个segment（xxx.index，xxx.log）
- segment 文件里的 大小和配置文件大小一致可以根据要求修改 默认为1g
- 如果大小大于1g时，会滚动一个新的segment并且以上一个segment最后一条消息的偏移量命名

## 三十七、kafka的ack机制

- request.required.acks有三个值 0、1、-1：
  - 0：生产者不会等待broker的ack，这个延迟最低但是存储的保证最弱当server挂掉的时候就会丢数据
  - 1：服务端会等待ack值 leader副本确认接收到消息后发送ack但是如果leader挂掉后他不确保是否复制完成新leader也会导致数据丢失
  - -1：同样在1的基础上 服务端会等所有的follower的副本受到数据后才会受到leader发出的ack，这样数据不会丢失

## 三十八、Kafka的消费者如何消费数据

- 消费者每次消费数据的时候，消费者都会记录消费的物理偏移量（offset）的位置
- 等到下次消费时，他会接着上次位置继续消费

## 三十八、kafka 的消费者方式？

- consumer 采用 pull（拉）模式从 broker 中读取数据。
  push（推）模式很难适应消费速率不同的消费者，因为消息发送速率是由 broker 决定的。它的目标是尽可能以最快速度传递消息，但是这样很容易造成 consumer 来不及处理消息，典型的表现就是拒绝服务以及网络拥塞。而 pull 模式则可以根据 consumer 的消费能力以适当的速率消费消息。
- 对于 Kafka 而言，pull 模式更合适，它可简化 broker 的设计，consumer 可自主控制消费消息的速率，同时 consumer 可以自己控制消费方式——即可批量消费也可逐条消费，同时还能选择不同的提交方式从而实现不同的传输语义。
  pull 模式不足之处是，如果 kafka 没有数据，消费者可能会陷入循环中，一直等待数据到达。为了避免这种情况，我们在我们的拉请求中有参数，允许消费者请求在等待数据到达的“长轮询”中进行阻塞。

## 三十九、消费者负载均衡策略

一个消费者组中的一个分片对应一个消费者成员，他能保证每个消费者成员都能访问，如果组中成员太多会有空闲的成员。

## 四十、数据有序

- 一个消费者组里它的内部是有序的
- 消费者组与消费者组之间是无序的

## 四十一、kafaka生产数据时数据的分组策略

- 生产者决定数据产生到集群的哪个partition中
- 每一条消息都是以（key，value）格式
- Key是由生产者发送数据传入
- 所以生产者（key）决定了数据产生到集群的哪个partition

## 四十二、kafka 数据丢失问题，及如何保证？

kafka的ack机制：在kafka发送数据的时候，每次发送消息都会有一个确认反馈机制，确保消息正常的能够被收到。

**1）数据丢失**
acks=1 的时候(只保证写入 leader 成功)，如果刚好 leader 挂了。数据会丢失。
acks=0 的时候，使用异步模式的时候，该模式下 kafka 无法保证消息，有可能会丢

**2）brocker 如何保证不丢失**
acks=all : 所有副本都写入成功并确认。
retries = 一个合理值。
min.insync.replicas=2 消息至少要被写入到这么多副本才算成功。
unclean.leader.election.enable=false 关闭 unclean leader 选举，即不允许非 ISR 中的副本被
选举为 leader，以避免数据丢失。

**3）Consumer 如何保证不丢失**
如果在消息处理完成前就提交了 offset，那么就有可能造成数据的丢失。
enable.auto.commit=false 关闭自动提交 offset。处理完数据之后手动提交。