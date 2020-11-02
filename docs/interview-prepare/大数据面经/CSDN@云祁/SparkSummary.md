**1.spark中的RDD是什么，有哪些特性？**

答：RDD（Resilient Distributed Dataset）叫做分布式数据集，是spark中最基本的数据抽象，它代表一个不可变，可分区，里面的元素可以并行计算的集合

Dataset：就是一个集合，用于存放数据的

Destributed：分布式，可以并行在集群计算

Resilient：表示弹性的，弹性表示

1.RDD中的数据可以存储在内存或者磁盘中；

2.RDD中的分区是可以改变的；

五大特性：

1.A list of partitions：一个分区列表，RDD中的数据都存储在一个分区列表中

2.A function for computing each split：作用在每一个分区中的函数

3.A list of dependencies on other RDDs：一个RDD依赖于其他多个RDD，这个点很重要，RDD的容错机制就是依据这个特性而来的

4.Optionally,a Partitioner for key-value RDDs(eg:to say that the RDD is hash-partitioned)：可选的，针对于kv类型的RDD才有这个特性，作用是决定了数据的来源以及数据处理后的去向

5.可选项，数据本地性，数据位置最优

2.概述一下spark中的常用算子区别（map,mapPartitions，foreach，foreachPatition）？

答：map：用于遍历RDD，将函数应用于每一个元素，返回新的RDD（transformation算子）

foreach：用于遍历RDD，将函数应用于每一个元素，无返回值（action算子）

mapPatitions：用于遍历操作RDD中的每一个分区，返回生成一个新的RDD（transformation算子）

foreachPatition：用于遍历操作RDD中的每一个分区，无返回值（action算子）

总结：一般使用mapPatitions和foreachPatition算子比map和foreach更加高效，推荐使用

3.谈谈spark中的宽窄依赖？

答：RDD和它的父RDD的关系有两种类型：窄依赖和宽依赖

宽依赖：指的是多个子RDD的Partition会依赖同一个父RDD的Partition，关系是一对多，父RDD的一个分区的数据去到子RDD的不同分区里面，会有shuffle的产生

窄依赖：指的是每一个父RDD的Partition最多被子RDD的一个partition使用，是一对一的，也就是父RDD的一个分区去到了子RDD的一个分区中，这个过程没有shuffle产生

区分的标准就是看父RDD的一个分区的数据的流向，要是流向一个partition的话就是窄依赖，否则就是宽依赖，如图所示：

