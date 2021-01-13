# RDD编程指南

- [总览](http://spark.apache.org/docs/latest/rdd-programming-guide.html#overview)  

- [与Spark链接](http://spark.apache.org/docs/latest/rdd-programming-guide.html#linking-with-spark)  

- 初始化Spark

  - [使用Shell](http://spark.apache.org/docs/latest/rdd-programming-guide.html#using-the-shell)  

- 弹性分布式数据集（RDD）

  - [并行集合](http://spark.apache.org/docs/latest/rdd-programming-guide.html#parallelized-collections)  

  - [外部数据集](http://spark.apache.org/docs/latest/rdd-programming-guide.html#external-datasets)  

  - RDD操作

    - [基本](http://spark.apache.org/docs/latest/rdd-programming-guide.html#basics)  

    - [将函数传递给Spark](http://spark.apache.org/docs/latest/rdd-programming-guide.html#passing-functions-to-spark)  

    - 了解闭包

      - [例](http://spark.apache.org/docs/latest/rdd-programming-guide.html#example)  
- [本地与集群模式](http://spark.apache.org/docs/latest/rdd-programming-guide.html#local-vs-cluster-modes)  
      - [RDD的打印元素](http://spark.apache.org/docs/latest/rdd-programming-guide.html#printing-elements-of-an-rdd)  
      
    - [使用键值对](http://spark.apache.org/docs/latest/rdd-programming-guide.html#working-with-key-value-pairs)  

    - [转变](http://spark.apache.org/docs/latest/rdd-programming-guide.html#transformations)  

    - [动作](http://spark.apache.org/docs/latest/rdd-programming-guide.html#actions)  

    - Shuffle 操作

      - [背景](http://spark.apache.org/docs/latest/rdd-programming-guide.html#background)  
  - [性能影响](http://spark.apache.org/docs/latest/rdd-programming-guide.html#performance-impact)  
    
  - RDD持久性

    - [选择哪个存储级别？](http://spark.apache.org/docs/latest/rdd-programming-guide.html#which-storage-level-to-choose)  
  - [移除缓存数据](http://spark.apache.org/docs/latest/rdd-programming-guide.html#removing-data)  
  
- 共享变量

  - [广播变量](http://spark.apache.org/docs/latest/rdd-programming-guide.html#broadcast-variables)  
  - [累加器](http://spark.apache.org/docs/latest/rdd-programming-guide.html#accumulators)  

- [部署到集群](http://spark.apache.org/docs/latest/rdd-programming-guide.html#deploying-to-a-cluster)  

- [从Java / Scala启动Spark作业](http://spark.apache.org/docs/latest/rdd-programming-guide.html#launching-spark-jobs-from-java--scala)  

- [单元测试](http://spark.apache.org/docs/latest/rdd-programming-guide.html#unit-testing)  

- [从这往哪儿走](http://spark.apache.org/docs/latest/rdd-programming-guide.html#where-to-go-from-here)  

# 总览

在较高级别上，每个Spark应用程序都包含一个*驱动程序*，该*程序*运行用户的`main`功能并在集群上执行各种*并行操作*。Spark提供的主要抽象是*弹性分布式数据集*（RDD），它是跨集群节点划分的元素的集合，可以并行操作。通过从Hadoop文件系统（或任何其他Hadoop支持的文件系统）中的文件或驱动程序中现有的Scala集合开始并进行转换来创建RDD。用户还可以要求Spark将RDD*保留*在内存中，以使其能够在并行操作中有效地重用。最后，RDD会自动从节点故障中恢复。

Spark中的第二个抽象是可以在并行操作中使用的*共享变量*。默认情况下，当Spark作为一组任务在不同节点上并行运行一个函数时，它会将函数中使用的每个变量的副本传送给每个任务。有时，需要在任务之间或任务与驱动程序之间共享变量。Spark支持两种类型的共享变量：*广播变量*（可用于在所有节点上的内存中缓存值）和*累加器（accumulator）*，这些变量仅被“添加”到其上，例如计数器和总和。

本指南以Spark的每种受支持的语言显示了所有这些功能。如果启动Spark的交互式Shell，无论`bin/spark-shell`是Scala Shell还是`bin/pyspark`Python Shell， 最简单的方法就是跟随它。

# 与Spark链接

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_0)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_0)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_0)  

默认情况下，Spark 3.0.1已构建并分发以与Scala 2.12一起使用。（也可以将Spark构建为与其他版本的Scala一起使用。）要在Scala中编写应用程序，您将需要使用兼容的Scala版本（例如2.12.X）。

要编写Spark应用程序，您需要在Spark上添加Maven依赖项。可通过Maven Central在以下位置获得Spark：

```
groupId = org.apache.spark
artifactId = spark-core_2.12
version = 3.0.1
```

另外，如果您想访问HDFS群集，则需要`hadoop-client`为您的HDFS版本添加依赖项 。

```
groupId = org.apache.hadoop
artifactId = hadoop-client
version = <your-hdfs-version>
```

最后，您需要将一些Spark类导入程序。添加以下行：

```
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
```

（在Spark 1.3.0之前，您需要显式`import org.apache.spark.SparkContext._`启用必要的隐式转换。）

# 初始化Spark

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_1)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_1)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_1)  

Spark程序必须做的第一件事是创建一个[SparkContext](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/SparkContext.html)  对象，该对象告诉Spark如何访问集群。要创建一个，`SparkContext`您首先需要构建一个[SparkConf](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/SparkConf.html)  对象，其中包含有关您的应用程序的信息。

每个JVM仅应激活一个SparkContext。`stop()  `创建新的SparkContext之前，您必须先激活它。

```
val conf = new SparkConf()  .setAppName(appName)  .setMaster(master)  
new SparkContext(conf)  
```

该`appName`参数是您的应用程序在集群UI上显示的名称。 `master`是[Spark，Mesos或YARN群集URL](http://spark.apache.org/docs/latest/submitting-applications.html#master-urls)  或特殊的“本地”字符串，以本地模式运行。实际上，当在集群上运行时，您将不希望`master`在程序中进行硬编码，而是在其中[启动应用程序`spark-submit`](http://spark.apache.org/docs/latest/submitting-applications.html)  并在其中接收。但是，对于本地测试和单元测试，您可以传递“ local”以在内部运行Spark。

## 使用Shell

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_2)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_2)  

在Spark Shell中，已经在名为的变量中为您创建了一个特殊的可识别解释器的SparkContext `sc`。制作自己的SparkContext无效。您可以使用`--master`参数设置上下文连接到哪个主机，还可以通过将逗号分隔的列表传递给参数来将JAR添加到类路径`--jars`。您还可以通过在`--packages`参数中提供逗号分隔的Maven坐标列表，从而将依赖项（例如Spark Packages）添加到Shell会话中。可以存在依赖项的任何其他存储库（例如Sonatype）都可以传递给`--repositories`参数。例如，要`bin/spark-shell`在四个核心上运行，请使用：

```shell
$ ./bin/spark-shell --master local[4]
```

或者，也要添加`code.jar`到其类路径中，请使用：

```shell
$ ./bin/spark-shell --master local[4] --jars code.jar
```

要使用Maven坐标包含依赖项，请执行以下操作：

```shell
$ ./bin/spark-shell --master local[4] --packages "org.example:example:0.1"
```

有关选项的完整列表，请运行`spark-shell --help`。在后台， `spark-shell`调用更通用的[`spark-submit`脚本](http://spark.apache.org/docs/latest/submitting-applications.html)  。

# 弹性分布式数据集（RDD）

Spark围绕着*弹性分布式数据集*（RDD）的概念，RDD是可并行操作的元素的容错集合。创建RDD的方法有两种：*并行化* 驱动程序中的现有集合，或引用外部存储系统（例如共享文件系统，HDFS，HBase或提供Hadoop InputFormat的任何数据源）中的数据集。

## 并行集合

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_3)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_3)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_3)  

通过在驱动程序（Scala ）中的现有集合上调用`SparkContext`的`parallelize`方法来创建并行集合`Seq`。复制集合中的元素以形成可以并行操作的分布式数据集。例如，以下是创建包含数字1到5的并行化集合的方法：

```scala
val data = Array(1, 2, 3, 4, 5)  
val distData = sc.parallelize(data)  
```

创建后，分布式数据集（`distData`）可以并行操作。例如，我们可能会调用`distData.reduce((a, b)   => a + b)  `以添加数组的元素。我们稍后将描述对分布式数据集的操作。

并行集合的一个重要参数是将数据集切入的*分区*数。Spark将为集群的每个分区运行一个任务。通常，群集中的每个CPU都需要2-4个分区。通常，Spark会尝试根据您的集群自动设置分区数。但是，您也可以通过将其作为第二个参数传递给`parallelize`（例如`sc.parallelize(data, 10)  `）来手动设置它。注意：代码中的某些地方使用术语片（分区的同义词）来保持向后兼容性。

## 外部数据集

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_4)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_4)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_4)  

Spark可以从Hadoop支持的任何存储源创建分布式数据集，包括您的本地文件系统，HDFS，Cassandra，HBase，[Amazon S3](http://wiki.apache.org/hadoop/AmazonS3)  等。Spark支持文本文件，[SequenceFiles](https://hadoop.apache.org/docs/stable/api/org/apache/hadoop/mapred/SequenceFileInputFormat.html)  和任何其他Hadoop [InputFormat](http://hadoop.apache.org/docs/stable/api/org/apache/hadoop/mapred/InputFormat.html)  。

可以使用`SparkContext`的`textFile`方法创建文本文件RDD 。此方法需要一个URI的文件（本地路径 file://)  的机器上，或一个`hdfs://`，`s3a://`等URI），并读取其作为行的集合。这是一个示例调用：

```shell
scala> val distFile = sc.textFile("data.txt")  
distFile: org.apache.spark.rdd.RDD[String] = data.txt MapPartitionsRDD[10] at textFile at <console>:26
```

一旦创建，`distFile`就可以通过数据集操作对其进行操作。例如，我们可以使用`map`和`reduce`操作将所有行的大小相加，如下所示：`distFile.map(s => s.length)  .reduce((a, b)   => a + b)  `。

关于使用Spark读取文件的一些注意事项：

- 如果在本地文件系统上使用路径，则还必须在工作节点上的相同路径上访问该文件。将文件复制给所有worker节点，或者使用网络安装的共享文件系统。
- Spark的所有基于文件的输入法（包括`textFile`）都支持在目录，压缩文件和通配符上运行。例如，你可以使用`textFile("/my/directory")  `，`textFile("/my/directory/*.txt")  `和`textFile("/my/directory/*.gz")  `。
- 该`textFile`方法还采用一个可选的第二个参数来控制文件的分区数。默认情况下，Spark为文件的每个块创建一个分区（HDFS中的块默认为128MB），但是您也可以通过传递更大的值来请求更大数量的分区。请注意，分区不能少于块。

除了文本文件，Spark的Scala API还支持其他几种数据格式：

- `SparkContext.wholeTextFiles`使您可以读取包含多个小文本文件的目录，并将每个小文本文件作为（文件名，内容）对返回。与相比`textFile`，会在每个文件的每一行返回一条记录。分区由数据局部性决定，在某些情况下，数据局部性可能导致分区太少。对于这些情况，`wholeTextFiles`提供一个可选的第二个参数来控制最小数量的分区。
- 对于[SequenceFiles](https://hadoop.apache.org/docs/stable/api/org/apache/hadoop/mapred/SequenceFileInputFormat.html)  ，请使用SparkContext的`sequenceFile[K, V]`方法，其中`K`和`V`是文件中键和值的类型。这些应该是Hadoop的[Writable](https://hadoop.apache.org/docs/stable/api/org/apache/hadoop/io/Writable.html)  接口的子类，例如[IntWritable](https://hadoop.apache.org/docs/stable/api/org/apache/hadoop/io/IntWritable.html)  和[Text](https://hadoop.apache.org/docs/stable/api/org/apache/hadoop/io/Text.html)  。另外，Spark允许您为一些常见的Writables指定本机类型。例如，`sequenceFile[Int, String]`将自动读取IntWritables和Texts。
- 对于其他Hadoop InputFormat，可以使用该`SparkContext.hadoopRDD`方法，该方法采用任意`JobConf`输入格式类，键类和值类。使用与输入源的Hadoop作业相同的方式设置这些内容。您还可以`SparkContext.newAPIHadoopRDD`基于“新” MapReduce API（`org.apache.hadoop.mapreduce`）将其用于InputFormats 。
- `RDD.saveAsObjectFile`并`SparkContext.objectFile`支持以包含序列化Java对象的简单格式保存RDD。虽然这不像Avro这样的专用格式有效，但它提供了一种保存任何RDD的简便方法。

## RDD操作

RDD支持两种类型的操作：*转换*（从现有操作创建新的数据集）和*动作（操作）*，在对数据集执行计算后，将值返回给驱动程序。例如，`map`是一个转换，该转换将每个数据集元素都传递给一个函数，并返回代表结果的新RDD。另一方面，这`reduce`是一项使用某些函数汇总RDD的所有元素并将最终结果返回给驱动程序的操作（尽管也有并行操作`reduceByKey`返回了分布式数据集）。

Spark中的所有转换都是*惰性的*，因为它们不会立即计算出结果。相反，他们只记得应用于某些基本数据集（例如文件）的转换。仅当动作要求将结果返回给驱动程序时才计算转换。这种设计使Spark可以更高效地运行。例如，我们可以认识到通过创建的数据集`map`将用于中，`reduce`并且仅将结果返回`reduce`给驱动程序，而不是将较大的映射数据集返回给驱动程序。

默认情况下，每次在其上执行操作时，可能会重新计算每个转换后的RDD。但是，您也可以使用`persist`（或`cache`）方法将RDD*保留*在内存中，在这种情况下，Spark会将元素保留在群集中，以便下次查询时可以更快地进行访问。还支持将RDD持久保存在磁盘上，或在多个节点之间复制。 

### 基本

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_5)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_5)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_5)  

为了说明RDD基础知识，请考虑以下简单程序：

```scala
val lines = sc.textFile("data.txt")  
val lineLengths = lines.map(s => s.length)  
val totalLength = lineLengths.reduce((a, b)   => a + b)  
```

第一行从外部文件定义基本RDD。该数据集未加载到内存中或没有采取其他行动：`lines`仅是文件的指针。第二行定义`lineLengths`为`map`转换的结果。再次，`lineLengths` 是*不是*马上计算，由于懒惰。最后，我们运行`reduce`，这是一个动作。此时，Spark将计算分解为任务，以在不同的机器上运行，每台机器既运行其映射的一部分，又运行本地还原，仅将其答案返回给驱动程序。

如果我们以后还要使用`lineLengths`，可以添加：

```scala
lineLengths.persist()  
```

在之前`reduce`，这将导致`lineLengths`在第一次计算后将其保存在内存中。

### 将函数传递给Spark

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_6)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_6)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_6)  

Spark的API在很大程度上依赖于在驱动程序中传递函数以在集群上运行。有两种推荐的方法可以做到这一点：

- [匿名函数语法](http://docs.scala-lang.org/tour/basics.html#functions)  ，可用于简短的代码段。
- 全局单例对象中的静态方法。例如，您可以如下定义`object MyFunctions`并传递`MyFunctions.func1`：

```scala
object MyFunctions {
  def func1(s: String)  : String = { ... }
}

myRdd.map(MyFunctions.func1)  
```

请注意，虽然也可以在类实例中传递对方法的引用（与单例对象相对），但这需要将包含该类的对象与方法一起发送。例如，考虑：

```scala
class MyClass {
  def func1(s: String)  : String = { ... }
  def doStuff(rdd: RDD[String])  : RDD[String] = { rdd.map(func1)   }
}
```

在这里，如果我们创建一个新的`MyClass`实例，并调用`doStuff`就可以了，`map`里面有引用的 `func1`方法*是的`MyClass`实例*，所以整个对象需要被发送到群集。它类似于写作`rdd.map(x => this.func1(x)  )  `。

以类似的方式，访问外部对象的字段将引用整个对象：

```scala
class MyClass {
  val field = "Hello"
  def doStuff(rdd: RDD[String])  : RDD[String] = { rdd.map(x => field + x)   }
}
```

等同于写作`rdd.map(x => this.field + x)  `，它引用了所有的`this`。为避免此问题，最简单的方法是将其复制`field`到局部变量中，而不是从外部访问它：

```scala
def doStuff(rdd: RDD[String])  : RDD[String] = {
  val field_ = this.field
  rdd.map(x => field_ + x)  
}
```

### 了解闭包 

关于Spark的难点之一是在跨集群执行代码时了解变量和方法的范围和生命周期。修改超出其范围的变量的RDD操作可能经常引起混乱。在下面的示例中，我们将查看`foreach()  `用于增加计数器的代码，但是其他操作也会发生类似的问题。

#### 例

考虑以下本机的RDD元素总和，其行为可能会有所不同，具体取决于执行是否在同一JVM中进行。一个常见的示例是在`local`模式（`--master = local[n]`）中运行Spark,而不是将Spark应用程序部署到集群（例如，通过将spark-submit提交给YARN）时：

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_7)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_7)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_7)  

```scala
var counter = 0
var rdd = sc.parallelize(data)  

// Wrong: Don't do this!!
rdd.foreach(x => counter += x)  

println("Counter value: " + counter)  
```

#### 本地与集群模式

上面的代码的行为是未定义的，可能无法按预期工作。为了执行作业，Spark将RDD操作的处理分解为任务，每个任务都由执行程序执行。在执行之前，Spark计算任务的**闭包**。闭包是执行者在RDD上执行其计算时必须可见的那些变量和方法（在本例中为`foreach()  `）。此闭包被序列化并发送给每个执行器。

发送给每个执行程序的闭包中的变量现在是副本，因此，在函数中引用**计数器**时`foreach`，它不再是驱动程序节点上的**计数器**。驱动程序节点的内存中仍然存在一个**计数器**，但是执行者将不再看到该**计数器**！执行者仅从序列化闭包中看到副本。因此，因为对**计数器的**所有操作都引用了序列化闭包内的值，所以**counter**的最终值仍将为零。

在本地模式下，在某些情况下，该`foreach`函数实际上将在与驱动程序相同的JVM中执行，并且将引用相同的原始**计数器**，并且实际上可能会对其进行更新。

为确保在此类情况下行为明确，应使用[`Accumulator`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#accumulators)  。Spark中的累加器专门用于提供一种机制，用于在集群中的各个工作节点之间拆分执行时安全地更新变量。本指南的“累加器”部分将详细讨论这些内容。

通常，闭包-诸如循环或局部定义的方法之类的构造，不应用于突变某些全局状态。Spark不定义或保证从闭包外部引用的对象的突变行为。某些执行此操作的代码可能会在本地模式下工作，但这只是偶然的情况，此类代码在分布式模式下将无法按预期运行。如果需要某些全局聚合，请使用累加器。

#### RDD的打印元素

另一个常见用法是尝试使用`rdd.foreach(println)  `或打印RDD的元素`rdd.map(println)  `。在单台机器上，这将生成预期的输出并打印所有RDD的元素。但是，在`cluster`模式下，`stdout`执行者要调用的输出现在`stdout`改为写入执行者的输出，而不是驱动程序上的那个，因此`stdout`驱动程序不会显示这些！要打印在驱动器的所有元素，可以使用的`collect()  `方法，首先使RDD到驱动器节点从而：`rdd.collect()  .foreach(println)  `。但是，这可能会导致驱动程序内存不足，因为`collect()  `将整个RDD提取到一台计算机上。如果只需要打印RDD的一些元素，则更安全的方法是使用`take()  `：`rdd.take(100)  .foreach(println)  `。

### 使用键值对

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_8)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_8)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_8)  

尽管大多数Spark操作可在包含任何类型的对象的RDD上运行，但一些特殊操作仅可用于键-值对的RDD。最常见的是分布式“Shuffle”操作，例如通过键对元素进行分组或聚合。

在Scala中，这些操作在包含[Tuple2](http://www.scala-lang.org/api/2.12.10/index.html#scala.Tuple2)  对象（该语言的内置元组，只需编写即可创建`(a, b)  `）的[RDD](http://www.scala-lang.org/api/2.12.10/index.html#scala.Tuple2)  上自动可用 。[PairRDDFunctions](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/PairRDDFunctions.html)  类中提供键值对操作， 该类会自动包装RDD元组。

例如，以下代码`reduceByKey`对键值对使用运算来计算文件中每一行文本出现的次数：

```scala
val lines = sc.textFile("data.txt")  
val pairs = lines.map(s => (s, 1)  )  
val counts = pairs.reduceByKey((a, b)   => a + b)  
```

`counts.sortByKey()  `例如，我们还可以使用按字母顺序对，最后 `counts.collect()  `将它们作为对象数组带回到驱动程序中。

**注意：**在键-值对操作中使用自定义对象作为键时，必须确保自定义`equals()  `方法与匹配`hashCode()  `方法一起使用。有关完整的详细信息，请参见[Object.hashCode（）文档中](https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--)  概述的合同。

### Transformations (转换)  

下表列出了Spark支持的一些常见转换。有关详细信息，请参考RDD API文档（[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html)  ， [Java](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/api/java/JavaRDD.html)  ， [Python](http://spark.apache.org/docs/latest/api/python/pyspark.html#pyspark.RDD)  ， [R](http://spark.apache.org/docs/latest/api/R/index.html)  ）和RDD函数对doc（[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/PairRDDFunctions.html)  ， [Java](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/api/java/JavaPairRDD.html)  ）。

| **Transformation** (转换)                                    | **Meaning** (含义)                                           |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| **map**（*func*）                                            | 返回一个新的分布式数据集，该数据集是通过将源的每个元素传递给函数*func形成的*。 |
| **filter**（*func*）                                         | 返回一个新的数据集，该数据集是通过选择源中*func*返回true的那些元素形成的。 |
| **flatMap**（*func*）                                        | 与map相似，但是每个输入项都可以映射到0个或多个输出项（因此*func*应该返回Seq而不是单个项）。 |
| **mapPartitions**（*func*）                                  | 与map相似，但是分别在RDD的每个分区（块）上运行，因此*func*在类型T的RDD上运行时必须为Iterator <T> => Iterator <U>类型。 |
| **mapPartitionsWithIndex**（*func*）                         | 与mapPartitions类似，但它还为*func*提供表示分区索引的整数值，因此当在类型T的RDD上运行时，*func*必须为（Int，Iterator <T>）=> Iterator <U>类型。 |
| **sample**（*withReplacement*，*fraction*，*seed*）          | 试分数*分数*的数据的，具有或不具有替换，使用给定的随机数发生器的种子。 |
| **union**（*otherDataset*）                                  | 返回一个新的数据集，其中包含源数据集中的元素和参数的并集。   |
| **intersection**（*otherDataset*）                           | 返回一个新的RDD，其中包含源数据集中的元素和参数的交集。      |
| **distinct**（[ *numPartitions* ]））                        | 返回一个新的数据集，其中包含源数据集的不同元素。             |
| **groupByKey**（[ *numPartitions* ]）                        | 在（K，V）对的数据集上调用时，返回（K，Iterable <V>）对的数据集。 **注意：**如果要分组以便对每个键执行聚合（例如求和或平均值），则使用`reduceByKey`或`aggregateByKey`将产生更好的性能。 **注意：**默认情况下，输出中的并行度取决于父RDD的分区数。您可以传递一个可选`numPartitions`参数来设置不同数量的任务。 |
| **reduceByKey**（*func*，[ *numPartitions* ]）               | 在（K，V）对的数据集上调用时，返回（K，V）对的数据集，其中每个键的值使用给定的reduce函数*func*进行汇总，该函数必须为（V，V）=> V.与in一样`groupByKey`，reduce任务的数量可以通过可选的第二个参数配置。 |
| **aggregateByKey**（*zeroValue*）（*seqOp*，*combOp*，[ *numPartitions* ]） | 在（K，V）对的数据集上调用时，返回（K，U）对的数据集，其中每个键的值使用给定的Combine函数和中性的“零”值进行汇总。允许与输入值类型不同的聚合值类型，同时避免不必要的分配。像in中一样`groupByKey`，reduce任务的数量可以通过可选的第二个参数进行配置。 |
| **sortByKey**（[*ascending*]，[ *numPartitions* ]）          | 在由K实现Ordered的（K，V）对的数据集上调用时，返回（K，V）对的数据集，按布尔值指定，按键以升序或降序排序`ascending`。 |
| **join**（*otherDataset*，[ *numPartitions* ]）              | 在（K，V）和（K，W）类型的数据集上调用时，返回（K，（V，W））对的数据集，其中每个键都有所有成对的元素。外连接通过支持`leftOuterJoin`，`rightOuterJoin`和`fullOuterJoin`。 |
| **cogroup**（*otherDataset*，[ *numPartitions* ]）           | 在（K，V）和（K，W）类型的数据集上调用时，返回（K，（Iterable <V>，Iterable <W>））元组的数据集。此操作也称为`groupWith`。 |
| **cartesian**（*otherDataset*）                              | 在类型T和U的数据集上调用时，返回（T，U）对（所有元素对）的数据集。 |
| **pipe**（*command*，*[envVars]*）                           | 通过Shell命令（例如Perl或bash脚本）通过管道传输RDD的每个分区。将RDD元素写入进程的stdin，并将输出到其stdout的行作为字符串的RDD返回。 |
| **coalesce**（*numPartitions*）                              | 将RDD中的分区数减少到numPartitions。筛选大型数据集后，对于更有效地运行操作很有用。 |
| **repartition**（*numPartitions*）                           | 随机地重新随机排列RDD中的数据，以创建更多或更少的分区，并在整个分区之间保持平衡。这总是会通过网络重新整理所有数据。 |
| **repartitionAndSortWithinPartitions**（*partitioner*）      | 根据给定的分区程序对RDD进行重新分区，并在每个结果分区中，按其键对记录进行排序。这比`repartition`在每个分区内调用然后排序更为有效，因为它可以将排序推入洗牌机制。 |

### Actions (行动)  

下表列出了Spark支持的一些常见操作。请参考RDD API文档（[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/RDD.html)  ， [Java](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/api/java/JavaRDD.html)  ， [Python](http://spark.apache.org/docs/latest/api/python/pyspark.html#pyspark.RDD)  ， [R](http://spark.apache.org/docs/latest/api/R/index.html)  ）

并配对RDD函数doc（[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/PairRDDFunctions.html)  和 [Java](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/api/java/JavaPairRDD.html)  ）以获取详细信息。

| Actions (行动)                                       | **Meaning** (含义)                                           |
| :--------------------------------------------------- | :----------------------------------------------------------- |
| **reduce**（*func*）                                 | 使用函数*func*（使用两个参数并返回一个参数）聚合数据集的元素。该函数应该是可交换的和关联的，以便可以并行正确地计算它。 |
| **collect**（）                                      | 在驱动程序中将数据集的所有元素作为数组返回。这通常在返回足够小的数据子集的过滤器或其他操作之后很有用。 |
| **count**（）                                        | 返回数据集中的元素数。                                       |
| **first**（）                                        | 返回数据集的第一个元素（类似于take（1））。                  |
| **take**（*n*）                                      | 返回具有数据集的前*n个*元素的数组。                          |
| **takeSample**（*withReplacement*，*num*，[*seed*]） | 返回带有数据集*num个*元素的随机样本的数组，带有或不带有替换，可以选择预先指定一个随机数生成器种子。 |
| **takeOrdered**（*n*，*[ordering]*）                 | 使用自然顺序或自定义比较器返回RDD的前*n个*元素。             |
| **saveAsTextFile**（*path*）                         | 将数据集的元素以文本文件（或文本文件集）的形式写入本地文件系统，HDFS或任何其他Hadoop支持的文件系统中的给定目录中。Spark将在每个元素上调用toString，以将其转换为文件中的一行文本。 |
| **saveAsSequenceFile**（*path*） （Java and Scala）  | 将数据集的元素作为Hadoop SequenceFile写入本地文件系统，HDFS或任何其他Hadoop支持的文件系统中的给定路径中。这在实现Hadoop的Writable接口的键/值对的RDD上可用。在Scala中，它也可用于隐式转换为Writable的类型（Spark包括对基本类型（如Int，Double，String等）的转换。 |
| **saveAsObjectFile**（*path*） （Java and Scala）    | 使用Java序列化以简单的格式编写数据集的元素，然后可以使用进行加载 `SparkContext.objectFile()  `。 |
| **countByKey**（）                                   | 仅在类型（K，V）的RDD上可用。返回（K，Int）对的哈希图以及每个键的计数。 |
| **foreach**（*func*）                                | 在数据集的每个元素上运行函数*func*。通常这样做是出于副作用，例如更新[累加器](http://spark.apache.org/docs/latest/rdd-programming-guide.html#accumulators)  或与外部存储系统交互。 **注意**：在之外修改除累加器以外的变量`foreach()  `可能会导致未定义的行为。有关更多详细信息，请参见[了解闭包](http://spark.apache.org/docs/latest/rdd-programming-guide.html#understanding-closures-a-nameclosureslinka)  。 |

Spark RDD API还公开了某些操作的异步版本，例如`foreachAsync`for `foreach`，它会立即`FutureAction`向调用方返回a ，而不是在操作完成时阻止。这可用于管理或等待动作的异步执行。

### Shuffle 操作

Spark中的某些操作会触发一个称为Shuffle的事件。Shuffle是Spark的一种用于重新分配数据的机制，以便跨分区对数据进行不同的分组。这通常涉及跨执行程序和机器复制数据，从而使Shuffle变得复杂而昂贵。

*TODO* to know about shuffle

#### 背景

要了解Shuffle期间发生的情况，我们可以考虑[`reduceByKey`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#ReduceByLink)  操作示例 。该`reduceByKey`操作将生成一个新的RDD，其中将单个键的所有值组合为一个元组-该键以及针对与该键关联的所有值执行reduce函数的结果。挑战在于，并非单个键的所有值都必须位于同一分区，甚至同一台机器上，但是必须将它们放在同一位置才能计算结果。

在Spark中，数据通常不会跨分区分布在特定操作的必要位置。在计算期间，单个任务将在单个分区上运行-因此，要组织所有数据`reduceByKey`以执行单个reduce任务，Spark需要执行所有操作。它必须从所有分区读取以找到所有键的所有值，然后将各个分区的值汇总在一起以计算每个键的最终结果-这称为**shuffle**。

尽管新改组后的数据的每个分区中的元素集都是确定性的，分区本身的顺序也是如此，但这些元素的顺序不是确定性的。如果人们希望在改组后可以预期地排序数据，则可以使用：

- `mapPartitions` 使用例如，对每个分区进行排序 `.sorted`
- `repartitionAndSortWithinPartitions` 在对分区进行有效排序的同时进行重新分区
- `sortBy` 生成全局排序的RDD

这可能会导致一个洗牌的操作包括**重新分区**一样操作 [`repartition`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#RepartitionLink)  和[`coalesce`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#CoalesceLink)  ，**ByKey”**操作，比如（除计数）[`groupByKey`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#GroupByLink)  和[`reduceByKey`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#ReduceByLink)  ，并 **加入**操作，如[`cogroup`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#CogroupLink)  和[`join`](http://spark.apache.org/docs/latest/rdd-programming-guide.html#JoinLink)  。

#### 性能影响

所述**Shuffle**是昂贵的操作，因为它涉及的磁盘I / O，数据序列，和网络I / O。为了组织洗牌的数据，Spark生成任务集-*映射*任务以组织数据，以及一组*reduce*任务来聚合数据。此术语来自MapReduce，与Spark`map`和`reduce`操作没有直接关系。

在内部，单个地图任务的结果会保留在内存中，直到无法容纳为止。然后，根据目标分区对它们进行排序并写入单个文件。在简化方面，任务读取相关的已排序块。

某些混洗操作会消耗大量的堆内存，因为它们在转移它们之前或之后采用内存中的数据结构来组织记录。具体而言， `reduceByKey`并`aggregateByKey`创建在地图上侧这样的结构，和`'ByKey`操作产生这些上减少侧。当数据不适合内存时，Spark会将这些表溢出到磁盘上，从而产生磁盘I / O的额外开销并增加垃圾回收。

Shuffle还会在磁盘上生成大量中间文件。从Spark 1.3开始，将保留这些文件，直到不再使用相应的RDD并进行垃圾回收为止。这样做是为了在重新计算沿袭时无需重新创建Shuffle文件。如果应用程序保留了对这些RDD的引用，或者如果GC不经常启动，则可能需要很长一段时间才能进行垃圾回收。这意味着长时间运行的Spark作业可能会占用大量磁盘空间。`spark.local.dir`在配置Spark上下文时，临时存储目录由配置参数指定 。

可以通过调整各种配置参数来调整Shuffle行为。请参阅《[Spark配置指南](http://spark.apache.org/docs/latest/configuration.html)  》中的“Shuffle行为”部分。

## RDD持久性

Spark中最重要的功能之一是跨操作在内存中*持久化*（或*缓存*）数据集。当您保留RDD时，每个节点都会将其计算的所有分区存储在内存中，并在该数据集（或从其派生的数据集）上的其他操作中重用它们。这样可以使以后的操作更快（通常快10倍以上）。缓存是用于迭代算法和快速交互使用的关键工具。

您可以使用`persist()  `或`cache()  `方法将RDD标记为要保留。第一次在操作中对其进行计算时，它将被保存在节点上的内存中。Spark的缓存是容错的–如果RDD的任何分区丢失，它将使用最初创建它的转换自动重新计算。

此外，每个持久化的RDD可以使用不同的*存储级别*进行存储，例如，允许您将数据集持久化在磁盘上，持久化在内存中，但作为序列化的Java对象（以节省空间）在节点之间复制。通过将一个`StorageLevel`对象（[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/storage/StorageLevel.html)  ， [Java](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/storage/StorageLevel.html)  ， [Python](http://spark.apache.org/docs/latest/api/python/pyspark.html#pyspark.StorageLevel)  ）传递给来设置这些级别 `persist()  `。该`cache()  `方法是使用默认存储级别`StorageLevel.MEMORY_ONLY`（将反序列化的对象存储在内存中）的简写。完整的存储级别集是：

| **Storage Level** (存储级别)         | **Meaning** (含义)                                           |
| :----------------------------------- | :----------------------------------------------------------- |
| MEMORY_ONLY                          | 将RDD作为反序列化的Java对象存储在JVM中。如果RDD不能容纳在内存中，则某些分区将不会被缓存，并且每次需要时都会即时重新计算。这是默认级别。 |
| MEMORY_AND_DISK                      | 将RDD作为反序列化的Java对象存储在JVM中。如果RDD不能容纳在内存中，请存储磁盘上不适合的分区，并在需要时从那里读取它们。 |
| MEMORY_ONLY_SER （Java和Scala）      | 将RDD存储为*序列化的*Java对象（每个分区一个字节数组）。通常，这比反序列化的对象更节省空间，尤其是在使用[快速序列化程序时](http://spark.apache.org/docs/latest/tuning.html)  ，但读取时会占用 更多CPU。 |
| MEMORY_AND_DISK_SER （Java和Scala）  | 与MEMORY_ONLY_SER类似，但是将内存中不适合的分区溢出到磁盘上，而不是在需要时即时对其进行重新计算。 |
| DISK_ONLY                            | 仅将RDD分区存储在磁盘上。                                    |
| MEMORY_ONLY_2，MEMORY_AND_DISK_2等。 | 与上面的级别相同，但是在两个群集节点上复制每个分区。         |
| OFF_HEAP（实验性）                   | 与MEMORY_ONLY_SER类似，但是将数据存储在 [堆外内存中](http://spark.apache.org/docs/latest/configuration.html#memory-management)  。这需要启用堆外内存。 |

**注意：** *在Python中，存储的对象将始终使用[Pickle](https://docs.python.org/2/library/pickle.html)  库进行序列化，因此，是否选择序列化级别都无关紧要。Python中的可用存储级别包括`MEMORY_ONLY`，`MEMORY_ONLY_2`， `MEMORY_AND_DISK`，`MEMORY_AND_DISK_2`，`DISK_ONLY`，和`DISK_ONLY_2`。*

`reduceByKey`即使没有用户调用，Spark也会自动将一些中间数据保留在随机操作中（例如）`persist`。这样做是为了避免在混洗期间节点发生故障时重新计算整个输入。我们仍然建议用户`persist`如果打算重复使用它，请调用生成的RDD。

### 选择哪个存储级别？

Spark的存储级别旨在在内存使用量和CPU效率之间提供不同的权衡。我们建议通过以下过程选择一个：

- 如果您的RDD适合默认存储级别（`MEMORY_ONLY`），则应保持这种状态。这是CPU效率最高的选项，允许RDD上的操作尽可能快地运行。
- 如果不是，请尝试使用`MEMORY_ONLY_SER`并[选择一个快速的序列化库，](http://spark.apache.org/docs/latest/tuning.html)  以使对象的空间效率更高，但访问速度仍然相当快。（Java和Scala）
- 除非用于计算数据集的函数非常昂贵，否则它们会泄漏到磁盘上，否则它们会过滤大量数据。否则，重新计算分区可能与从磁盘读取分区一样快。
- 如果要快速恢复故障，请使用复制的存储级别（例如，如果使用Spark来处理来自Web应用程序的请求）。*所有*存储级别都通过重新计算丢失的数据来提供完全的容错能力，但是复制的存储级别使您可以继续在RDD上运行任务，而不必等待重新计算丢失的分区。

### 移除数据

Spark自动监视每个节点上的缓存使用情况，并以最近最少使用（LRU）的方式丢弃旧的数据分区。如果您想手动删除一个RDD而不是等待它脱离缓存，请使用该`RDD.unpersist()  `方法。请注意，此方法默认情况下不会阻止。要阻塞直到释放资源，请`blocking=true`在调用此方法时指定。

# Shared Variables(共享变量)  

通常，当传递给Spark操作的函数（例如`map`或`reduce`）在远程集群节点上执行时，它将在该函数中使用的所有变量的单独副本上工作。这些变量将复制到每台计算机，并且远程计算机上的变量更新不会传播回驱动程序。在各个任务之间支持通用的读写共享变量将效率很低。但是，Spark确实为两种常用用法模式提供了两种有限类型的*共享变量*：广播变量和累加器。

## Broadcast Variables(广播变量)  

广播变量使程序员可以在每台计算机上保留一个只读变量，而不是将其副本与任务一起发送。例如，可以使用它们以有效的方式为每个节点提供大型输入数据集的副本。Spark还尝试使用有效的广播算法分配广播变量，以降低通信成本。

Spark动作是通过一组阶段执行的，这些阶段由分布式“Shuffle”操作分开。Spark自动广播每个阶段中任务所需的通用数据。在运行每个任务之前，以这种方式广播的数据以序列化形式缓存并反序列化。这意味着仅当跨多个阶段的任务需要相同数据或以反序列化形式缓存数据非常重要时，显式创建广播变量才有用。

`v`通过调用从变量创建广播变量`SparkContext.broadcast(v)  `。broadcast变量是的包装`v`，可以通过调用`value` 方法访问其值。下面的代码显示了这一点：

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_9)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_9)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_9)  

```shell
scala> val broadcastVar = sc.broadcast(Array(1, 2, 3)  )  
broadcastVar: org.apache.spark.broadcast.Broadcast[Array[Int]] = Broadcast(0)  

scala> broadcastVar.value
res0: Array[Int] = Array(1, 2, 3)  
```

创建广播变量之后，它应该被用来代替值`v`的集群，使上运行的任何功能`v`不运到节点超过一次。另外，在`v`广播对象后不应修改该对象 ，以确保所有节点都具有相同的广播变量值（例如，如果变量稍后被传送到新节点）。

要释放广播变量复制到执行程序上的资源，请调用`.unpersist()  `。如果此后再次使用广播，将重新广播。要永久释放广播变量使用的所有资源，调用 .destroy()  `。此后不能使用广播变量。请注意，这些方法默认情况下不会阻止。要阻塞直到释放资源，请`blocking=true`在调用它们时指定。

## Accumulators(累加器)  

累加器是仅通过关联和交换操作“累加”的变量，因此可以有效地并行支持。它们可用于实现计数器（如在MapReduce中）或总和。Spark本身支持数字类型的累加器，程序员可以添加对新类型的支持。

作为用户，您可以创建命名或未命名的累加器。如下图所示，一个已命名的累加器（在这种情况下`counter`）将在Web UI中显示修改该累加器的阶段。Spark在“任务”表中显示由任务修改的每个累加器的值。

![Spark UI中的累加器](http://spark.apache.org/docs/latest/img/spark-webui-accumulators.png)  

UI中的跟踪累加器对于了解运行阶段的进度很有用（注意：Python尚不支持此功能）。

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_10)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_10)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_10)  

可以通过分别调用`SparkContext.longAccumulator()  `或`SparkContext.doubleAccumulator()  ` 累加Long或Double类型的值来创建数字累加器。然后，可以使用`add`方法将在集群上运行的任务添加到集群中。但是，他们无法读取其值。只有驱动程序可以使用其`value`方法读取累加器的值。

下面的代码显示了一个累加器，用于累加一个数组的元素：

```shell
scala> val accum = sc.longAccumulator("My Accumulator")  
accum: org.apache.spark.util.LongAccumulator = LongAccumulator(id: 0, name: Some(My Accumulator)  , value: 0)  

scala> sc.parallelize(Array(1, 2, 3, 4)  )  .foreach(x => accum.add(x)  )  
...
10/09/29 18:41:08 INFO SparkContext: Tasks finished in 0.317106 s

scala> accum.value
res2: Long = 10
```

尽管此代码使用了对Long类型的累加器的内置支持，但程序员也可以通过对[AccumulatorV2](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/util/AccumulatorV2.html)  进行子类化来创建自己的类型。AccumulatorV2抽象类具有几种必须被覆盖的方法：`reset`将累加器重置为零，`add`将另一个值添加到累加器中， `merge`将另一个相同类型的累加器合并到该方法中。[API文档](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/util/AccumulatorV2.html)  中包含其他必须重写的方法。例如，假设我们有一个`MyVector`代表数学向量的类，我们可以这样写：

```scala
class VectorAccumulatorV2 extends AccumulatorV2[MyVector, MyVector] {

  private val myVector: MyVector = MyVector.createZeroVector

  def reset()  : Unit = {
    myVector.reset()  
  }

  def add(v: MyVector)  : Unit = {
    myVector.add(v)  
  }
  ...
}

// Then, create an Accumulator of this type:
val myVectorAcc = new VectorAccumulatorV2
// Then, register it into spark context:
sc.register(myVectorAcc, "MyVectorAcc1")  
```

请注意，当程序员定义自己的AccumulatorV2类型时，结果类型可能与所添加元素的类型不同。

对于**仅**在**操作**内部**执行的**累加器更新，Spark保证每个任务对累加器的更新将仅应用一次，即重新启动的任务将不会更新该值。在转换中，用户应意识到，如果重新执行任务或作业阶段，则可能不止一次应用每个任务的更新。

累加器不会更改Spark的惰性评估模型。如果在RDD上的操作中对其进行更新，则仅当将RDD计算为操作的一部分时才更新它们的值。因此，当在类似的惰性转换中进行累加器更新时，不能保证执行更新`map()  `。下面的代码片段演示了此属性：

- [**Scala**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_scala_11)  
- [**Java**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_java_11)  
- [**Python**](http://spark.apache.org/docs/latest/rdd-programming-guide.html#tab_python_11)  

```scala
val accum = sc.longAccumulator
data.map { x => accum.add(x)  ; x }
// Here, accum is still 0 because no actions have caused the map operation to be computed.
```

# 部署到集群

在[提交申请指南](http://spark.apache.org/docs/latest/submitting-applications.html)  介绍了如何提交申请到集群。简而言之，一旦将应用程序打包到JAR（对于Java / Scala）或一组`.py`或`.zip`文件（对于Python）中，该`bin/spark-submit`脚本便可以将其提交给任何受支持的集群管理器。

# 从Java / Scala启动Spark作业

该[org.apache.spark.launcher](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/launcher/package-summary.html)   包提供的类使用一个简单的Java API推出Spark工作的子进程。

# 单元测试

Spark非常适合使用任何流行的单元测试框架进行单元测试。只需`SparkContext`在测试中创建一个主URL设置为的`local`，运行您的操作，然后调用`SparkContext.stop()  `将其拆解。确保您在`finally`块或测试框架的`tearDown`方法中停止contexts，因为Spark不支持在同一程序中同时运行的两个contexts。

# 从这往哪儿走

您可以在Spark网站上看到一些[示例Spark程序](https://spark.apache.org/examples.html)  。此外，Spark在`examples`目录（[Scala](https://github.com/apache/spark/tree/master/examples/src/main/scala/org/apache/spark/examples)  ， [Java](https://github.com/apache/spark/tree/master/examples/src/main/java/org/apache/spark/examples)  ， [Python](https://github.com/apache/spark/tree/master/examples/src/main/python)  ， [R](https://github.com/apache/spark/tree/master/examples/src/main/r)  ）中包含几个示例。您可以通过将类名称传递给Spark的`bin/run-example`脚本来运行Java和Scala示例。例如：

```shell
./bin/run-example SparkPi
```

对于Python示例，请`spark-submit`改用：

```shell
./bin/spark-submit examples/src/main/python/pi.py
```

对于R示例，请`spark-submit`改用：

```shell
./bin/spark-submit examples/src/main/r/dataframe.R
```

为了帮助您优化程序，[配置](http://spark.apache.org/docs/latest/configuration.html)  和 [调优](http://spark.apache.org/docs/latest/tuning.html)  指南提供了有关最佳做法的信息。它们对于确保数据以有效格式存储在内存中尤其重要。为了获得部署方面的帮助，[集群模式概述](http://spark.apache.org/docs/latest/cluster-overview.html)  描述了分布式操作和支持的集群管理器中涉及的组件。

最后，[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/)  ，[Java](http://spark.apache.org/docs/latest/api/java/)  ，[Python](http://spark.apache.org/docs/latest/api/python/)  和[R中](http://spark.apache.org/docs/latest/api/R/)  提供了完整的API文档 。