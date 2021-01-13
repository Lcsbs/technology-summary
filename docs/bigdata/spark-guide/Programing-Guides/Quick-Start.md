# 快速开始

- [安全](http://spark.apache.org/docs/latest/quick-start.html#security)  
- 使用Spark Shell进行交互式分析
  - [基本](http://spark.apache.org/docs/latest/quick-start.html#basics)  
  - [有关数据集操作的更多信息](http://spark.apache.org/docs/latest/quick-start.html#more-on-dataset-operations)  
  - [快取](http://spark.apache.org/docs/latest/quick-start.html#caching)  
- [自包含的应用程序](http://spark.apache.org/docs/latest/quick-start.html#self-contained-applications)  
- [从这往哪儿走](http://spark.apache.org/docs/latest/quick-start.html#where-to-go-from-here)  

本教程提供了使用Spark的快速介绍。我们将首先通过Spark的交互式Shell（在Python或Scala中）介绍API，然后展示如何用Java，Scala和Python编写应用程序。

要遵循本指南，请首先从[Spark网站](https://spark.apache.org/downloads.html)  下载Spark的打包版本 。由于我们不会使用HDFS，因此您可以下载适用于任何Hadoop版本的软件包。

请注意，在Spark 2.0之前，Spark的主要编程接口是弹性分布式数据集（RDD）。在Spark 2.0之后，RDD被Dataset取代，Dataset的类型像RDD一样强，但具有更丰富的优化功能。仍支持RDD界面，您可以在[RDD编程指南中](http://spark.apache.org/docs/latest/rdd-programming-guide.html)  获得更详细的参考。但是，我们强烈建议您切换到使用Dataset，Dataset的性能比RDD更好。请参阅[SQL编程指南](http://spark.apache.org/docs/latest/sql-programming-guide.html)  以获取有关数据集的更多信息。

# 安全

默认情况下，Spark中的安全性处于关闭状态。这可能意味着您默认情况下容易受到攻击。运行Spark之前，请参阅[Spark Security](http://spark.apache.org/docs/latest/security.html)  。

# 使用Spark Shell进行交互式分析

## 基本

Spark的Shell提供了学习API的简单方法，以及强大的工具来交互式地分析数据。它可以在Scala（可在Java VM上运行，因此是使用现有Java库的好方法）或Python中提供。通过在Spark目录中运行以下命令来启动它：

- [**Scala**](http://spark.apache.org/docs/latest/quick-start.html#tab_scala_0)  
- [**Python**](http://spark.apache.org/docs/latest/quick-start.html#tab_python_0)  

```shell
./bin/spark-shell
```

Spark的主要抽象是称为数据集的项目的分布式集合。可以从Hadoop InputFormats（例如HDFS文件）或通过转换其他数据集来创建数据集。让我们从Spark源目录中的README文件的文本中创建一个新的数据集：

```shell
scala> val textFile = spark.read.textFile("README.md")  
textFile: org.apache.spark.sql.Dataset[String] = [value: string]
```

您可以通过调用某些操作直接从数据集中获取值，或转换数据集以获取新值。有关更多详细信息，请阅读*[API文档](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/Dataset.html)  *。

```shell
scala> textFile.count()   // Number of items in this Dataset
res0: Long = 126 // May be different from yours as README.md will change over time, similar to other outputs

scala> textFile.first()   // First item in this Dataset
res1: String = # Apache Spark
```

现在，让我们将此数据集转换为一个新的数据集。我们调用`filter`返回一个新的数据集，其中包含文件中项的子集。

```shell
scala> val linesWithSpark = textFile.filter(line => line.contains("Spark")  )  
linesWithSpark: org.apache.spark.sql.Dataset[String] = [value: string]
```

我们可以将转换和动作链接在一起：

```shell
scala> textFile.filter(line => line.contains("Spark")  )  .count()   // How many lines contain "Spark"?
res3: Long = 15
```

## 有关数据集操作的更多信息

数据集操作和转换可用于更复杂的计算。假设我们要查找包含最多单词的行：

- [**Scala**](http://spark.apache.org/docs/latest/quick-start.html#tab_scala_1)  
- [**Python**](http://spark.apache.org/docs/latest/quick-start.html#tab_python_1)  

```shell
scala> textFile.map(line => line.split(" ")  .size)  .reduce((a, b)   => if (a > b)   a else b)  
res4: Long = 15
```

首先，将一条线映射到一个整数值，以创建一个新的数据集。`reduce`在该数据集上调用，以找到最大的字数。到的参数`map`和`reduce`是Scala的函数文本（关闭），并且可以使用任何语言功能或Scala/ Java库。例如，我们可以轻松地调用在其他地方声明的函数。我们将使用`Math.max()  `函数使此代码更易于理解：

```shell
scala> import java.lang.Math
import java.lang.Math

scala> textFile.map(line => line.split(" ")  .size)  .reduce((a, b)   => Math.max(a, b)  )  
res5: Int = 15
```

一种常见的数据流模式是Hadoop流行的MapReduce。Spark可以轻松实现MapReduce流：

```shell
scala> val wordCounts = textFile.flatMap(line => line.split(" ")  )  .groupByKey(identity)  .count()  
wordCounts: org.apache.spark.sql.Dataset[(String, Long)  ] = [value: string, count(1)  : bigint]
```

在这里，我们调用`flatMap`将行的数据集转换为单词的数据集，然后组合`groupByKey`并`count`计算文件中每个单词的计数，作为（字符串，长整数）对的数据集。要收集外壳中的字数，我们可以调用`collect`：

```shell
scala> wordCounts.collect()  
res6: Array[(String, Int)  ] = Array((means,1)  , (under,2)  , (this,3)  , (Because,1)  , (Python,2)  , (agree,1)  , (cluster.,1)  , ...)  
```

## 快取

Spark还支持将数据集提取到群集范围的内存中缓存中。当重复访问数据时，例如查询小的“热”数据集或运行迭代算法（如PageRank）时，这非常有用。作为一个简单的示例，让我们将`linesWithSpark`数据集标记为要缓存：

- [**Scala**](http://spark.apache.org/docs/latest/quick-start.html#tab_scala_2)  
- [**Python**](http://spark.apache.org/docs/latest/quick-start.html#tab_python_2)  

```shell
scala> linesWithSpark.cache()  
res7: linesWithSpark.type = [value: string]

scala> linesWithSpark.count()  
res8: Long = 15

scala> linesWithSpark.count()  
res9: Long = 15
```

使用Spark浏览和缓存100行文本文件似乎很愚蠢。有趣的是，即使在数十个或数百个节点上进行条带化时，这些相同的函数也可以用于非常大的数据集。您也可以通过连接`bin/spark-shell`到集群来交互式地执行此操作，如[RDD编程指南中所述](http://spark.apache.org/docs/latest/rdd-programming-guide.html#using-the-shell)  。

# 自包含的应用程序

假设我们希望使用Spark API编写一个独立的应用程序。我们将逐步介绍Scala（带有sbt），Java（带有Maven）和Python（pip）的简单应用程序。

- [**Scala**](http://spark.apache.org/docs/latest/quick-start.html#tab_scala_3)  
- [**Java**](http://spark.apache.org/docs/latest/quick-start.html#tab_java_3)  
- [**Python**](http://spark.apache.org/docs/latest/quick-start.html#tab_python_3)  

我们将在Scala中创建一个非常简单的Spark应用程序-实际上如此简单，它名为`SimpleApp.scala`：

```scala
/* SimpleApp.scala */
import org.apache.spark.sql.SparkSession

object SimpleApp {
  def main(args: Array[String])   {
    val logFile = "YOUR_SPARK_HOME/README.md" // Should be some file on your system
    val spark = SparkSession.builder.appName("Simple Application")  .getOrCreate()  
    val logData = spark.read.textFile(logFile)  .cache()  
    val numAs = logData.filter(line => line.contains("a")  )  .count()  
    val numBs = logData.filter(line => line.contains("b")  )  .count()  
    println(s"Lines with a: $numAs, Lines with b: $numBs")  
    spark.stop()  
  }
}
```

注意，应用程序应该定义一个`main()  `方法而不是extend `scala.App` 的子类`scala.App`可能无法正常工作。

该程序只计算Spark自述文件中包含“ a”的行数和包含“ b”的行数。请注意，您需要用Spark的安装位置替换YOUR_SPARK_HOME。与前面带有初始化自己的SparkSession的Spark Shell的示例不同，我们将SparkSession初始化为程序的一部分。

我们调用`SparkSession.builder`构造一个`SparkSession`，然后设置应用程序名称，最后调用`getOrCreate`来获取`SparkSession`实例。

我们的应用程序依赖于Spark API，因此我们还将包含一个sbt配置文件`build.sbt`，该文件 解释了Spark是一个依赖项。该文件还添加了Spark依赖的存储库：

```shell
name := "Simple Project"

version := "1.0"

scalaVersion := "2.12.10"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.1"
```

对于SBT正常工作，我们需要布局`SimpleApp.scala`并`build.sbt` 根据典型的目录结构。安装好之后，我们可以创建一个包含应用程序代码的JAR包，然后使用`spark-submit`脚本来运行我们的程序。

```shell
# Your directory layout should look like this
$ find .
.
./build.sbt
./src
./src/main
./src/main/scala
./src/main/scala/SimpleApp.scala

# Package a jar containing your application
$ sbt package
...
[info] Packaging {..}/{..}/target/scala-2.12/simple-project_2.12-1.0.jar

# Use spark-submit to run your application
$ YOUR_SPARK_HOME/bin/spark-submit \
  --class "SimpleApp" \
  --master local[4] \
  target/scala-2.12/simple-project_2.12-1.0.jar
...
Lines with a: 46, Lines with b: 23
```

# 从这往哪儿走

祝贺您运行第一个Spark应用程序！

- 有关API的深入概述，请从[RDD编程指南](http://spark.apache.org/docs/latest/rdd-programming-guide.html)  和[SQL编程指南开始](http://spark.apache.org/docs/latest/sql-programming-guide.html)  ，或参阅“编程指南”菜单以获取其他组件。
- 要在集群上运行应用程序，请转至[部署概述](http://spark.apache.org/docs/latest/cluster-overview.html)  。
- 最后，Spark在`examples`目录（[Scala](https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples)  ， [Java](https://github.com/apache/spark/tree/master/examples/src/main/java/org/apache/spark/examples)  ， [Python](https://github.com/apache/spark/tree/master/examples/src/main/python)  ， [R](https://github.com/apache/spark/tree/master/examples/src/main/r)  ）中包含几个示例。您可以按以下方式运行它们：

```shell
# For Scala and Java, use run-example:
./bin/run-example SparkPi

# For Python examples, use spark-submit directly:
./bin/spark-submit examples/src/main/python/pi.py

# For R examples, use spark-submit directly:
./bin/spark-submit examples/src/main/r/dataframe.R
```