![img](https://img-blog.csdn.net/20180909161853157?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

4.spark中如何划分stage？

答：概念：Spark任务会根据RDD之间的依赖关系，形成一个DAG有向无环图，DAG会提交给DAGScheduler，DAGScheduler会把DAG划分相互依赖的多个stage，划分依据就是宽窄依赖，遇到宽依赖就划分stage，每个stage包含一个或多个task，然后将这些task以taskSet的形式提交给TaskScheduler运行，stage是由一组并行的task组成

 

8.stage的task的并行度是由stage的最后一个RDD的分区数来决定的，一般来说，一个partition对应一个task，但最后reduce的时候可以手动改变reduce的个数，也就是改变最后一个RDD的分区数，也就改变了并行度。例如：reduceByKey(_+_,3)

9.优化 提高stage的并行度：reduceByKey(_+_,patition的个数) ，join(_+_,patition的个数)

10.DAGScheduler分析：

答：概述：是一个面向stage 的调度器；

主要入参有：dagScheduler.runJob(rdd, cleanedFunc, partitions, callSite, allowLocal,resultHandler, localProperties.get)

rdd： final RDD；

cleanedFunc： 计算每个分区的函数；

resultHander： 结果侦听器；

主要功能：1.接受用户提交的job；

2.将job根据类型划分为不同的stage，记录那些RDD，stage被物化，并在每一个stage内产生一系列的task，并封装成taskset；

3.决定每个task的最佳位置，任务在数据所在节点上运行，并结合当前的缓存情况，将taskSet提交给TaskScheduler；

4.重新提交shuffle输出丢失的stage给taskScheduler；

注：一个stage内部的错误不是由shuffle输出丢失造成的，DAGScheduler是不管的，由TaskScheduler负责尝试重新提交task执行。

5.Job的生成：

答：一旦driver程序中出现action，就会生成一个job，比如count等，向DAGScheduler提交job，如果driver程序后面还有action，那么其他action也会对应生成相应的job，所以，driver端有多少action就会提交多少job，这可能就是为什么spark将driver程序称为application而不是job 的原因。每一个job可能会包含一个或者多个stage，最后一个stage生成result，在提交job 的过程中，DAGScheduler会首先从后往前划分stage，划分的标准就是宽依赖，一旦遇到宽依赖就划分，然后先提交没有父阶段的stage们，并在提交过程中，计算该stage的task数目以及类型，并提交具体的task，在这些无父阶段的stage提交完之后，依赖该stage 的stage才会提交。

6.有向无环图：

答：DAG，有向无环图，简单的来说，就是一个由顶点和有方向性的边构成的图中，从任意一个顶点出发，没有任意一条路径会将其带回到出发点的顶点位置。通俗说就是所有任务的依赖关系。为每个spark job计算具有依赖关系的多个stage任务阶段，通常根据shuffle来划分stage，如reduceByKey,groupByKey等涉及到shuffle的transformation就会产生新的stage ，然后将每个stage划分为具体的一组任务，以TaskSets的形式提交给底层的任务调度模块来执行，其中不同stage之前的RDD为宽依赖关系，TaskScheduler任务调度模块负责具体启动任务，监控和汇报任务运行情况。

8.RDD的操作？

 

![img](https://img-blog.csdn.net/20180909162041368?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/2018090916204272?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909162101528?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909162110376?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/2018090916212499?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909162136156?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909163215275?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909163232304?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909163244483?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909163257291?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

9.RDD缓存？

Spark可以使用 persist 和 cache 方法将任意 RDD 缓存到内存、磁盘文件系统中。缓存是容错的，如果一个 RDD 分片丢失，可以通过构建它的 transformation自动重构。被缓存的 RDD 被使用的时，存取速度会被大大加速。一般的executor内存60%做 cache， 剩下的40%做task。

Spark中，RDD类可以使用cache() 和 persist() 方法来缓存。cache()是persist()的特例，将该RDD缓存到内存中。而persist可以指定一个StorageLevel。StorageLevel的列表可以在StorageLevel 伴生单例对象中找到。

Spark的不同StorageLevel ，目的满足内存使用和CPU效率权衡上的不同需求。我们建议通过以下的步骤来进行选择：

·如果你的RDDs可以很好的与默认的存储级别(MEMORY_ONLY)契合，就不需要做任何修改了。这已经是CPU使用效率最高的选项，它使得RDDs的操作尽可能的快。

·如果不行，试着使用MEMORY_ONLY_SER并且选择一个快速序列化的库使得对象在有比较高的空间使用率的情况下，依然可以较快被访问。

·尽可能不要存储到硬盘上，除非计算数据集的函数，计算量特别大，或者它们过滤了大量的数据。否则，重新计算一个分区的速度，和与从硬盘中读取基本差不多快。

·如果你想有快速故障恢复能力，使用复制存储级别(例如：用Spark来响应web应用的请求)。所有的存储级别都有通过重新计算丢失数据恢复错误的容错机制，但是复制存储级别可以让你在RDD上持续的运行任务，而不需要等待丢失的分区被重新计算。

·如果你想要定义你自己的存储级别(比如复制因子为3而不是2)，可以使用StorageLevel 单例对象的apply()方法。

在不会使用cached RDD的时候，及时使用unpersist方法来释放它。

10.RDD共享变量？

在应用开发中，一个函数被传递给Spark操作（例如map和reduce），在一个远程集群上运行，它实际上操作的是这个函数用到的所有变量的独立拷贝。这些变量会被拷贝到每一台机器。通常看来，在任务之间中，读写共享变量显然不够高效。然而，Spark还是为两种常见的使用模式，提供了两种有限的共享变量：广播变量和累加器。

(1). 广播变量（Broadcast Variables）

– 广播变量缓存到各个节点的内存中，而不是每个 Task

– 广播变量被创建后，能在集群中运行的任何函数调用

– 广播变量是只读的，不能在被广播后修改

– 对于大数据集的广播， Spark 尝试使用高效的广播算法来降低通信成本

1. val broadcastVar = sc.broadcast(Array(1, 2, 3))方法参数中是要广播的变量

(2). 累加器

累加器只支持加法操作，可以高效地并行，用于实现计数器和变量求和。Spark 原生支持数值类型和标准可变集合的计数器，但用户可以添加新的类型。只有驱动程序才能获取累加器的值

11.spark-submit的时候如何引入外部jar包：

在通过spark-submit提交任务时，可以通过添加配置参数来指定 

- –driver-class-path 外部jar包
- –jars 外部jar包

13.spark中cache和persist的区别？14.spark分布式集群搭建的步骤？

- cache：缓存数据，默认是缓存在内存中，其本质还是调用persist
- persist:缓存数据，有丰富的数据缓存策略。数据可以保存在内存也可以保存在磁盘中，使用的时候指定对应的缓存级别就可以了。

![img](https://img-blog.csdn.net/20180909163054975?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

地球人都知道

- 这里可以概述下如何搭建高可用的spark集群（HA） 

- - 主要是引入了zookeeper

15.spark中的数据倾斜的现象，原因，后果？

- (1)、数据倾斜的现象 

- - 多数task执行速度较快,少数task执行时间非常长，或者等待很长时间后提示你内存不足，执行失败。

- (2)、数据倾斜的原因 

- - 数据问题 

- - - 1、key本身分布不均衡（包括大量的key为空）
    - 2、key的设置不合理

- - spark使用问题 

- - - 1、shuffle时的并发度不够
    - 2、计算方式有误

- (3)、数据倾斜的后果 

- - 1、spark中的stage的执行时间受限于最后那个执行完成的task,因此运行缓慢的任务会拖垮整个程序的运行速度（分布式程序运行的速度是由最慢的那个task决定的）。
  - 2、过多的数据在同一个task中运行，将会把executor撑爆。

18.kafka整合sparkStreaming问题？

20.spark master在使用zookeeper进行HA时，有哪些元数据保存在zookeeper？

答：spark通过这个参数spark.deploy.zookeeper.dir指定master元数据在zookeeper中保存的位置，包括worker,master,application,executors.standby节点要从zk中获得元数据信息，恢复集群运行状态，才能对外继续提供服务，作业提交资源申请等，在恢复前是不能接受请求的，另外，master切换需要注意两点：

1.在master切换的过程中，所有的已经在运行的程序皆正常运行，因为spark application在运行前就已经通过cluster manager获得了计算资源，所以在运行时job本身的调度和处理master是没有任何关系的；

2.在master的切换过程中唯一的影响是不能提交新的job，一方面不能提交新的应用程序给集群，因为只有Active master才能接受新的程序的提交请求，另外一方面，已经运行的程序也不能action操作触发新的job提交请求。

21.spark master HA主从切换过程不会影响集群已有的作业运行，为什么？

答：因为程序在运行之前，已经向集群申请过资源，这些资源已经提交给driver了，也就是说已经分配好资源了，这是粗粒度分配，一次性分配好资源后不需要再关心资源分配，在运行时让driver和executor自动交互，弊端是如果资源分配太多，任务运行完不会很快释放，造成资源浪费，这里不适用细粒度分配的原因是因为任务提交太慢。

22.什么是粗粒度，什么是细粒度，各自的优缺点是什么？

答：1.粗粒度：启动时就分配好资源，程序启动，后续具体使用就使用分配好的资源，不需要再分配资源。好处：作业特别多时，资源复用率较高，使用粗粒度。缺点：容易资源浪费，如果一个job有1000个task，完成了999个，还有一个没完成，那么使用粗粒度。如果有999个资源闲置在那里，会造成资源大量浪费。

2.细粒度：用资源的时候分配，用完了就立即回收资源，启动会麻烦一点，启动一次分配一次，会比较麻烦。

23.driver的功能是什么？

答：1.一个spark作业运行时包括一个driver进程，也就是作业的主进程，具有main函数，并且有sparkContext的实例，是程序的入口；

2.功能：负责向集群申请资源，向master注册信息，负责了作业的调度，负责了作业的解析，生成stage并调度task到executor上，包括DAGScheduler，TaskScheduler。

24.spark的有几种部署模式，每种模式特点？

1）本地模式

Spark不一定非要跑在hadoop集群，可以在本地，起多个线程的方式来指定。将Spark应用以多线程的方式直接运行在本地，一般都是为了方便调试，本地模式分三类

· local：只启动一个executor

· local[k]:启动k个executor

· local：启动跟cpu数目相同的 executor

2)standalone模式

分布式部署集群， 自带完整的服务，资源管理和任务监控是Spark自己监控，这个模式也是其他模式的基础，

3)Spark on yarn模式

分布式部署集群，资源和任务监控交给yarn管理，但是目前仅支持粗粒度资源分配方式，包含cluster和client运行模式，cluster适合生产，driver运行在集群子节点，具有容错功能，client适合调试，dirver运行在客户端

4）Spark On Mesos模式。官方推荐这种模式（当然，原因之一是血缘关系）。正是由于Spark开发之初就考虑到支持Mesos，因此，目前而言，Spark运行在Mesos上会比运行在YARN上更加灵活，更加自然。用户可选择两种调度模式之一运行自己的应用程序：

25.Spark技术栈有哪些组件，每个组件都有什么功能，适合什么应用场景？

1）Spark core：是其它组件的基础，spark的内核，主要包含：有向循环图、RDD、Lingage、Cache、broadcast等，并封装了底层通讯框架，是Spark的基础。

2）SparkStreaming是一个对实时数据流进行高通量、容错处理的流式处理系统，可以对多种数据源（如Kdfka、Flume、Twitter、Zero和TCP 套接字）进行类似Map、Reduce和Join等复杂操作，将流式计算分解成一系列短小的批处理作业。

3）Spark sql：Shark是SparkSQL的前身，Spark SQL的一个重要特点是其能够统一处理关系表和RDD，使得开发人员可以轻松地使用SQL命令进行外部查询，同时进行更复杂的数据分析

4）BlinkDB ：是一个用于在海量数据上运行交互式 SQL 查询的大规模并行查询引擎，它允许用户通过权衡数据精度来提升查询响应时间，其数据的精度被控制在允许的误差范围内。

5）MLBase是Spark生态圈的一部分专注于机器学习，让机器学习的门槛更低，让一些可能并不了解机器学习的用户也能方便地使用MLbase。MLBase分为四部分：MLlib，MLI、ML Optimizer和MLRuntime。

6）GraphX是Spark中用于图和图并行计算

26.spark中worker 的主要工作是什么？

主要功能：管理当前节点内存，CPU的使用情况，接受master发送过来的资源指令，通过executorRunner启动程序分配任务，worker就类似于包工头，管理分配新进程，做计算的服务，相当于process服务，需要注意的是：

1.worker会不会汇报当前信息给master？worker心跳给master主要只有workid，不会以心跳的方式发送资源信息给master，这样master就知道worker是否存活，只有故障的时候才会发送资源信息；

2.worker不会运行代码，具体运行的是executor，可以运行具体application斜的业务逻辑代码，操作代码的节点，不会去运行代码。

27.简单说一下hadoop和spark的shuffle相同和差异？

答：1）从 high-level 的角度来看，两者并没有大的差别。 都是将 mapper（Spark 里是 ShuffleMapTask）的输出进行 partition，不同的 partition 送到不同的 reducer（Spark 里 reducer 可能是下一个 stage 里的 ShuffleMapTask，也可能是 ResultTask）。Reducer 以内存作缓冲区，边 shuffle 边 aggregate 数据，等到数据 aggregate 好以后进行 reduce() （Spark 里可能是后续的一系列操作）。

2）从 low-level 的角度来看，两者差别不小。 Hadoop MapReduce 是 sort-based，进入 combine() 和 reduce() 的 records 必须先 sort。这样的好处在于 combine/reduce() 可以处理大规模的数据，因为其输入数据可以通过外排得到（mapper 对每段数据先做排序，reducer 的 shuffle 对排好序的每段数据做归并）。目前的 Spark 默认选择的是 hash-based，通常使用 HashMap 来对 shuffle 来的数据进行 aggregate，不会对数据进行提前排序。如果用户需要经过排序的数据，那么需要自己调用类似 sortByKey() 的操作；如果你是Spark 1.1的用户，可以将spark.shuffle.manager设置为sort，则会对数据进行排序。在Spark 1.2中，sort将作为默认的Shuffle实现。

3）从实现角度来看，两者也有不少差别。 Hadoop MapReduce 将处理流程划分出明显的几个阶段：map(), spill, merge, shuffle, sort, reduce() 等。每个阶段各司其职，可以按照过程式的编程思想来逐一实现每个阶段的功能。在 Spark 中，没有这样功能明确的阶段，只有不同的 stage 和一系列的 transformation()，所以 spill, merge, aggregate 等操作需要蕴含在 transformation() 中。

如果我们将 map 端划分数据、持久化数据的过程称为 shuffle write，而将 reducer 读入数据、aggregate 数据的过程称为 shuffle read。那么在 Spark 中，问题就变为怎么在 job 的逻辑或者物理执行图中加入 shuffle write 和 shuffle read 的处理逻辑？以及两个处理逻辑应该怎么高效实现？ 

Shuffle write由于不要求数据有序，shuffle write 的任务很简单：将数据 partition 好，并持久化。之所以要持久化，一方面是要减少内存存储空间压力，另一方面也是为了 fault-tolerance。

28.Mapreduce和Spark的都是并行计算，那么他们有什么相同和区别？

答：两者都是用mr模型来进行并行计算:

1)hadoop的一个作业称为job，job里面分为map task和reduce task，每个task都是在自己的进程中运行的，当task结束时，进程也会结束。 

