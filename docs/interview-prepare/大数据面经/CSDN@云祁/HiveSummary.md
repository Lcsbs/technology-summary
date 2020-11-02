### 文章目录

- - [1、Hive 表关联查询，如何解决数据倾斜的问题？](https://blog.csdn.net/BeiisBei/article/details/104057281#1Hive__2)
  - [2、请谈一下 Hive 的特点，Hive 和 RDBMS 有什么异同？](https://blog.csdn.net/BeiisBei/article/details/104057281#2_Hive_Hive__RDBMS__32)
  - [3、请说明 hive 中 Sort By，Order By，Cluster By，Distrbute By各代表什么意思？](https://blog.csdn.net/BeiisBei/article/details/104057281#3_hive__Sort_ByOrder_ByCluster_ByDistrbute_By_52)
  - [4、Hive 有哪些方式保存元数据，各有哪些特点？](https://blog.csdn.net/BeiisBei/article/details/104057281#4Hive__59)
  - [5、Hive 内部表和外部表的区别？](https://blog.csdn.net/BeiisBei/article/details/104057281#5Hive__63)
  - [6、Hive 的 HSQL 转换为 MapReduce 的过程？](https://blog.csdn.net/BeiisBei/article/details/104057281#6Hive__HSQL__MapReduce__68)
  - [7、Hive 中 的 压 缩 格 式 TextFile 、 SequenceFile 、 RCfile 、ORCfile 各有什么区别？](https://blog.csdn.net/BeiisBei/article/details/104057281#7Hive_______TextFile__SequenceFile__RCfile_ORCfile__72)
  - [8、Hive join 过程中大表小表的放置顺序？](https://blog.csdn.net/BeiisBei/article/details/104057281#8Hive_join__81)
  - [9、Hive 的两张表关联，使用 MapReduce 怎么实现？](https://blog.csdn.net/BeiisBei/article/details/104057281#9Hive__MapReduce__85)
  - [10、Hive 的函数：UDF、UDAF、UDTF 的区别？](https://blog.csdn.net/BeiisBei/article/details/104057281#10Hive_UDFUDAFUDTF__89)
  - [11、说说对 Hive 桶表的理解？](https://blog.csdn.net/BeiisBei/article/details/104057281#11_Hive__95)
  - [12、Hive 自定义 UDF 函数的流程?](https://blog.csdn.net/BeiisBei/article/details/104057281#12Hive__UDF__105)
  - [13、Hive 可以像关系型数据库那样建立多个库吗？](https://blog.csdn.net/BeiisBei/article/details/104057281#13Hive__116)
  - [14、Hive 优化措施？](https://blog.csdn.net/BeiisBei/article/details/104057281#14Hive__120)
  - [15、窗口函数](https://blog.csdn.net/BeiisBei/article/details/104057281#15_124)



## 1、Hive 表关联查询，如何解决数据倾斜的问题？

1）倾斜原因：
map 输出数据按 key Hash 的分配到 reduce 中，由于 key 分布不均匀、业务数据本身的特、
建表时考虑不周、等原因造成的 reduce 上的数据量差异过大。
（1）key 分布不均匀;
（2）业务数据本身的特性;
（3）建表时考虑不周;
（4）某些 SQL 语句本身就有数据倾斜;

如何避免：对于 key 为空产生的数据倾斜，可以对其赋予一个随机值。

2）解决方案

（1）参数调节：

**hive.map.aggr = true
hive.groupby.skewindata=true**

有数据倾斜的时候进行负载均衡，当选项设定位 true，生成的查询计划会有两个 MR Job。第一个 MR Job 中，Map 的输出结果集合会随机分布到 Reduce 中，每个 Reduce 做部分聚合操作，并输出结果，这样处理的结果是相同的 Group By Key 有可能被分发到不同的 Reduce中，从而达到负载均衡的目的；第二个 MR Job 再根据预处理的数据结果按照 Group By Key分布到 Reduce 中（这个过程可以保证相同的 Group By Key 被分布到同一个 Reduce 中），最后完成最终的聚合操作。

（2）SQL 语句调节：

① 选用 join key 分布最均匀的表作为驱动表。做好列裁剪和 filter 操作，以达到两表做join 的时候，数据量相对变小的效果。

② 大小表 Join：

使用 map join 让小的维度表（1000 条以下的记录条数）先进内存，在 map 端完成 reduce。

③ 大表 Join 大表：

把空值的 key 变成一个字符串加上随机数，把倾斜的数据分到不同的 reduce 上，由于null 值关联不上，处理后并不影响最终结果。

④ count distinct 大量相同特殊值:

count distinct 时，将值为空的情况单独处理，如果是计算 count distinct，可以不用处理，直接过滤，在最后结果中加 1，如果还有其他计算，需要进行 group by，可以先将值为空的记录单独处理，再和其他计算结果进行 union。 

## 2、请谈一下 Hive 的特点，Hive 和 RDBMS 有什么异同？

Hive 和数据库除了拥有类似的查询语言，再无类似之处。

1）数据存储位置

Hive 存储在 HDFS 。数据库将数据保存在块设备或者本地文件系统中。

2）数据更新

Hive中不建议对数据的改写。而数据库中的数据通常是需要经常进行修改的，

3）执行延迟

Hive 执行延迟较高。数据库的执行延迟较低。当然，这个是有条件的，即数据规模较小，当数据规模大到超过数据库的处理能力的时候，Hive的并行计算显然能体现出优势。

4）数据规模

Hive支持很大规模的数据计算；数据库可以支持的数据规模较小。

## 3、请说明 hive 中 Sort By，Order By，Cluster By，Distrbute By各代表什么意思？

order by：会对输入做全局排序，因此只有一个 reducer（多个 reducer 无法保证全局有序）。只有一个 reducer，会导致当输入规模较大时，需要较长的计算时间。
sort by：不是全局排序，其在数据进入 reducer 前完成排序。
distribute by：按照指定的字段对数据进行划分输出到不同的 reduce 中。
cluster by：除了具有 distribute by 的功能外还兼具 sort by 的功能。

## 4、Hive 有哪些方式保存元数据，各有哪些特点？

Hive 支持三种不同的元存储服务器，分别为：内嵌式元存储服务器、本地元存储服务器、远程元存储服务器，每种存储方式使用不同的配置参数。内嵌式元存储主要用于单元测试，在该模式下每次只有一个进程可以连接到元存储，Derby 是内嵌式元存储的默认数据库。在本地模式下，每个 Hive 客户端都会打开到数据存储的连接并在该连接上请求 SQL 查询。在远程模式下，所有的 Hive 客户端都将打开一个到元数据服务器的连接，该服务器依次查询元数据，元数据服务器和客户端之间使用 Thrift 协议通信。

## 5、Hive 内部表和外部表的区别？

创建表时：创建内部表时，会将数据移动到数据仓库指向的路径；若创建外部表，仅记录数据所在的路径， 不对数据的位置做任何改变。
删除表时：在删除表的时候，内部表的元数据和数据会被一起删除， 而外部表只删除元数据，不删除数据。这样外部表相对来说更加安全些，数据组织也更加灵活，方便共享源数据。 

## 6、Hive 的 HSQL 转换为 MapReduce 的过程？

![img](https://img-blog.csdnimg.cn/2019040316251031.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NodWp1ZWxpbg==,size_16,color_FFFFFF,t_70)

## 7、Hive 中 的 压 缩 格 式 TextFile 、 SequenceFile 、 RCfile 、ORCfile 各有什么区别？

HIve的文件存储格式有四种：TEXTFILE 、SEQUENCEFILE、ORC、PARQUET，前面两种是行式存储，后面两种是列式存储；所谓的存储格式就是在Hive建表的时候指定的将表中的数据按照什么样子的存储方式，如果指定了A方式，那么在向表中插入数据的时候，将会使用该方式向HDFS中添加相应的数据类型。

如果为textfile的文件格式，直接load就OK，不需要走MapReduce；如果是其他的类型就需要走MapReduce了，因为其他的类型都涉及到了文件的压缩，这需要借助MapReduce的压缩方式来实现。

比对三种主流的文件存储格式TEXTFILE 、ORC、PARQUET
压缩比：ORC >  Parquet >  textFile（textfile没有进行压缩）
查询速度：三者几乎一致
HDFS上显示的是原来的文件名，如果压缩的话，使用类似于000000_0的文件名

## 8、Hive join 过程中大表小表的放置顺序？

将最大的表放置在 JOIN 语句的最右边，或者直接使用/*+ streamtable(table_name) */指出。在编写带有 join 操作的代码语句时，应该将条目少的表/子查询放在 Join 操作符的左边。因为在 Reduce 阶段，位于 Join 操作符左边的表的内容会被加载进内存，载入条目较少的表可以有效减少 OOM（out of memory）即内存溢出。所以对于同一个 key 来说，对应的 value 值小的放前，大的放后，这便是“小表放前”原则。若一条语句中有多个 Join，依据 Join 的条件相同与否，有不同的处理方法。

## 9、Hive 的两张表关联，使用 MapReduce 怎么实现？

如果其中有一张表为小表，直接使用 map 端 join 的方式（map 端加载小表）进行聚合。如果两张都是大表，那么采用联合 key，联合 key 的第一个组成部分是 join on 中的公共字段，第二部分是一个 flag，0 代表表 A，1 代表表 B，由此让 Reduce 区分客户信息和订单信息；在 Mapper 中同时处理两张表的信息，将 join on 公共字段相同的数据划分到同一个分区中，进而传递到一个 Reduce 中，然后在 Reduce 中实现聚合。 

## 10、Hive 的函数：UDF、UDAF、UDTF 的区别？

UDF: 单行进入，单行输出
UDAF: 多行进入，单行输出
UDTF: 单行输入，多行输出 

## 11、说说对 Hive 桶表的理解？

分区针对的是数据的存储路径；分桶针对的是数据文件。

Hive分桶通俗点来说就是将表（或者分区，也就是hdfs上的目录而真正的数据是存储在该目录下的文件）中文件分成几个文件去存储。比如表buck(目录，里面存放了某个文件如sz.data)文件中本来是1000000条数据，由于在处理大规模数据集时，在开发和修改查询的阶段，如果能在数据集的一小部分数据上试运行查询，会带来很多方便，所以我们可以分4个文件去存储。 

桶表是对数据进行哈希取值，然后放到不同文件中存储。数据加载到桶表时，会对字段取 hash 值，然后与桶的数量取模。把数据放到对应的文件中。物理上，每个桶就是表(或分区）目录里的一个文件，一个作业产生的桶(输出文件)和reduce 任务个数相同。桶表专门用于抽样查询，是很专业性的，不是日常用来存储数据的表，需要抽样查询时，才创建和使用桶表。 

## 12、Hive 自定义 UDF 函数的流程?

1）写一个类继承（org.apache.hadoop.hive.ql.）UDF 类；
2）覆盖方法 evaluate()；
3）打 JAR 包；
4）通过 hive 命令将 JAR 添加到 Hive 的类路径：
hive> add jar /home/ubuntu/ToDate.jar;
5）注册函数：
hive> create temporary function xxx as 'XXX';
6）使用函数；

## 13、Hive 可以像关系型数据库那样建立多个库吗？

可以建立多个库。 

## 14、Hive 优化措施？

hive调优是比较大的专题，需要结合实际的业务，数据的类型，分布，质量状况等来实际的考虑如何进行系统性的优化，hive底层是mapreduce，所以hadoop调优也是hive调优的一个基础,hvie调优可以分为几个模块进行考虑，数据的压缩与存储，sql的优化，hive参数的优化，解决数据的倾斜等。

## 15、窗口函数

RANK() 排序相同时会重复，总数不会变

DENSE_RANK() 排序相同时会重复，总数会减少

ROW_NUMBER() 会根据顺序计算

1） OVER()：指定分析函数工作的数据窗口大小，这个数据窗口大小可能会随着行的变而变化

2）CURRENT ROW：当前行

3）n PRECEDING：往前n行数据

4） n FOLLOWING：往后n行数据

5）UNBOUNDED：起点，UNBOUNDED PRECEDING 表示从前面的起点， UNBOUNDED FOLLOWING表示到后面的终点

6） LAG(col,n)：往前第n行数据

7）LEAD(col,n)：往后第n行数据

8） NTILE(n)：把有序分区中的行分发到指定数据的组中，各个组有编号，编号从1开始，对于每一行，NTILE返回此行所属的组的编号。注意：n必须为int类型。