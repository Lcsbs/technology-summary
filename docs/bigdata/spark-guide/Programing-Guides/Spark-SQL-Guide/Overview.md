#  Spark SQL，DataFrames 和 Datasets 指南

Spark SQL是用于结构化数据处理的Spark模块。与基本的Spark RDD API不同，Spark SQL提供的接口为Spark提供了有关数据结构和正在执行的计算的更多信息。在内部，Spark SQL使用这些额外的信息来执行额外的优化。与Spark SQL交互的方法有多种，包括SQL和Dataset API。计算结果时，将使用相同的执行引擎，而与要用来表达计算的API /语言无关。这种统一意味着开发人员可以轻松地在不同的API之间来回切换，从而提供最自然的方式来表达给定的转换。

此页面上的所有示例均使用Spark发行版中包含的示例数据，并且可以在`spark-shell`，`pyspark`shell或`sparkR`shell中运行。

## SQL

Spark SQL的一种用途是执行SQL查询。Spark SQL还可以用于从现有的Hive安装中读取数据。有关如何配置此功能的更多信息，请参考[Hive Tables](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html)部分。当从另一种编程语言中运行SQL时，结果将作为[Dataset / DataFrame](http://spark.apache.org/docs/latest/sql-programming-guide.html#datasets-and-dataframes)返回。您还可以使用[命令行](http://spark.apache.org/docs/latest/sql-distributed-sql-engine.html#running-the-spark-sql-cli) 或通过[JDBC / ODBC](http://spark.apache.org/docs/latest/sql-distributed-sql-engine.html#running-the-thrift-jdbcodbc-server)与SQL接口进行交互。

## Datasets 和 DataFrames

Dataset是数据的分布式集合。Dataset是Spark 1.6中添加的新接口，它具有RDD的优点（强类型输入，使用强大的Lambda函数的能力）和Spark SQL的优化执行引擎的优点。Dataset可以被[构造](http://spark.apache.org/docs/latest/sql-getting-started.html#creating-datasets)从JVM对象，然后使用功能性的转换（操作`map`，`flatMap`，`filter`等等）。Dataset API在[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/Dataset.html)和 [Java中](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/sql/Dataset.html)可用。Python不支持Dataset API。但是由于Python的动态特性，Dataset API的许多优点已经可用（即，您可以自然地通过名称访问行的字段 `row.columnName`）。R的情况类似。

DataFrame是组织为命名列的*Dataset*。从概念上讲，它等效于关系数据库中的表或R / Python中的数据框，但是在后台进行了更丰富的优化。可以从多种[资源](http://spark.apache.org/docs/latest/sql-data-sources.html)构造DataFrame，例如：结构化数据文件，Hive中的表，外部数据库或现有的RDD。DataFrame API在Scala，Java，[Python](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.DataFrame)和[R中](http://spark.apache.org/docs/latest/api/R/index.html)可用。在Scala和Java中，DataFrame由的Dataset表示`Row`。在[Scala API中](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/Dataset.html)，`DataFrame`只是类型别名`Dataset[Row]`。而在[Java API中](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/sql/Dataset.html)，用户需要使用`Dataset<Row>`来代表`DataFrame`。

在整个文档中，我们通常将的Scala / Java Dataset`Row`称为DataFrames。