2)spark用户提交的任务成为application，一个application对应一个sparkcontext，app中存在多个job，每触发一次action操作就会产生一个job。这些job可以并行或串行执行，每个job中有多个stage，stage是shuffle过程中DAGSchaduler通过RDD之间的依赖关系划分job而来的，每个stage里面有多个task，组成taskset由TaskSchaduler分发到各个executor中执行，executor的生命周期是和app一样的，即使没有job运行也是存在的，所以task可以快速启动读取内存进行计算。 

3)hadoop的job只有map和reduce操作，表达能力比较欠缺而且在mr过程中会重复的读写hdfs，造成大量的io操作，多个job需要自己管理关系。 

spark的迭代计算都是在内存中进行的，API中提供了大量的RDD操作如join，groupby等，而且通过DAG图可以实现良好的容错。

30、spark有哪些组件？ 

答：主要有如下组件：

1）master：管理集群和节点，不参与计算。 

2）worker：计算节点，进程本身不参与计算，和master汇报。 

3）Driver：运行程序的main方法，创建spark context对象。 

4）spark context：控制整个application的生命周期，包括dagsheduler和task scheduler等组件。 

5）client：用户提交程序的入口。

31、spark工作机制？ 

![img](https://img-blog.csdn.net/20180922220013302?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NodWp1ZWxpbg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

32、spark的优化怎么做？ 

答： spark调优比较复杂，但是大体可以分为三个方面来进行，

1）平台层面的调优：防止不必要的jar包分发，提高数据的本地性，选择高效的存储格式如parquet，

2）应用程序层面的调优：过滤操作符的优化降低过多小任务，降低单条记录的资源开销，处理数据倾斜，复用RDD进行缓存，作业并行化执行等等，

3）JVM层面的调优：设置合适的资源量，设置合理的JVM，启用高效的序列化方法如kyro，增大off head内存等等

1. 序列化在分布式系统中扮演着重要的角色，优化Spark程序时，首当其冲的就是对序列化方式的优化。Spark为使用者提供两种序列化方式：
2.  
3. Java serialization: 默认的序列化方式。
4.  
5. Kryo serialization: 相较于 Java serialization 的方式，速度更快，空间占用更小，但并不支持所有的序列化格式，同时使用的时候需要注册class。spark-sql中默认使用的是kyro的序列化方式。
6. 可以在spark-default.conf设置全局参数，也可以代码中初始化时对SparkConf设置 conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer") ，该参数会同时作用于机器之间数据的shuffle操作以及序列化rdd到磁盘，内存。

Spark不将Kyro设置成默认的序列化方式是因为它需要对类进行注册，官方强烈建议在一些网络数据传输很大的应用中使用kyro序列化。

 

![img](https://img-blog.csdn.net/20180909162702679?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

如果你要序列化的对象比较大，可以增加参数spark.kryoserializer.buffer所设置的值。

如果你没有注册需要序列化的class，Kyro依然可以照常工作，但会存储每个对象的全类名(full class name)，这样的使用方式往往比默认的 Java serialization 还要浪费更多的空间。

可以设置 spark.kryo.registrationRequired 参数为 true，使用kyro时如果在应用中有类没有进行注册则会报错：

如上这个错误需要添加

 

![img](https://img-blog.csdn.net/20180909162720293?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180909162742290?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

33.选择题

二、选择题

\1. Spark 的四大组件下面哪个不是 (D )

A.Spark Streaming  B. Mlib 

C Graphx  D.Spark R

 

2.下面哪个端口不是 spark 自带服务的端口 (C )

A.8080 B.4040 C.8090 D.18080

备注：8080：spark集群web ui端口，4040：sparkjob监控端口，18080：jobhistory端口

 

3.spark 1.4 版本的最大变化 (B )

A spark sql Release 版本 B .引入 Spark R 

C DataFrame D.支持动态资源分配

 

\4. Spark Job 默认的调度模式 (A )

A FIFO  B FAIR  

C 无  D 运行时指定

备注：Spark中的调度模式主要有两种：FIFO和FAIR。默认情况下Spark的调度模式是FIFO（先进先出），谁先提交谁先执行，后面的任务需要等待前面的任务执行。而FAIR（公平调度）模式支持在调度池中为任务进行分组，不同的调度池权重不同，任务可以按照权重来决定执行顺序。使用哪种调度器由参数spark.scheduler.mode来设置，可选的参数有FAIR和FIFO，默认是FIFO。

5.哪个不是本地模式运行的条件 ( D)

A spark.localExecution.enabled=true 

B 显式指定本地运行

C finalStage 无父 Stage

D partition默认值

6.下面哪个不是 RDD 的特点 (C )

A. 可分区  B 可序列化  C 可修改  D 可持久化

 

\7. 关于广播变量，下面哪个是错误的 (D )

A 任何函数调用  B 是只读的 

C 存储在各个节点  D 存储在磁盘或 HDFS

 

\8. 关于累加器，下面哪个是错误的 (D )

A 支持加法 B 支持数值类型 

C 可并行 D 不支持自定义类型

 

9.Spark 支持的分布式部署方式中哪个是错误的 (D )

A standalone B spark on mesos 

C spark on YARN D Spark on local

 

10.Stage 的 Task 的数量由什么决定 (A )

A Partition B Job C Stage D TaskScheduler

 

11.下面哪个操作是窄依赖 (B )

A join B filter 

C group D sort

 

12.下面哪个操作肯定是宽依赖 (C )

A map B flatMap 

C reduceByKey D sample

 

13.spark 的 master 和 worker 通过什么方式进行通信的？ (D )

A http B nio C netty D Akka

备注：从spark1.3.1之后，netty完全代替 了akka

一直以来，基于Akka实现的RPC通信框架是Spark引以为豪的主要特性，也是与Hadoop等分布式计算框架对比过程中一大亮点，但是时代和技术都在演化，从Spark1.3.1版本开始，为了解决大数据块（如shuffle）的传输问题，Spark引入了Netty通信框架，到了1.6.0版本，Netty居然完全取代了Akka，承担Spark内部所有的RPC通信以及数据流传输。

那么Akka又是什么东西？从Akka出现背景来说，它是基于Actor的RPC通信系统，它的核心概念也是Message，它是基于协程的，性能不容置疑；基于scala的偏函数，易用性也没有话说，但是它毕竟只是RPC通信，无法适用大的package/stream的数据传输，这也是Spark早期引入Netty的原因。

那么Netty为什么可以取代Akka？首先不容置疑的是Akka可以做到的，Netty也可以做到，但是Netty可以做到，Akka却无法做到，原因是啥？在软件栈中，Akka相比Netty要Higher一点，它专门针对RPC做了很多事情，而Netty相比更加基础一点，可以为不同的应用层通信协议（RPC，FTP，HTTP等）提供支持，在早期的Akka版本，底层的NIO通信就是用的Netty；其次一个优雅的工程师是不会允许一个系统中容纳两套通信框架，恶心！最后，虽然Netty没有Akka协程级的性能优势，但是Netty内部高效的Reactor线程模型，无锁化的串行设计，高效的序列化，零拷贝，内存池等特性也保证了Netty不会存在性能问题。

那么Spark是怎么用Netty来取代Akka呢？一句话，利用偏函数的特性，基于Netty“仿造”出一个简约版本的Actor模型！！

 

14 默认的存储级别 (A )

A MEMORY_ONLY B MEMORY_ONLY_SER

C MEMORY_AND_DISK D MEMORY_AND_DISK_SER

 

15 spark.deploy.recoveryMode 不支持那种 (D )

A.ZooKeeper B. FileSystem 

D NONE D Hadoop

 

16.下列哪个不是 RDD 的缓存方法 (C )

A persist() B Cache() 

C Memory()

 

17.Task 运行在下来哪里个选项中 Executor 上的工作单元 (C )

A Driver program B. spark master 

C.worker node D Cluster manager

 

18.hive 的元数据存储在 derby 和 MySQL 中有什么区别 (B )

A.没区别 B.多会话

C.支持网络环境 D数据库的区别

备注： Hive 将元数据存储在 RDBMS 中，一般常用 MySQL 和 Derby。默认情况下，Hive 元数据保存在内嵌的 Derby 数据库中，只能允许一个会话连接，只适合简单的测试。实际生产环境中不适用， 为了支持多用户会话，则需要一个独立的元数据库，使用 MySQL 作为元数据库，Hive 内部对 MySQL 提供了很好的支持。

内置的derby主要问题是并发性能很差，可以理解为单线程操作。

Derby还有一个特性。更换目录执行操作，会找不到相关表等

 

19.DataFrame 和 RDD 最大的区别 (B )

A.科学统计支持 B.多了 schema 

C.存储方式不一样 D.外部数据源支持

备注：

19.简述SparkSQL中RDD、DataFrame、DataSet三者的区别与联系? （笔试重点）

**1****）RDD**

**分布式弹性数据集**

优点:

编译时类型安全 

编译时就能检查出类型错误

面向对象的编程风格 

直接通过类名点的方式来操作数据

缺点:

序列化和反序列化的性能开销 

无论是集群间的通信, 还是IO操作都需要对对象的结构和数据进行序列化和反序列化。

GC的性能开销，频繁的创建和销毁对象, 势必会增加GC

**2****）DataFrame**

DataFrame引入了schema和off-heap

schema : RDD每一行的数据, 结构都是一样的，这个结构就存储在schema中。 Spark通过schema就能够读懂数据, 因此在通信和IO时就只需要序列化和反序列化数据, 而结构的部分就可以省略了。

**3****）DataSet**

DataSet结合了RDD和DataFrame的优点，并带来的一个新的概念Encoder。

当序列化数据时，Encoder产生字节码与off-heap进行交互，能够达到按需访问数据的效果，而不用反序列化整个对象。Spark还没有提供自定义Encoder的API，但是未来会加入。

三者之间的转换:

![img](https://img-blog.csdnimg.cn/20190819173727534.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NodWp1ZWxpbg==,size_16,color_FFFFFF,t_70)

 

34.cache后面能不能接其他算子,它是不是action操作？

答：cache可以接其他算子，但是接了算子之后，起不到缓存应有的效果，因为会重新触发cache。

cache不是action操作

35.reduceByKey是不是action？

答：不是，很多人都会以为是action，reduce rdd是action

38.常规的容错方式有哪几种类型？

1）.数据检查点,会发生拷贝，浪费资源

2）.记录数据的更新，每次更新都会记录下来，比较复杂且比较消耗性能

39.RDD通过Linage（记录数据更新）的方式为何很高效？

1）lazy记录了数据的来源，RDD是不可变的，且是lazy级别的，且rDD之间构成了链条，lazy是弹性的基石。由于RDD不可变，所以每次操作就产生新的rdd，不存在全局修改的问题，控制难度下降，所有有计算链条将复杂计算链条存储下来，计算的时候从后往前回溯900步是上一个stage的结束，要么就checkpoint

2）记录原数据，是每次修改都记录，代价很大如果修改一个集合，代价就很小，官方说rdd是粗粒度的操作，是为了效率，为了简化，每次都是操作数据集合，写或者修改操作，都是基于集合的rdd的写操作是粗粒度的，rdd的读操作既可以是粗粒度的

也可以是细粒度，读可以读其中的一条条的记录。

3）简化复杂度，是高效率的一方面，写的粗粒度限制了使用场景如网络爬虫，现实世界中，大多数写是粗粒度的场景

40.RDD有哪些缺陷？

1）不支持细粒度的写和更新操作（如网络爬虫），spark写数据是粗粒度的所谓粗粒度，就是批量写入数据，为了提高效率。但是读数据是细粒度的也就是说可以一条条的读

2）不支持增量迭代计算，Flink支持

41.说一说Spark程序编写的一般步骤？

\42. Spark有哪两种算子？

答：Transformation（转化）算子和Action（执行）算子。

\43. Spark提交你的jar包时所用的命令是什么？

答：spark-submit。

\44. Spark有哪些聚合类的算子,我们应该尽量避免什么类型的算子？

答：在我们的开发过程中，能避免则尽可能避免使用reduceByKey、join、distinct、repartition等会进行shuffle的算子，尽量使用map类的非shuffle算子。这样的话，没有shuffle操作或者仅有较少shuffle操作的Spark作业，可以大大减少性能开销。

\45. 你所理解的Spark的shuffle过程？

答：从下面三点去展开

1）shuffle过程的划分

2）shuffle的中间结果如何存储

3）shuffle的数据如何拉取过来

可以参考这篇博文：http://www.cnblogs.com/jxhd1/p/6528540.html

 

![img](https://img-blog.csdn.net/20180909162912406?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0x3ajg3OTUyNTkzMA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

\46. 你如何从Kafka中获取数据？

1)基于Receiver的方式

这种方式使用Receiver来获取数据。Receiver是使用Kafka的高层次Consumer API来实现的。receiver从Kafka中获取的数据都是存储在Spark Executor的内存中的，然后Spark Streaming启动的job会去处理那些数据。

2)基于Direct的方式

这种新的不基于Receiver的直接方式，是在Spark 1.3中引入的，从而能够确保更加健壮的机制。替代掉使用Receiver来接收数据后，这种方式会周期性地查询Kafka，来获得每个topic+partition的最新的offset，从而定义每个batch的offset的范围。当处理数据的job启动时，就会使用Kafka的简单consumer api来获取Kafka指定offset范围的数据。

\47. 对于Spark中的数据倾斜问题你有什么好的方案？

48.RDD创建有哪几种方式？

1).使用程序中的集合创建rdd

2).使用本地文件系统创建rdd

3).使用hdfs创建rdd，

