# Spark概述

Apache Spark是用于大规模数据处理的统一分析引擎。它提供Java，Scala，Python和R中的高级API，以及支持常规执行图的优化引擎。它还支持一组丰富的更高级别的工具，包括[Spark SQL](http://spark.apache.org/docs/latest/sql-programming-guide.html) 用于SQL和结构化数据的处理，[MLlib](http://spark.apache.org/docs/latest/ml-guide.html) 机器学习，[GraphX](http://spark.apache.org/docs/latest/graphx-programming-guide.html) 用于图形处理，以及[Structured Streaming](http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html) 的增量计算和流处理。

# 安全

默认情况下，Spark中的安全性处于关闭状态。这可能意味着您默认情况下容易受到攻击。下载并运行Spark之前，请参阅[Spark Security](http://spark.apache.org/docs/latest/security.html) 。

# 正在下载

从项目网站的[下载页面](https://spark.apache.org/downloads.html) 获取Spark 。本文档适用于Spark版本3.0.1。Spark将Hadoop的客户端库用于HDFS和YARN。下载已预先打包为一些流行的Hadoop版本。用户还可以下载“免费的Hadoop”二进制文件，并[通过扩展Spark的classpath](http://spark.apache.org/docs/latest/hadoop-provided.html) 在任何Hadoop版本上运行Spark 。Scala和Java用户可以使用其Maven坐标将Spark包含在他们的项目中，而Python用户可以从PyPI安装Spark。

如果您想从源代码构建Spark，请访问[Building Spark](http://spark.apache.org/docs/latest/building-spark.html) 。

Spark可在Windows和类似UNIX的系统（例如Linux，Mac OS）上运行，并且应在运行受支持的Java版本的任何平台上运行。这应该包括x86_64和ARM64上的JVM。在一台机器上本地运行很容易-您所需要做的就是`java`在系统上安装`PATH`或`JAVA_HOME`指向Java安装的环境变量。

Spark可在Java 8/11，Scala 2.12，Python 2.7 + / 3.4 +和R 3.5+上运行。从Spark 3.0.0开始不支持Java 8之前的版本8u92。从Spark 3.0.0开始不推荐使用3.6版之前的Python 2和Python 3。对于Scala API，Spark 3.0.1使用Scala 2.12。您将需要使用兼容的Scala版本（2.12.x）。

对于Java 11，`-Dio.netty.tryReflectionSetAccessible=true`Apache Arrow库另外需要。这样可以防止`java.lang.UnsupportedOperationException: sun.misc.Unsafe or java.nio.DirectByteBuffer.(long, int)  not available`Apache Arrow在内部使用Netty的情况。

# 运行示例和Shell

Spark附带了几个示例程序。目录中有Scala，Java，Python和R示例 `examples/src/main`。要运行Java或Scala示例程序之一，请 `bin/run-example <class> [params]`在顶级Spark目录中使用。（在后台，这将调用更通用的 [`spark-submit`脚本](http://spark.apache.org/docs/latest/submitting-applications.html) 来启动应用程序）。例如，

```shell
./bin/run-example SparkPi 10
```

您还可以通过修改后的Scala shell版本以交互方式运行Spark。这是学习框架的好方法。

```shell
./bin/spark-shell --master local[2]
```

该`--master`选项指定[分布式集群](http://spark.apache.org/docs/latest/submitting-applications.html#master-urls) 的 [主URL](http://spark.apache.org/docs/latest/submitting-applications.html#master-urls) ，或`local`使用一个线程`local[N]`在本地运行，或使用N个线程在本地运行。您应该先从`local`进行测试开始 。有关选项的完整列表，请运行带有该`--help`选项的Spark shell 。

Spark还提供了Python API。要在Python解释器中交互式运行Spark，请使用 `bin/pyspark`：

```shell
./bin/pyspark --master local[2]
```

Python还提供了示例应用程序。例如，

```shell
./bin/spark-submit examples/src/main/python/pi.py 10
```

从1.4开始，Spark还提供了[R API](http://spark.apache.org/docs/latest/sparkr.html) （仅包含DataFrames API）。要在R解释器中交互式运行Spark，请使用`bin/sparkR`：

```shell
./bin/sparkR --master local[2]
```

R中还提供了示例应用程序。例如，

```shell
./bin/spark-submit examples/src/main/r/dataframe.R
```

# 在集群上启动

Spark[集群模式概述](http://spark.apache.org/docs/latest/cluster-overview.html) 介绍了在集群上运行的关键概念。Spark既可以单独运行，也可以在多个现有集群管理器上运行。当前，它提供了几种部署选项：

- [Standalone Depoly Model](http://spark.apache.org/docs/latest/spark-standalone.html) ：在私有集群上部署Spark的最简单方法
- [Apache Mesos](http://spark.apache.org/docs/latest/running-on-mesos.html) 
- [Hadoop Yarn](http://spark.apache.org/docs/latest/running-on-yarn.html) 
- [Kubernetes](http://spark.apache.org/docs/latest/running-on-kubernetes.html) 

# 从这往哪儿走

**编程指南：**

- [Quick Start](http://spark.apache.org/docs/latest/quick-start.html) ：Spark API快速入门；从这里开始！
- [RDD Programing Guide](http://spark.apache.org/docs/latest/rdd-programming-guide.html) ：Spark基础概述-RDD（核心但旧的API），累加器和广播变量
- [Spark SQL,Datasets,and DataFrames](http://spark.apache.org/docs/latest/sql-programming-guide.html) ：通过关系查询（比RDD更新的API）处理结构化数据
- [Structured Streaming](http://spark.apache.org/docs/latest/structured-streaming-programming-guide.html) ：使用关系查询处理结构化数据流（使用数据集和数据帧，比DStreams更新的API）
- [Spark Streaming](http://spark.apache.org/docs/latest/streaming-programming-guide.html) ：使用DStreams处理数据流（旧API）
- [MLlib](http://spark.apache.org/docs/latest/ml-guide.html) ：应用机器学习算法
- [GraphX](http://spark.apache.org/docs/latest/graphx-programming-guide.html) ：处理图形

**API文件：**

- [Spark Scala API（Scaladoc）](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/index.html) 
- [Spark Java API（Javadoc）](http://spark.apache.org/docs/latest/api/java/index.html) 
- [Spark Python API（Sphinx）](http://spark.apache.org/docs/latest/api/python/index.html) 
- [Spark R API（Roxygen2）](http://spark.apache.org/docs/latest/api/R/index.html) 
- [Spark SQL，内置函数（MkDocs）](http://spark.apache.org/docs/latest/api/sql/index.html) 

**部署指南：**

- [集群概述](http://spark.apache.org/docs/latest/cluster-overview.html) ：在集群上运行时的概念和组件概述
- [提交应用程序](http://spark.apache.org/docs/latest/submitting-applications.html) ：打包和部署应用程序
- 部署方式：
  - [Amazon EC2](https://github.com/amplab/spark-ec2) ：可使您在大约5分钟内在EC2上启动集群的脚本
  - [独立部署模式](http://spark.apache.org/docs/latest/spark-standalone.html) ：无需第三方集群管理器即可快速启动独立集群
  - [Mesos](http://spark.apache.org/docs/latest/running-on-mesos.html) ：使用[Apache Mesos](https://mesos.apache.org/) 部署私有集群
  - [YARN](http://spark.apache.org/docs/latest/running-on-yarn.html) ：在Hadoop NextGen（YARN）之上部署Spark
  - [Kubernetes](http://spark.apache.org/docs/latest/running-on-kubernetes.html) ：在[Kubernetes之上](http://spark.apache.org/docs/latest/running-on-kubernetes.html) 部署Spark

**其他文件：**

- [配置](http://spark.apache.org/docs/latest/configuration.html) ：通过其配置系统自定义Spark
- [监视](http://spark.apache.org/docs/latest/monitoring.html) ：跟踪应用程序的行为
- [调优指南](http://spark.apache.org/docs/latest/tuning.html) ：优化性能和内存使用的最佳做法
- [作业调度](http://spark.apache.org/docs/latest/job-scheduling.html) ：在Spark应用程序之间和内部调度资源
- [安全性](http://spark.apache.org/docs/latest/security.html) ：Spark安全性支持
- [硬件配置](http://spark.apache.org/docs/latest/hardware-provisioning.html) ：有关群集硬件的建议
- 与其他存储系统集成：
  - [云基础架构](http://spark.apache.org/docs/latest/cloud-integration.html) 
  - [OpenStack迅捷](http://spark.apache.org/docs/latest/storage-openstack-swift.html) 
- [迁移指南](http://spark.apache.org/docs/latest/migration-guide.html) ：Spark组件的迁移指南
- [构建Spark](http://spark.apache.org/docs/latest/building-spark.html) ：使用Maven系统构建Spark
- [为Spark贡献](https://spark.apache.org/contributing.html) 
- [第三方项目](https://spark.apache.org/third-party-projects.html) ：相关的第三方Spark项目

**外部资源：**

- [Spark主页](https://spark.apache.org/) 
- [Spark社区](https://spark.apache.org/community.html) 资源，包括本地聚会
- [StackOverflow标签 `apache-spark`](http://stackoverflow.com/questions/tagged/apache-spark) 
- [邮件列表](https://spark.apache.org/mailing-lists.html) ：在此处询问有关Spark的问题
- [AMP营地](http://ampcamp.berkeley.edu/) ：加州大学伯克利分校的一系列训练营，其中包含有关Spark，Spark Streaming，Mesos等的讲座和练习。[视频](http://ampcamp.berkeley.edu/6/) ， [幻灯片](http://ampcamp.berkeley.edu/6/) 和[练习](http://ampcamp.berkeley.edu/6/exercises/) 可在线免费获得。
- [代码示例](https://spark.apache.org/examples.html) ：`examples`Spark的子文件夹（[Scala](https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples) ， [Java](https://github.com/apache/spark/tree/master/examples/src/main/java/org/apache/spark/examples) ， [Python](https://github.com/apache/spark/tree/master/examples/src/main/python) ， [R](https://github.com/apache/spark/tree/master/examples/src/main/r) ）中也提供更多[示例](https://spark.apache.org/examples.html) 
