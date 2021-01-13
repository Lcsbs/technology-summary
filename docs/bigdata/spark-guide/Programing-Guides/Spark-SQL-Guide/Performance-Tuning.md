# 性能调优

- [在内存中缓存数据](http://spark.apache.org/docs/latest/sql-performance-tuning.html#caching-data-in-memory)
- [其他配置选项](http://spark.apache.org/docs/latest/sql-performance-tuning.html#other-configuration-options)
- [加入针对SQL查询的策略提示](http://spark.apache.org/docs/latest/sql-performance-tuning.html#join-strategy-hints-for-sql-queries)
- [SQL查询的合并提示](http://spark.apache.org/docs/latest/sql-performance-tuning.html#coalesce-hints-for-sql-queries)
- 自适应查询执行
  - [合并后的Shuffle分区](http://spark.apache.org/docs/latest/sql-performance-tuning.html#coalescing-post-shuffle-partitions)
  - [将排序合并联接转换为广播联接](http://spark.apache.org/docs/latest/sql-performance-tuning.html#converting-sort-merge-join-to-broadcast-join)
  - [优化偏斜连接](http://spark.apache.org/docs/latest/sql-performance-tuning.html#optimizing-skew-join)

对于某些工作负载，可以通过在内存中缓存数据或打开某些实验选项来提高性能。

## 在内存中缓存数据

Spark SQL可以通过调用`spark.catalog.cacheTable("tableName")`或使用内存列式格式缓存表`dataFrame.cache()`。然后，Spark SQL将仅扫描所需的列，并将自动调整压缩以最大程度地减少内存使用和GC压力。您可以调用`spark.catalog.uncacheTable("tableName")`从内存中删除表。

可以使用`setConf`on上的方法`SparkSession`或`SET key=value`使用SQL运行 命令来完成内存中缓存的配置。

| 属性名称                                       | 默认  | 含义                                                         | 自版本 |
| :--------------------------------------------- | :---- | :----------------------------------------------------------- | :----- |
| `spark.sql.inMemoryColumnarStorage.compressed` | 真正  | 设置为true时，Spark SQL将根据数据统计信息自动为每一列选择一个压缩编解码器。 | 1.0.1  |
| `spark.sql.inMemoryColumnarStorage.batchSize`  | 10000 | 控制用于列式缓存的批处理的大小。较大的批处理大小可以提高内存利用率和压缩率，但是在缓存数据时会出现OOM。 | 1.1.1  |

## 其他配置选项

以下选项也可以用于调整查询执行的性能。随着自动执行更多优化，这些选项可能会在将来的版本中被弃用。

| 属性名称                                                   | 默认                | 含义                                                         | 自版本 |
| :--------------------------------------------------------- | :------------------ | :----------------------------------------------------------- | :----- |
| `spark.sql.files.maxPartitionBytes`                        | 134217728（128 MB） | 读取文件时打包到单个分区中的最大字节数。仅当使用基于文件的源（例如Parquet，JSON和ORC）时，此配置才有效。 | 2.0.0  |
| `spark.sql.files.openCostInBytes`                          | 4194304（4 MB）     | 可以同时扫描以字节数衡量的打开文件的估计成本。将多个文件放入分区时使用。最好高估一下，然后具有较小文件的分区将比具有较大文件的分区（首先安排）更快。仅当使用基于文件的源（例如Parquet，JSON和ORC）时，此配置才有效。 | 2.0.0  |
| `spark.sql.broadcastTimeout`                               | 300                 | 广播加入中广播等待时间的秒数超时                             | 1.3.0  |
| `spark.sql.autoBroadcastJoinThreshold`                     | 10485760（10 MB）   | 配置表的最大大小（以字节为单位），该表在执行联接时将广播到所有工作程序节点。通过将此值设置为-1，可以禁用广播。请注意，当前仅`ANALYZE TABLE <tableName> COMPUTE STATISTICS noscan`运行命令的Hive Metastore表支持统计信息 。 | 1.1.0  |
| `spark.sql.shuffle.partitions`                             | 200                 | 配置在对联接或聚集进行数据混排时要使用的分区数。             | 1.1.0  |
| `spark.sql.sources.parallelPartitionDiscovery.threshold`   | 32                  | 配置阈值以启用作业输入路径的并行列表。如果输入路径数大于此阈值，Spark将使用Spark分布式作业列出文件。否则，它将回退到顺序列出。仅当使用基于文件的数据源（例如Parquet，ORC和JSON）时，此配置才有效。 | 1.5.0  |
| `spark.sql.sources.parallelPartitionDiscovery.parallelism` | 10000               | 配置作业输入路径的最大列表并行度。如果输入路径的数量大于此值，它将被调低以使用此值。与上述相同，此配置仅在使用基于文件的数据源（例如Parquet，ORC和JSON）时有效。 | 2.1.1  |

## 加入针对SQL查询的策略提示

连接策略提示，即`BROADCAST`，`MERGE`，`SHUFFLE_HASH`和`SHUFFLE_REPLICATE_NL`，指导星火与其他关系结合时，他们使用暗示策略上的每个特定关系。例如，当`BROADCAST`在表't1'上使用提示时，Spark将优先考虑以't1'作为构建侧的广播联接（广播哈希联接或广播嵌套循环联接，取决于是否有任何等联接键）。即使统计信息建议的表't1'的大小在配置之上`spark.sql.autoBroadcastJoinThreshold`。

当在连接的两侧指定了不同的连接策略提示时，Spark会优先于`BROADCAST`提示而不是`MERGE`提示优先 `SHUFFLE_HASH`于`SHUFFLE_REPLICATE_NL` 提示。当使用`BROADCAST`提示或`SHUFFLE_HASH`提示指定双方时，Spark将根据联接类型和关系的大小选择构建方。

请注意，由于特定策略可能不支持所有联接类型，因此不能保证Spark将选择提示中指定的联接策略。

- [**Scala**](http://spark.apache.org/docs/latest/sql-performance-tuning.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-performance-tuning.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-performance-tuning.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-performance-tuning.html#tab_r_0)
- [**SQL**](http://spark.apache.org/docs/latest/sql-performance-tuning.html#tab_SQL_0)

```scala
spark.table("src").join(spark.table("records").hint("broadcast"), "key").show()
```

有关更多详细信息，请参阅[Join Hints](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-hints.html#join-hints)的文档。

## SQL查询的合并提示

Coalesce提示使Spark SQL用户可以像一样控制输出文件的数量 `coalesce`，`repartition`并且`repartitionByRange`在Dataset API中，它们可以用于性能调整和减少输出文件的数量。“ COALESCE”提示仅具有分区号作为参数。“ REPARTITION”提示具有分区号和/或列作为参数。“ REPARTITION_BY_RANGE”提示必须具有列名，并且分区号是可选的。

```scala
SELECT /*+ COALESCE(3) */ * FROM t
SELECT /*+ REPARTITION(3) */ * FROM t
SELECT /*+ REPARTITION(c) */ * FROM t
SELECT /*+ REPARTITION(3, c) */ * FROM t
SELECT /*+ REPARTITION_BY_RANGE(c) */ * FROM t
SELECT /*+ REPARTITION_BY_RANGE(3, c) */ * FROM t
```

有关更多详细信息，请参阅“[分区提示](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-hints.html#partitioning-hints)”文档。

## 自适应查询执行

自适应查询执行（AQE）是Spark SQL中的一种优化技术，它利用运行时统计信息来选择最有效的查询执行计划。默认情况下禁用AQE。Spark SQL可以使用的伞形配置`spark.sql.adaptive.enabled`来控制是否打开/关闭它。从Spark 3.0开始，AQE具有三个主要功能，包括合并后混洗分区，将排序合并联接转换为广播联接以及倾斜联接优化。

### 合并后的Shuffle分区

当`spark.sql.adaptive.enabled`和`spark.sql.adaptive.coalescePartitions.enabled`配置均为true时，此功能将根据地图输出统计信息合并后混洗分区。此功能简化了在运行查询时对混洗分区号的调整。您无需设置适当的随机播放分区号即可适合您的数据集。一旦通过`spark.sql.adaptive.coalescePartitions.initialPartitionNum`配置设置了足够大的初始shuffle分区数量，Spark便可以在运行时选择正确的shuffle分区编号。

| 属性名称                                                    | 默认     | 含义                                                         | 自版本 |
| :---------------------------------------------------------- | :------- | :----------------------------------------------------------- | :----- |
| `spark.sql.adaptive.coalescePartitions.enabled`             | 真正     | 如果为true和`spark.sql.adaptive.enabled`true，Spark将根据目标大小（由指定`spark.sql.adaptive.advisoryPartitionSizeInBytes`）合并连续的shuffle分区，以避免执行过多的小任务。 | 3.0.0  |
| `spark.sql.adaptive.coalescePartitions.minPartitionNum`     | 默认并行 | 合并后的最小混洗分区数。如果未设置，则默认值为Spark集群的默认并行度。仅当`spark.sql.adaptive.enabled`和`spark.sql.adaptive.coalescePartitions.enabled`都启用时，此配置才有效。 | 3.0.0  |
| `spark.sql.adaptive.coalescePartitions.initialPartitionNum` | 200      | 合并之前的洗牌分区的初始数量。默认情况下，它等于`spark.sql.shuffle.partitions`。仅当`spark.sql.adaptive.enabled`和`spark.sql.adaptive.coalescePartitions.enabled`都启用时，此配置才有效。 | 3.0.0  |
| `spark.sql.adaptive.advisoryPartitionSizeInBytes`           | 64兆字节 | 自适应优化过程中洗牌分区的建议大小（以字节为单位`spark.sql.adaptive.enabled`）（为true时）。当Spark合并较小的shuffle分区或拆分倾斜的shuffle分区时，此命令才会生效。 | 3.0.0  |

### 将排序合并联接转换为广播联接

当任何联接端的运行时统计信息小于广播哈希联接阈值时，AQE会将排序合并联接转换为广播哈希联接。这不像首先计划广播哈希连接那样有效，但是比继续进行排序合并连接要好，因为我们可以保存两个连接端的排序，并在本地读取随机文件以节省网络流量（如果`spark.sql.adaptive.localShuffleReader.enabled`是真的）

### 优化偏斜连接

数据偏斜会严重降低联接查询的性能。此功能通过将倾斜的任务拆分（按需复制）为大小大致相等的任务来动态处理排序合并联接中的倾斜。同时启用`spark.sql.adaptive.enabled`和`spark.sql.adaptive.skewJoin.enabled`配置时，此选项才生效。

| 属性名称                                                     | 默认  | 含义                                                         | 自版本 |
| :----------------------------------------------------------- | :---- | :----------------------------------------------------------- | :----- |
| `spark.sql.adaptive.skewJoin.enabled`                        | 真正  | 在true和`spark.sql.adaptive.enabled`true时，Spark通过拆分（如果需要，可以复制）歪斜的分区来动态处理排序合并联接中的歪斜。 | 3.0.0  |
| `spark.sql.adaptive.skewJoin.skewedPartitionFactor`          | 10    | 如果分区的大小大于此因子乘以分区中位数的乘积，并且也大于，则认为分区是倾斜的`spark.sql.adaptive.skewedPartitionThresholdInBytes`。 | 3.0.0  |
| `spark.sql.adaptive.skewJoin.skewedPartitionThresholdInBytes` | 256MB | 如果分区的字节大小大于此阈值，并且大于`spark.sql.adaptive.skewJoin.skewedPartitionFactor`分区中位数的乘积，则认为该分区是歪斜的。理想情况下，此配置应设置为大于`spark.sql.adaptive.advisoryPartitionSizeInBytes`。 | 3.0.0  |