4).基于数据库db创建rdd

5).基于Nosql创建rdd，如hbase

6).基于数据流，如socket创建rdd

如果只回答了前面三种，是不够的，只能说明你的水平还是入门级的，实践过程中有很多种创建方式。

49.Spark并行度怎么设置比较合适？

答：spark并行度，每个core承载2~4个partition,如，32个core，那么64~128之间的并行度，也就是

设置64~128个partion，并行读和数据规模无关，只和内存使用量和cpu使用时间有关

50.Spark中数据的位置是被谁管理的？

答：每个数据分片都对应具体物理位置，数据的位置是被blockManager，无论数据是在磁盘，内存还是tacyan，都是由blockManager管理

52.rdd有几种操作类型？

1）transformation，rdd由一种转为另一种rdd

2）action，

3）cronroller，crontroller是控制算子,cache,persist，对性能和效率的有很好的支持

三种类型，不要回答只有2中操作

53.Spark如何处理不能被序列化的对象？

将不能序列化的内容封装成object

54.collect功能是什么，其底层是怎么实现的？

答：driver通过collect把集群中各个节点的内容收集过来汇总成结果，collect返回结果是Array类型的，collect把各个节点上的数据抓过来，抓过来数据是Array型，collect对Array抓过来的结果进行合并，合并后Array中只有一个元素，是tuple类型（KV类型的）的。

