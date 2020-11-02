### 文章目录

- - [一、Hbase是什么？](https://blog.csdn.net/BeiisBei/article/details/104129280#Hbase_1)
  - [二、HBase 的特点是什么？](https://blog.csdn.net/BeiisBei/article/details/104129280#HBase__7)
  - [三、HBase 和 Hive 的区别？](https://blog.csdn.net/BeiisBei/article/details/104129280#HBase__Hive__19)
  - - [1）两者是什么？](https://blog.csdn.net/BeiisBei/article/details/104129280#1_22)
    - [2）两者的特点](https://blog.csdn.net/BeiisBei/article/details/104129280#2_28)
    - [3）两者的限制](https://blog.csdn.net/BeiisBei/article/details/104129280#3_34)
    - [4）两者的应用场景](https://blog.csdn.net/BeiisBei/article/details/104129280#4_41)
    - [5）总结](https://blog.csdn.net/BeiisBei/article/details/104129280#5_47)
  - [四、HBase 适用于怎样的情景？](https://blog.csdn.net/BeiisBei/article/details/104129280#HBase__50)
  - [五、描述 HBase 的 rowKey 的设计原则？](https://blog.csdn.net/BeiisBei/article/details/104129280#_HBase__rowKey__59)
  - - [1）Rowkey 长度原则](https://blog.csdn.net/BeiisBei/article/details/104129280#1Rowkey__61)
    - [2）Rowkey 散列原则](https://blog.csdn.net/BeiisBei/article/details/104129280#2Rowkey__76)
    - [3）Rowkey 唯一原则](https://blog.csdn.net/BeiisBei/article/details/104129280#3Rowkey__80)
  - [六、描述HBase 中scan 和get 的功能以及实现的异同？](https://blog.csdn.net/BeiisBei/article/details/104129280#HBase_scan_get__84)
  - [七、hbase如何导入数据？](https://blog.csdn.net/BeiisBei/article/details/104129280#hbase_99)
  - [八、hbase 的存储结构？](https://blog.csdn.net/BeiisBei/article/details/104129280#hbase__105)
  - [九、解释下 hbase 实时查询的原理](https://blog.csdn.net/BeiisBei/article/details/104129280#_hbase__108)
  - [十、详细描述 HBase 中一个 cell 的结构？](https://blog.csdn.net/BeiisBei/article/details/104129280#_HBase__cell__111)
  - [十一、简述 HBase 中 compact 用途是什么，什么时候触发，分为哪两种，有什么区别，有哪些相关配置参数？（☆☆☆☆☆）](https://blog.csdn.net/BeiisBei/article/details/104129280#_HBase__compact__114)
  - [十二、HBase 中实现了两种 compaction 的方式：minor and major. 这两种 compaction 方式的 区别是：](https://blog.csdn.net/BeiisBei/article/details/104129280#HBase__compaction_minor_and_major__compaction___123)
  - [十三、简述 Hbase filter 的实现原理是什么？结合实际项目经验，写出几个使用filter 的场景。](https://blog.csdn.net/BeiisBei/article/details/104129280#_Hbase_filter_filter__130)
  - [十四、Hbase 内部是什么机制？](https://blog.csdn.net/BeiisBei/article/details/104129280#Hbase__136)
  - [十五、HBase 宕机如何处理？](https://blog.csdn.net/BeiisBei/article/details/104129280#HBase__154)
  - [十六、HRegionServer宕机如何处理？](https://blog.csdn.net/BeiisBei/article/details/104129280#HRegionServer_161)
  - [十七、hbase写数据 和 读数据过程](https://blog.csdn.net/BeiisBei/article/details/104129280#hbase___172)
  - [十八、HBase优化方法](https://blog.csdn.net/BeiisBei/article/details/104129280#HBase_198)
  - - [1）减少调整](https://blog.csdn.net/BeiisBei/article/details/104129280#1_201)
    - [2）减少启停](https://blog.csdn.net/BeiisBei/article/details/104129280#2_211)
    - [3）减少数据量](https://blog.csdn.net/BeiisBei/article/details/104129280#3_222)
    - [4）合理设计](https://blog.csdn.net/BeiisBei/article/details/104129280#4_230)
  - [十九、为什么不建议在 HBase 中使用过多的列族](https://blog.csdn.net/BeiisBei/article/details/104129280#_HBase__255)
  - [二十、Region 如何预建分区？](https://blog.csdn.net/BeiisBei/article/details/104129280#Region__266)
  - [二十一、如何提高 HBase 客户端的读写性能？请举例说明（☆☆☆☆☆）](https://blog.csdn.net/BeiisBei/article/details/104129280#_HBase__280)
  - [二十一、直接将时间戳作为行健，在写入单个 region 时候会发生热点问题，为什么呢？（☆☆☆☆☆）](https://blog.csdn.net/BeiisBei/article/details/104129280#_region__288)
  - [二十二、请描述如何解决 HBase 中 region 太小和 region 太大带来的冲突？](https://blog.csdn.net/BeiisBei/article/details/104129280#_HBase__region__region__294)
  - [二十三、解释一下布隆过滤器原理（☆☆☆☆☆）？](https://blog.csdn.net/BeiisBei/article/details/104129280#_298)
  - [二十四、HBase与传统关系型数据库(如MySQL)的区别](https://blog.csdn.net/BeiisBei/article/details/104129280#HBaseMySQL_316)
  - [二十五、另外的一些面试题](https://blog.csdn.net/BeiisBei/article/details/104129280#_326)



## 一、Hbase是什么？

1. Hbase一个分布式的基于列式存储的数据库，基于Hadoop的 hdfs 存储，zookeeper 进行管理。
2. Hbase适合存储半结构化或非结构化数据，对于数据结构字段不够确定或者杂乱无章很难按一个概念去抽取的数据。
3. Hbase 为 null 的记录不会被存储。
4. 基于的表包含 rowkey，时间戳，和列族。新写入数据时，时间戳更新， 同时可以查询到以前的版本。
5. hbase 是主从架构。hmaster 作为主节点，hregionserver 作为从节点。

## 二、HBase 的特点是什么？

```sql
1） 大：一个表可以有数十亿行，上百万列；
2） 无模式：每行都有一个可排序的主键和任意多的列，列可以根据需要动态的增加，同一张表中不
同的行可以有截然不同的列；
3） 面向列：面向列（族）的存储和权限控制，列（族）独立检索；
4） 稀疏：空（null）列并不占用存储空间，表可以设计的非常稀疏；
5） 数据多版本：每个单元中的数据可以有多个版本，默认情况下版本号自动分配，是单元格插入时
的时间戳；
6） 数据类型单一：Hbase 中的数据都是字符串，没有类型。
12345678
```

## 三、HBase 和 Hive 的区别？

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200131230533568.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

### 1）两者是什么？

Apache Hive 是一个构建在Hadoop 基础设施之上的数据仓库。通过Hive 可以使用HQL语言查询存放在HDFS 上的数据。HQL 是一种类SQL 语言， 这种语言最终被转化为Map/Reduce。虽然Hive 提供了SQL 查询功能，但是Hive 不能够进行交互查询–因为它只能够在Haoop 上批量的执行Hadoop。

Apache HBase 是一种Key/Value 系统，它运行在HDFS 之上。和Hive 不一样，Hbase 的能够在它的数据库上实时运行，而不是运行MapReduce 任务。Hbase 被分区为表格，表格又被进一步分割为列簇。列簇必须使用schema 定义，列簇将某一类型列集合起来（列不要求schema 定义）。例如，“message”列簇可能包含：“to”, ”from” “date”, “subject”,和”body”. 每一个key/value 对在Hbase 中被定义为一个cell，每一个key 由row-key，列簇、列和时间戳。在Hbase 中，行是key/value 映射的集合，这个映射通过row-key 来唯一标识。Hbase 利用Hadoop 的基础设施，可以利用通用的设备进行水平的扩展。

### 2）两者的特点

Hive 帮助熟悉SQL 的人运行MapReduce 任务。因为它是JDBC 兼容的，同时，它也能够和现存的SQL 工具整合在一起。运行Hive 查询会花费很长时间，因为它会默认遍历表中所有的数据。虽然有这样的缺点，一次遍历的数据量可以通过Hive 的分区机制来控制。分区允许在数据集上运行过滤查询，这些数据集存储在不同的文件夹内，查询的时候只遍历指定文件夹（分区）中的数据。这种机制可以用来，例如，只处理在某一个时间范围内的文件， 只要这些文件名中包括了时间格式。

HBase 通过存储key/value 来工作。它支持四种主要的操作：增加或者更新行，查看一个范围内的cell，获取指定的行，删除指定的行、列或者是列的版本。版本信息用来获取历史数据（每一行的历史数据可以被删除，然后通过Hbase compactions 就可以释放出空间）。虽然HBase 包括表格，但是schema 仅仅被表格和列簇所要求，列不需要schema。Hbase 的表格包括增加/计数功能。

### 3）两者的限制

Hive 目前不支持更新操作。另外，由于hive 在hadoop 上运行批量操作，它需要花费很长的时间，通常是几分钟到几个小时才可以获取到查询的结果。Hive 必须提供预先定义好的schema 将
文件和目录映射到列，并且Hive 与ACID 不兼容。

HBase 查询是通过特定的语言来编写的，这种语言需要重新学习。类SQL 的功能可以通过Apache Phonenix 实现，但这是以必须提供schema 为代价的。另外，Hbase 也并不是兼容所有的ACID 特性，虽然它支持某些特性。最后但不是最重要的–为了运行Hbase，Zookeeper 是必须的，zookeeper 是一个用来进行分布式协调的服务，这些服务包括配置服务，维护元信息和命名空间服务。

### 4）两者的应用场景

Hive 适合用来对一段时间内的数据进行分析查询，例如，用来计算趋势或者网站的日志。
Hive 不应该用来进行实时的查询。因为它需要很长时间才可以返回结果。
Hbase 非常适合用来进行大数据的实时查询。Facebook 用Hbase 进行消息和实时的分析。它也可以用来统计Facebook 的连接数。

### 5）总结

Hive 和Hbase 是两种基于Hadoop 的不同技术–Hive 是一种类SQL 的引擎，并且运 MapReduce 任务，Hbase 是一种在Hadoop 之上的NoSQL 的Key/vale 数据库。当然，这两种工具是可以同时使用的。就像用Google 来搜索，用FaceBook 进行社交一样，Hive 可以用来进行统计查询，HBase 可以用来进行实时查询，数据也可以从Hive 写到Hbase，设置再从Hbase写回Hive。

## 四、HBase 适用于怎样的情景？

① 半结构化或非结构化数据

② 记录非常稀疏

③ 多版本数据

④ 超大数据量

## 五、描述 HBase 的 rowKey 的设计原则？

### 1）Rowkey 长度原则

Rowkey 是一个二进制码流，Rowkey 的长度被很多开发者建议说设计在10~100 个字节，不过建议是越短越好，不要超过16 个字节。原因如下：

```sql
1） 大：一个表可以有数十亿行，上百万列；
2） 无模式：每行都有一个可排序的主键和任意多的列，列可以根据需要动态的增加，同一张表中不
同的行可以有截然不同的列；
3） 面向列：面向列（族）的存储和权限控制，列（族）独立检索；
4） 稀疏：空（null）列并不占用存储空间，表可以设计的非常稀疏；
5） 数据多版本：每个单元中的数据可以有多个版本，默认情况下版本号自动分配，是单元格插入时
的时间戳；
6） 数据类型单一：Hbase 中的数据都是字符串，没有类型。
12345678
```

### 2）Rowkey 散列原则

如果Rowkey 是按时间戳的方式递增，不要将时间放在二进制码的前面，建议将Rowkey 的高位作为散列字段，由程序循环生成，低位放时间字段，这样将提高数据均衡分布在每个Regionserver 实现负载均衡的几率。如果没有散列字段，首字段直接是时间信息将产生所有新数据都在一个RegionServer上堆积的热点现象，这样在做数据检索的时候负载将会集中在个别RegionServer，降低查询效率。

### 3）Rowkey 唯一原则

必须在设计上保证其唯一性。

## 六、描述HBase 中scan 和get 的功能以及实现的异同？

**HBase 的查询实现只提供两种方式：
1） 按指定RowKey 获取唯一一条记录**
get 方法（org.apache.hadoop.hbase.client.Get）
Get 的方法处理分两种: 设置了ClosestRowBefore 和没有设置ClosestRowBefore 的rowlock。主要是用来保证行的事务性，即每个get 是以一个row 来标记的。一个row 中可以有很多family和column。
**2） 按指定的条件获取一批记录**
scan 方法(org.apache.Hadoop.hbase.client.Scan）实现条件查询功能

```sql
使用的就是scan 方式。
（1）scan 可以通过setCaching 与setBatch 方法提高速度(以空间换时间)；
（2）scan 可以通过setStartRow 与setEndRow 来限定范围([start，end)start 是闭区间，end 是开区
间)。范围越小，性能越高。
（3）scan 可以通过setFilter 方法添加过滤器，这也是分页、多条件查询的基础。
12345
```

## 七、hbase如何导入数据？

- 通过HBase API进行批量写入数据；
- 使用Sqoop工具批量导数到HBase集群；
- 使用MapReduce批量导入；
- HBase BulkLoad的方式。

## 八、hbase 的存储结构？

Hbase 中的每张表都通过行键 (rowkey) 按照一定的范围被分割成多个子表（HRegion），默认一个 HRegion 超过 256M 就要被分割成两个，由 HRegionServer 管理，管理哪些 HRegion 由 Hmaster 分配。 HRegion 存取一个子表时，会创建一个 HRegion 对象，然后对表的每个列族 （Column Family） 创建一个 store 实例， 每个 store 都会有 0个或多个 StoreFile 与之对应，每个 StoreFile 都会对应一个 HFile ， HFile 就是实际的存储文件，因此，一个 HRegion 还拥有一个 MemStore 实例。

## 九、解释下 hbase 实时查询的原理

实时查询，可以认为是从内存中查询，一般响应时间在 1 秒内。HBase 的机制是数据先写入到内存中，当数据量达到一定的量（如 128M），再写入磁盘中， 在内存中，是不进行数据的更新或合并操作的，只增加数据，这使得用户的写操作只要进入内存中就可以立即返回，保证了 HBase I/O 的高性能。

## 十、详细描述 HBase 中一个 cell 的结构？

HBase 中通过 row 和 columns 确定的为一个存贮单元称为 cell。Cell：由{row key， column(= + )， version}是唯一确定的单元cell 中的数据是没有类型的，全部是字节码形式存贮。

## 十一、简述 HBase 中 compact 用途是什么，什么时候触发，分为哪两种，有什么区别，有哪些相关配置参数？（☆☆☆☆☆）

在 hbase 中每当有 memstore 数据 flush 到磁盘之后，就形成一个 storefile，当 storeFile的数量达到一定程度后，就需要将 storefile 文件来进行 compaction 操作。
Compact 的作用：

**① 合并文件
② 清除过期，多余版本的数据
③ 提高读写数据的效率**

## 十二、HBase 中实现了两种 compaction 的方式：minor and major. 这两种 compaction 方式的 区别是：

1、Minor 操作只用来做部分文件的合并操作以及包括 minVersion=0 并且设置 ttl 的过
期版本清理，不做任何删除数据、多版本数据的清理工作。
2、Major 操作是对 Region 下的 HStore 下的所有 StoreFile 执行合并操作，最终的结果
是整理合并出一个文件。

## 十三、简述 Hbase filter 的实现原理是什么？结合实际项目经验，写出几个使用filter 的场景。

HBase 为筛选数据提供了一组过滤器，通过这个过滤器可以在 HBase 中的数据的多个维度（行，列，数据版本）上进行对数据的筛选操作，也就是说过滤器最终能够筛选的数据能够细化到具体的一个存储单元格上（由行键， 列名，时间戳定位）。

RowFilter、PrefixFilter。hbase 的 filter 是通过 scan 设置的，所以是基于 scan 的查询结果进行过滤. 过滤器的类型很多，但是可以分为两大类——比较过滤器，专用过滤器。过滤器的作用是在服务端判断数据是否满足条件，然后只将满足条件的数据返回给客户端；如在进行订单开发的时候，我们使用 rowkeyfilter 过滤出某个用户的所有订单。

## 十四、Hbase 内部是什么机制？

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200201010635975.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0JlaWlzQmVp,size_16,color_FFFFFF,t_70#pic_center)

> Hbase 是一个能适应联机业务的数据库系统物理存储：hbase 的持久化数据是将数据存储在 HDFS 上。
> 存储管理：一个表是划分为很多 region 的，这些 region 分布式地存放在很多 regionserver上 Region 内部还可以划分为 store，store 内部有 memstore 和 storefile。
> 版本管理：hbase 中的数据更新本质上是不断追加新的版本，通过 compact 操作来做版本间的文件合并 Region 的 split。
> 集群管理：ZooKeeper + HMaster + HRegionServer。

在 HBase 中无论是增加新行还是修改已有的行，其内部流程都是相同的。HBase 接到命令后存下变化信息，或者写入失败抛出异常。默认情况下，执行写入时会写到两个地方：预写式日志（write-ahead log，也称 HLog）和 MemStore。HBase 的默认方式是把写入动作记录在这两个地方，以保证数据持久化。只有当这两个地方的变化信息都写入并确认后，才认为写动作完成。

MemStore 是内存里的写入缓冲区，HBase 中数据在永久写入硬盘之前在这里累积。当MemStore 填满后，其中的数据会刷写到硬盘，生成一个HFile。HFile 是HBase 使用的底层存储格式。HFile 对应于列族，一个列族可以有多个 HFile，但一个 HFile 不能存储多个列族的数据。在集群的每个节点上，每个列族有一个MemStore。大型分布式系统中硬件故障很常见，HBase 也不例外。

设想一下，如果MemStore 还没有刷写，服务器就崩溃了，内存中没有写入硬盘的数据就会丢失。HBase 的应对办法是在写动作完成之前先写入 WAL。HBase 集群中每台服务器维护一个 WAL 来记录发生的变化。WAL 是底层文件系统上的一个文件。直到WAL 新记录成功写入后，写动作才被认为成功完成。这可以保证 HBase 和支撑它的文件系统满足持久性。

大多数情况下，HBase 使用Hadoop分布式文件系统（HDFS）来作为底层文件系统。如果 HBase 服务器宕机，没有从 MemStore 里刷写到 HFile 的数据将可以通过回放 WAL 来恢复。你不需要手工执行。Hbase 的内部机制中有恢复流程部分来处理。每台 HBase 服务器有一个 WAL，这台服务器上的所有表（和它们的列族）共享这个 WAL。你可能想到，写入时跳过 WAL 应该会提升写性能。但我们不建议禁用 WAL， 除非你愿意在出问题时丢失数据。如果你想测试一下，如下代码可以禁用 WAL： 注意：不写入 WAL 会在 RegionServer 故障时增加丢失数据的风险。关闭 WAL， 出现故障时 HBase 可能无法恢复数据，没有刷写到硬盘的所有写入数据都会丢失。

## 十五、HBase 宕机如何处理？

宕机分为 HMaster 宕机和 HRegisoner 宕机.

如果是 HRegisoner 宕机，HMaster 会将其所管理的 region 重新分布到其他活动的 RegionServer 上，由于数据和日志都持久在 HDFS 中，该操作不会导致数据丢失,所以数据的一致性和安全性是有保障的。

如果是 HMaster 宕机， HMaster 没有单点问题， HBase 中可以启动多个HMaster，通过 Zookeeper 的 Master Election 机制保证总有一个 Master 运行。即ZooKeeper 会保证总会有一个 HMaster 在对外提供服务。

## 十六、HRegionServer宕机如何处理？

- ZooKeeper 会监控 HRegionServer 的上下线情况，当 ZK 发现某个 HRegionServer 宕机之后会通知 HMaster 进行失效备援
- HRegionServer 会停止对外提供服务，就是它所负责的 region 暂时停止对外提供服务
- HMaster 会将该 HRegionServer 所负责的 region 转移到其他 HRegionServer 上，并且会对 HRegionServer 上存在 memstore 中还未持久化到磁盘中的数据进行恢复
- 这个恢复的工作是由 **WAL重播** 来完成，这个过程如下：

> - wal实际上就是一个文件，存在/hbase/WAL/对应RegionServer路径下
> - 宕机发生时，读取该RegionServer所对应的路径下的wal文件，然后根据不同的region切分成不同的临时文件recover.edits
> - 当region被分配到新的RegionServer中，RegionServer读取region时会进行是否存在recover.edits，如果有则进行恢复

## 十七、hbase写数据 和 读数据过程

**获取region存储位置信息**

写数据和读数据一般都会获取hbase的region的位置信息。大概步骤为：

1. 从zookeeper中获取.ROOT.表的位置信息，在zookeeper的存储位置为/hbase/root-region-server；
2. 根据.ROOT.表中信息，获取.META.表的位置信息；
3. META.表中存储的数据为每一个region存储位置；

**向hbase表中插入数据**

hbase中缓存分为两层：Memstore 和 BlockCache：

1. 首先写入到 WAL文件 中，目的是为了数据不丢失；
2. 再把数据插入到 Memstore缓存中，当 Memstore达到设置大小阈值时，会进行flush进程；
3. flush过程中，需要获取每一个region存储的位置。

**从hbase中读取数据**

BlockCache 主要提供给读使用。读请求先到 Memtore中查数据，查不到就到 BlockCache 中查，再查不到就会到磁盘上读，并把读的结果放入 BlockCache 。

BlockCache 采用的算法为 LRU（最近最少使用算法），因此当 BlockCache 达到上限后，会启动淘汰机制，淘汰掉最老的一批数据。

一个 RegionServer 上有一个 BlockCache 和N个 Memstore，它们的大小之和不能大于等于 heapsize * 0.8，否则 hbase 不能启动。默认 BlockCache 为 0.2，而 Memstore 为 0.4。对于注重读响应时间的系统，应该将 BlockCache 设大些，比如设置BlockCache =0.4，Memstore=0.39。这会加大缓存命中率。

## 十八、HBase优化方法

优化手段主要有以下四个方面

### 1）减少调整

减少调整这个如何理解呢？HBase中有几个内容会动态调整，如region（分区）、HFile，所以通过一些方法来减少这些会带来I/O开销的调整。

**Region**
如果没有预建分区的话，那么随着region中条数的增加，region会进行分裂，这将增加I/O开销，所以解决方法就是根据你的RowKey设计来进行预建分区，减少region的动态分裂。

**HFile**
HFile是数据底层存储文件，在每个memstore进行刷新时会生成一个HFile，当HFile增加到一定程度时，会将属于一个region的HFile进行合并，这个步骤会带来开销但不可避免，但是合并后HFile大小如果大于设定的值，那么HFile会重新分裂。为了减少这样的无谓的I/O开销，建议估计项目数据量大小，给HFile设定一个合适的值。

### 2）减少启停

数据库事务机制就是为了更好地实现批量写入，较少数据库的开启关闭带来的开销，那么HBase中也存在频繁开启关闭带来的问题。

**1、关闭Compaction，在闲时进行手动Compaction**

因为HBase中存在Minor Compaction和Major Compaction，也就是对HFile进行合并，所谓合并就是I/O读写，大量的HFile进行肯定会带来I/O开销，甚至是I/O风暴，所以为了避免这种不受控制的意外发生，建议关闭自动Compaction，在闲时进行compaction

**2、批量数据写入时采用BulkLoad**

如果通过HBase-Shell或者JavaAPI的put来实现大量数据的写入，那么性能差是肯定并且还可能带来一些意想不到的问题，所以当需要写入大量离线数据时建议使用BulkLoad

### 3）减少数据量

虽然我们是在进行大数据开发，但是如果可以通过某些方式在保证数据准确性同时减少数据量，何乐而不为呢？

**1、开启过滤，提高查询速度**
开启BloomFilter，BloomFilter是列族级别的过滤，在生成一个StoreFile同时会生成一个MetaBlock，用于查询时过滤数据

**2、使用压缩：一般推荐使用Snappy和LZO压缩**

### 4）合理设计

在一张HBase表格中RowKey和ColumnFamily的设计是非常重要，好的设计能够提高性能和保证数据的准确性

**1、RowKey设计：应该具备以下几个属性**

- 散列性：散列性能够保证相同相似的rowkey聚合，相异的rowkey分散，有利于查询
- 简短性：rowkey作为key的一部分存储在HFile中，如果为了可读性将rowKey设计得过长，那么将会增加存储压力
- 唯一性：rowKey必须具备明显的区别性
- 业务性：举些例子
  假如我的查询条件比较多，而且不是针对列的条件，那么rowKey的设计就应该支持多条件查询
  如果我的查询要求是最近插入的数据优先，那么rowKey则可以采用叫上Long.Max-时间戳的方式，这样rowKey就是递减排列

**2、列族的设计**

列族的设计需要看应用场景

多列族设计的优劣

**优势：**
HBase中数据时按列进行存储的，那么查询某一列族的某一列时就不需要全盘扫描，只需要扫描某一列族，减少了读I/O；
其实多列族设计对减少的作用不是很明显，适用于读多写少的场景。

**劣势：**
降低了写的I/O性能。原因如下：数据写到store以后是先缓存在memstore中，同一个region中存在多个列族则存在多个store，每个store都一个memstore，当其实memstore进行flush时，属于同一个region 的 store 中的 memstore 都会进行 flush，增加I/O开销。

## 十九、为什么不建议在 HBase 中使用过多的列族

在 Hbase 的表中，每个列族对应 Region 中的一个Store，Region的大小达到阈值时会分裂，因此如果表中有多个列族，则可能出现以下现象：

1. 一个Region中有多个Store，如果每个CF的数据量分布不均匀时，比如CF1为100万，CF2为1万，则Region分裂时导致CF2在每个Region中的数据量太少，查询CF2时会横跨多个Region导致效率降低。
2. 如果每个CF的数据分布均匀，比如CF1有50万，CF2有50万，CF3有50万，则Region分裂时导致每个CF在Region的数据量偏少，查询某个CF时会导致横跨多个Region的概率增大。
3. 多个CF代表有多个Store，也就是说有多个MemStore(2MB)，也就导致内存的消耗量增大，使用效率下降。
4. Region 中的 缓存刷新 和 压缩 是基本操作，即一个CF出现缓存刷新或压缩操作，其它CF也会同时做一样的操作，当列族太多时就会导致IO频繁的问题。

## 二十、Region 如何预建分区？

预分区的目的主要是在创建表的时候指定分区数，提前规划表有多个分区，以及每个分区的区间范围，这样在存储的时候 rowkey 按照分区的区间存储，可以避免 region 热点问题。
通常有两种方案：
**方案 1：shell 方法**

```sql
create 'tb_splits', {NAME => 'cf',VERSIONS=> 3},{SPLITS => ['10','20','30']}
1
```

**方案 2: JAVA 程序控制**

- 取样，先随机生成一定数量的 rowkey,将取样数据按升序排序放到一个集合里；
- 根据预分区的 region 个数，对整个集合平均分割，即是相关的 splitKeys；
- HBaseAdmin.createTable(HTableDescriptor tableDescriptor,byte[][]splitkeys)可以指定预分区的 splitKey，即是指定 region 间的 rowkey 临界值。

## 二十一、如何提高 HBase 客户端的读写性能？请举例说明（☆☆☆☆☆）

1. 开启 bloomfilter 过滤器，开启 bloomfilter 比没开启要快 3、4 倍
2. Hbase 对于内存有特别的需求，在硬件允许的情况下配足够多的内存给它
3. 通过修改 hbase-env.sh 中的
   export HBASE_HEAPSIZE=3000 #这里默认为 1000m
4. 增大 RPC 数量
   通过修改 hbase-site.xml 中的 hbase.regionserver.handler.count 属性，可以适当的放大RPC 数量，默认值为 10 有点小

## 二十一、直接将时间戳作为行健，在写入单个 region 时候会发生热点问题，为什么呢？（☆☆☆☆☆）

region 中的 rowkey 是有序存储，若时间比较集中。就会存储到一个 region 中，这样一个 region 的数据变多，其它的 region 数据很少，加载数据就会很慢，直到 region 分裂，此问题才会得到缓解。

## 二十二、请描述如何解决 HBase 中 region 太小和 region 太大带来的冲突？

Region 过大会发生多次compaction，将数据读一遍并重写一遍到 hdfs 上，占用io，region过小会造成多次 split，region 会下线，影响访问服务，最佳的解决方法是调整 hbase.hregion.max.filesize 为 256m。

## 二十三、解释一下布隆过滤器原理（☆☆☆☆☆）？

Bloom Filter是HBASE用来优化读性能的手段，我们经常会去判断一个元素是否在一个集合中，当数据量比较小的时候，我们可以用Java的HashSet，Java的HashSet是创建一个散列数组，把原来的元素以某种规则映射到散列数组中特定的位置。但如果我们需要判断的元素个数非常大，会导致散列数组非常大，这个时候Bloom Filter就可以发挥作用。问题引出来了，就是我们要用尽可能小的空间，在大数据场景下实现过滤。接下来我会从作用、算法原理、问题、公式推导、hbase应用来介绍Bloom Filter。

**作用**

Bloom Filter的作用就是过滤。Bloom Filter过滤掉的数据，一定不在集合中；未被过滤的数据可能在集合中，也可能不在。

**算法流程**

- 首先需要k个hash函数，每个函数可以把key散列成为1个整数
- 初始化一个长度为n比特的数组，每个比特位初始化为0
- 当某个key加入集合时，用k个hash函数计算出k个散列值，并把数组中对应的比特位从0置为1，如果已经是1则不变。
- 判断某个key是否在集合时，用k个hash函数计算出k个散列值，并查询数组中对应的比特位，如果所有的比特位都是1，认为在集合中。

下图展示了Bloom Filter的原理

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200201100755504.png#pic_center)

## 二十四、HBase与传统关系型数据库(如MySQL)的区别

> - 数据类型：没有数据类型，都是字节数组（有一个工具类Bytes，将java对象序列化为字节数组）。
> - 数据操作：HBase只有很简单的插入、查询、删除、清空等操作，表和表之间是分离的，没有复杂的表和表之间的关系，而传统数据库通常有各式各样的函数和连接操作。
> - 存储模式：Hbase适合于非结构化数据存储，基于列存储而不是行。
> - 数据维护：HBase的更新操作不应该叫更新，它实际上是插入了新的数据，而传统数据库是替换修改
> - 时间版本：Hbase数据写入cell时，还会附带时间戳，默认为数据写入时RegionServer的时间，但是也可以指定一个不同的时间。数据可以有多个版本。
> - 可伸缩性，Hbase这类分布式数据库就是为了这个目的而开发出来的，所以它能够轻松增加或减少硬件的数量，并且对错误的兼容性比较高。而传统数据库通常需要增加中间层才能实现类似的功能

## 二十五、另外的一些面试题

1、读写性能对比（读快还是写快）
2、Hbase的设计有什么心得？
3、Hbase的操作是用的什么API还是什么工具？
4、你们hbase里面是存一些什么数据
5、知道spark怎么读hbase吗？
6、做过hbase的二级索引吗？
7、Hbase的PUT的一个过程
8、什么时候适合使用HBase（应用场景）

```sql
半结构化或非结构化数据:
对于数据结构字段不够确定或杂乱无章非常难按一个概念去进行抽取的数据适合用HBase，因为HBase支持动态添加列。
记录很稀疏：
RDBMS的行有多少列是固定的。为null的列浪费了存储空间。而如上文提到的，HBase为null的Column不会被存储，这样既节省了空间又提高了读性能。
多版本号数据：
依据Row key和Column key定位到的Value能够有随意数量的版本号值，因此对于须要存储变动历史记录的数据，用HBase是很方便的。比方某个用户的Address变更，用户的Address变更记录也许也是具有研究意义的。
仅要求最终一致性：
对于数据存储事务的要求不像金融行业和财务系统这么高，只要保证最终一致性就行。（比如HBase+elasticsearch时，可能出现数据不一致）
高可用和海量数据以及很大的瞬间写入量：
WAL解决高可用，支持PB级数据，put性能高
索引插入比查询操作更频繁的情况。比如，对于历史记录表和日志文件。（HBase的写操作更加高效）
业务场景简单：
不需要太多的关系型数据库特性，列入交叉列，交叉表，事务，连接等。burong
```