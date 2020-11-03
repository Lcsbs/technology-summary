## Kafka

### 1. Kafka如何保证高可用？

通过副本机制保证高可用，Kafka的分区是多副本的，如果一个副本丢失了，那么还可以从其他副本中获取分区数据。

① 分区和副本

Kafka的主题被分为多个分区，分区是Kafka最基本的存储单位。每个分区可以有多个副本（可以在创建主题时使用 replication-factor 参数进行指定）。其中一个副本是首领副本（Leader replica），所有的事件都直接发送给首领副本；其他副本是跟随副本（Follower replica），需要通过复制来保持与首领副本数据一致，当首领副本不可用时，其中一个跟随者副本将成为新首领。

② ISR机制

每个分区都有一个 ISR(in-sync Replica) 列表，用于维护所有同步的、可用的副本。首领副本必然是同步副本，而对于跟随者副本来说，它需要满足以下条件才能被认为是同步副本：

- 与 Zookeeper 之间有一个活跃的会话，即必须定时向 Zookeeper 发送心跳；
- 在规定的时间内从首领副本那里低延迟地获取过消息。

如果副本不满足上面条件的话，就会被从 ISR 列表中移除，直到满足条件才会被再次加入。

这里给出一个主题创建的示例：使用 `--replication-factor` 指定副本系数为 3，创建成功后使用 `--describe ` 命令可以看到分区 0 的有 0,1,2 三个副本，且三个副本都在 ISR 列表中，其中 1 为首领副本。

![img](https://gitee.com/heibaiying/BigData-Notes/raw/master/pictures/kafka-%E5%88%86%E5%8C%BA%E5%89%AF%E6%9C%AC.png)

3. 不完全首领选举

对于副本机制，在 broker 级别有一个可选的配置参数 unclean.leader.election.enable，默认值为 fasle，代表禁止不完全的首领选举。这是针对当首领副本挂掉且 ISR 中没有其他可用副本时，是否允许某个不完全同步的副本成为首领副本，这可能会导致数据丢失或者数据不一致，在某些对数据一致性要求较高的场景 (如金融领域)，这可能无法容忍的，所以其默认值为 false，如果你能够允许部分数据不一致的话，可以配置为 true。

④ 最少同步副本

ISR 机制的另外一个相关参数是 min.insync.replicas , 可以在 broker 或者主题级别进行配置，代表 ISR 列表中至少要有几个可用副本。这里假设设置为 2，那么当可用副本数量小于该值时，就认为整个分区处于不可用状态。此时客户端再向分区写入数据时候就会抛出异常 org.apache.kafka.common.errors.NotEnoughReplicasExceptoin: Messages are rejected since there are fewer in-sync replicas than required。

5. 发送确认

Kafka 在生产者上有一个可选的参数 ack，该参数指定了必须要有多少个分区副本收到消息，生产者才会认为消息写入成功：

acks=0 ：消息发送出去就认为已经成功了，不会等待任何来自服务器的响应；
acks=1 ： 只要集群的首领节点收到消息，生产者就会收到一个来自服务器成功响应；
acks=all ：只有当所有参与复制的节点全部收到消息时，生产者才会收到一个来自服务器的成功响应。

### 2. Kafka 如何保证不丢失数据？

kafka的ack机制：在kafka发送数据的时候，每次发送消息都会有一个发送确认反馈机制，确保消息正常的能够被收到。

1）数据丢失 acks=1 的时候(只保证写入 leader 成功)，如果刚好 leader 挂了。数据会丢失。 acks=0 的时候，使用异步模式的时候，该模式下 kafka 无法保证消息，有可能会丢

2）brocker 如何保证不丢失 acks=all : 所有副本都写入成功并确认。 retries = 一个合理值。 min.insync.replicas=2 消息至少要被写入到这么多副本才算成功。 unclean.leader.election.enable=false 关闭 unclean leader 选举，即不允许非 ISR 中的副本被 选举为 leader，以避免数据丢失。

3）Consumer 如何保证不丢失 如果在消息处理完成前就提交了 offset，那么就有可能造成数据的丢失。 enable.auto.commit=false 关闭自动提交 offset。处理完数据之后手动提交。

Kafka实现：1. 高可用，副本机制；2. 高吞吐率，单个borker可处理千个分区及每秒百万级别的消息量；