55.Spaek程序执行，有时候默认为什么会产生很多task，怎么修改默认task执行个数？

答：1）因为输入数据有很多task，尤其是有很多小文件的时候，有多少个输入block就会有多少个task启动；

2）spark中有partition的概念，每个partition都会对应一个task，task越多，在处理大规模数据的时候，就会越有效率。不过task并不是越多越好，如果平时测试，或者数据量没有那么大，则没有必要task数量太多。

3）参数可以通过spark_home/conf/spark-default.conf配置文件设置:

spark.sql.shuffle.partitions 50 spark.default.parallelism 10

第一个是针对spark sql的task数量

第二个是非spark sql程序设置生效

56.为什么Spark Application在没有获得足够的资源，job就开始执行了，可能会导致什么什么问题发生?

答：会导致执行该job时候集群资源不足，导致执行job结束也没有分配足够的资源，分配了部分Executor，该job就开始执行task，应该是task的调度线程和Executor资源申请是异步的；如果想等待申请完所有的资源再执行job的：需要将spark.scheduler.maxRegisteredResourcesWaitingTime设置的很大；spark.scheduler.minRegisteredResourcesRatio 设置为1，但是应该结合实际考虑，否则很容易出现长时间分配不到资源，job一直不能运行的情况。

57.map与flatMap的区别？

map：对RDD每个元素转换，文件中的每一行数据返回一个数组对象

flatMap：对RDD每个元素转换，然后再扁平化将所有的对象合并为一个对象，文件中的所有行数据仅返回一个数组对象，会抛弃值为null的值

58.列举你常用的action？

collect，reduce,take,count,saveAsTextFile等

59.Spark为什么要持久化，一般什么场景下要进行persist操作？

为什么要进行持久化？

spark所有复杂一点的算法都会有persist身影,spark默认数据放在内存，spark很多内容都是放在内存的，非常适合高速迭代，1000个步骤只有第一个输入数据，中间不产生临时数据，但分布式系统风险很高，所以容易出错，就要容错，rdd出错或者分片可以根据血统算出来，如果没有对父rdd进行persist 或者cache的化，就需要重头做。

以下场景会使用persist

1）某个步骤计算非常耗时，需要进行persist持久化

2）计算链条非常长，重新恢复要算很多步骤，很好使，persist

3）checkpoint所在的rdd要持久化persist，

lazy级别，框架发现有checnkpoint，checkpoint时单独触发一个job，需要重算一遍，checkpoint前

要持久化，写个rdd.cache或者rdd.persist，将结果保存起来，再写checkpoint操作，这样执行起来会非常快，不需要重新计算rdd链条了。checkpoint之前一定会进行persist。

4）shuffle之后为什么要persist，shuffle要进性网络传输，风险很大，数据丢失重来，恢复代价很大

5）shuffle之前进行persist，框架默认将数据持久化到磁盘，这个是框架自动做的。

60.为什么要进行序列化？

序列化可以减少数据的体积，减少存储空间，高效存储和传输数据，不好的是使用的时候要反序列化，非常消耗CPU

61.介绍一下join操作优化经验？

答：join其实常见的就分为两类： map-side join 和 reduce-side join。当大表和小表join时，用map-side join能显著提高效率。将多份数据进行关联是数据处理过程中非常普遍的用法，不过在分布式计算系统中，这个问题往往会变的非常麻烦，因为框架提供的 join 操作一般会将所有数据根据 key 发送到所有的 reduce 分区中去，也就是 shuffle 的过程。造成大量的网络以及磁盘IO消耗，运行效率极其低下，这个过程一般被称为 reduce-side-join。如果其中有张表较小的话，我们则可以自己实现在 map 端实现数据关联，跳过大量数据进行 shuffle 的过程，运行时间得到大量缩短，根据不同数据可能会有几倍到数十倍的性能提升。

备注：这个题目面试中非常非常大概率见到，务必搜索相关资料掌握，这里抛砖引玉。

63.如何配置 spark master 的 HA？

1）配置 zookeeper
2）修改 spark_env.sh 文件,spark 的 master 参数不在指定，添加如下代码到各个 master 节点
export SPARK_DAEMON_JAVA_OPTS="-Dspark.deploy.recoveryMode=ZOOKEEPER
-Dspark.deploy.zookeeper.url=zk01:2181,zk02:2181,zk03:2181
-Dspark.deploy.zookeeper.dir=/spark"

3）将 spark_env.sh 分发到各个节点
4）找到一个 master 节点，执行./start-all.sh，会在这里启动主 master,其他的 master 备节点，
启动 master 命令: ./sbin/start-master.sh
5）提交程序的时候指定 master 的时候要指定三台 master，例如
./spark-shell --master spark://master01:7077,master02:7077,master03:7077

66.Spark 为什么比 mapreduce 快？

基于内存，迭代，分布式计算。

1）基于内存计算，减少低效的磁盘交互；
2）高效的调度算法，基于 DAG；
3）容错机制 Linage，精华部分就是 DAG 和 Lingae

\67. 简要描述 Spark 分布式集群搭建的步骤？

1）准备 linux 环境，设置集群搭建账号和用户组，设置 ssh，关闭防火墙，关闭 seLinux，
配置 host，hostname
2）配置 jdk 到环境变量
3）搭建 hadoop 集群，如果要做 master ha，需要搭建 zookeeper 集群
修改 hdfs-site.xml,hadoop_env.sh,yarn-site.xml,slaves 等配置文件
4）启动 hadoop 集群，启动前要格式化 namenode
5）配置 spark 集群，修改 spark-env.xml，slaves 等配置文件，拷贝 hadoop 相关配置到 spark
conf 目录下
6）启动 spark 集群。

68.cache 和 pesist 的区别 ?

1）cache 和 persist 都是用于将一个 RDD 进行缓存的，这样在之后使用的过程中就不需要重
新计算了，可以大大节省程序运行时间；
2）cache 只有一个默认的缓存级别 MEMORY_ONLY ，cache 调用了 persist，而 persist 可以
根据情况设置其它的缓存级别；
3）executor 执行的时候，默认 60%做 cache，40%做 task 操作，persist 最根本的函数，最底
层的函数

69.RDD 的弹性表现在哪几点？

1）自动的进行内存和磁盘的存储切换；
2）基于 Lineage 的高效容错；
3）task 如果失败会自动进行特定次数的重试；
4）stage 如果失败会自动进行特定次数的重试，而且只会计算失败的分片；
5）checkpoint 和 persist，数据计算之后持久化缓存
6）数据调度弹性，DAG TASK 调度和资源无关 

70.你所理解的 Spark 的 shuffle 过程？

答：从下面三点去展开
1）shuffle 过程的划分
2）shuffle 的中间结果如何存储
3）shuffle 的数据如何拉取过来
可以参考这篇博文：http://www.cnblogs.com/jxhd1/p/6528540.html 

71.collect 功能是什么，其底层是怎么实现的？

答：driver 通过 collect 把集群中各个节点的内容收集过来汇总成结果，collect 返回结果是Array 类型的，collect 把各个节点上的数据抓过来，抓过来数据是 Array 型，collect 对 Array抓过来的结果进行合并，合并后 Array 中只有一个元素，是 tuple 类型（KV 类型的）的。

73.spark on yarn Cluster 模式下，ApplicationMaster 和 driver是在同一个进程么？

是,driver 位于 ApplicationMaster 进程中。该进程负责申请资源，还负责监控程序、资源的动态情况。

\74. 介绍 parition 和 block 有什么关联关系？

1）hdfs 中的 block 是分布式存储的最小单元，等分，可设置冗余，这样设计有一部分磁盘空间的浪费，但是整齐的 block 大小，便于快速找到、读取对应的内容；
2）Spark 中的 partion 是弹性分布式数据集 RDD 的最小单元，RDD 是由分布在各个节点上的 partion 组成的。partion 是指的 spark 在计算过程中，生成的数据在计算空间内最小单元，同一份数据（RDD）的 partion 大小不一，数量不定，是根据 application 里的算子和最初读入的数据分块数量决定；
3）block 位于存储空间、partion 位于计算空间，block 的大小是固定的、partion 大小是不固定的，是从 2 个不同的角度去看数据。

75.Spark 应用程序的执行过程是什么？（☆☆☆☆☆） 

1）构建 Spark Application 的运行环境（启动 SparkContext），SparkContext 向资源管理器（可以是 Standalone、Mesos 或 YARN）注册并申请运行 Executor 资源；
2）资源管理器分配 Executor 资源并启动 StandaloneExecutorBackend，Executor 运行情况将随着心跳发送到资源管理器上；
3）SparkContext 构建成 DAG 图，将 DAG 图分解成 Stage，并把 Taskset 发送给 TaskScheduler。Executor 向 SparkContext 申请 Task，Task Scheduler 将 Task 发放给 Executor 运行同时 SparkContext 将应用程序代码发放给 Executor；
4）Task 在 Executor 上运行，运行完毕释放所有资源。 

76.如何理解 Standalone 模式下，Spark 资源分配是粗粒度的？ 

spark 默认情况下资源分配是粗粒度的，也就是说程序在提交时就分配好资源，后面执行的时候使用分配好的资源，除非资源出现了故障才会重新分配。比如 Spark shell 启动，已提交，一注册，哪怕没有任务，worker 都会分配资源给 executor。 

77.spark 中 task 有几种类型？

Spark 中的 Task 有 2 种类型：
1）result task 类型，最后一个 task；
2）shuffleMapTask 类型，除了最后一个 task 都是此类型；

78.如何使用 Spark 解决分组排序问题？

 ![img](https://img-blog.csdnimg.cn/20190403114237687.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NodWp1ZWxpbg==,size_16,color_FFFFFF,t_70)

需求：
1）对上述数据按 key 值进行分组
2）对分组后的值进行排序
3）截取分组后值得 top 3 位以 key-value 形式返回结果

代码：

```java
     val groupTopNRdd = sc.textFile(
     "hdfs://db02:8020/user/hadoop/groupsorttop/groupsorttop.data")
    
   
   
    
   
   
    
     groupTopNRdd.map(_.split(
     " ")).map(x => (x(
     0),x(
     1))).groupByKey().map(
    
   
   
    
   
   
    
     x => {
    
   
   
    
   
   
    
     val xx = x._1
    
   
   
    
   
   
    
     val yy = x._2
    
   
   
    
   
   
    
     (xx,yy.toList.sorted.reverse.take(
     3))
    
   
   
    
   
   
    
     }).collect
    
   
 1
```

 79.scala 中 private 与 private[this] 修饰符的区别？

1）private ，类私有的字段，Scala 会自动生成私有的 getter/setter 方法，通过对象实例可以调用；
2）private[this]，对象私有的字段，Scala 不生成 getter/setter 方法，所以只能在对象内部访问被修饰的字段，如下代码是不能编译通过的，因为没有生成 getter/setter 方法，所以不能通过这个方法调用。private[this]比 private 要更加严格，他将声明的变量只能在自己的同一个实例中可以被访问。 

80.Spark 中 Lineage 的基本原理？

这里应该是问你 Spark 的容错机制的原理：
1）Lineag（e又称为RDD 运算图或RDD 依赖关系图）是RDD 所有父RDD 的graph（图）。它是在 RDD 上执行 transformations 函数并创建 logical execution plan（逻辑执行计划）的结果，是 RDD 的逻辑执行计划，记录了 RDD 之间的依赖关系。
2）使用Lineage 实现 spark 的容错，本质上类似于数据库中重做日志，是容错机制的一种方式，不过这个重做日志粒度非常大，是对全局数据做同样的重做进行数据恢复。

81.使用 shell 和 scala 代码实现 WordCount？

这个题目即考察了你对 shell 的掌握，又考察了你对 scala 的了解，还考察了你动手写代
码的能力，是比较好的一道题（实际开发中，有些代码是必须要背下来的，烂熟于心，劣等
的程序员就是百度+copy，是不可取的）

```java
     val conf = 
     new SparkConf()
    
   
   
    
   
   
    
     val sc = 
     new SparkContext(conf)
    
   
   
    
   
   
    
     val line = sc.textFile(
     "xxxx.txt") .flatMap(_.split(
     " ")).map((_,
     1)).reduceByKey(_+_).
    
   
   
    
   
   
    
     collect().foreach(println) 
    
   
   
    
   
   
    
     sc.stop()
    
   
 1
```

82.Spark sql 又为什么比 hive 快呢？

 计算引擎不一样，一个是 spark 计算模型，一个是 mapreudce 计算模型。

83.hbase region 多大会分区，spark 读取 hbase 数据是如何划分 partition 的？

region 超过了 hbase.hregion.max.filesize 这个参数配置的大小就会自动裂分，默认值是
1G。