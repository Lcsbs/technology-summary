# 数据源

Spark SQL支持通过DataFrame接口对各种数据源进行操作。DataFrame可以使用关系转换进行操作，也可以用于创建临时视图。将DataFrame注册为临时视图使您可以对其数据运行SQL查询。本节介绍了使用Spark数据源加载和保存数据的一般方法，然后介绍了可用于内置数据源的特定选项。

- 通用加载/保存功能
  - [手动指定选项](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#manually-specifying-options)
  - [直接在文件上运行SQL](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#run-sql-on-files-directly)
  - [保存模式](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#save-modes)
  - [保存到永久表](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#saving-to-persistent-tables)
  - [分组，分类和分区](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#bucketing-sorting-and-partitioning)
- 通用文件源选项
  - [忽略损坏的文件](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#ignore-corrupt-iles)
  - [忽略丢失的文件](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#ignore-missing-iles)
  - [路径全局过滤器](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#path-global-filter)
  - [递归文件查找](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#recursive-file-lookup)
- Parquet文件
  - [以编程方式加载数据](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#loading-data-programmatically)
  - [分区发现](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#partition-discovery)
  - [模式合并](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#schema-merging)
  - [Hive Metastore Parquet表转换](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#hive-metastore-parquet-table-conversion)
  - [配置](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#configuration)
- [ORC文件](http://spark.apache.org/docs/latest/sql-data-sources-orc.html)
- [JSON文件](http://spark.apache.org/docs/latest/sql-data-sources-json.html)
- Hive表
  - [指定Hive表的存储格式](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#specifying-storage-format-for-hive-tables)
  - [与Hive Metastore的不同版本进行交互](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#interacting-with-different-versions-of-hive-metastore)
- [JDBC到其他数据库](http://spark.apache.org/docs/latest/sql-data-sources-jdbc.html)
- Avro文件
  - [部署中](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#deploying)
  - [加载和保存功能](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#load-and-save-functions)
  - [to_avro（）和from_avro（）](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#to_avro-and-from_avro)
  - [数据源选项](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#data-source-option)
  - [配置](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#configuration)
  - [与Databricks spark-avro的兼容性](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#compatibility-with-databricks-spark-avro)
  - [Avro-> Spark SQL转换支持的类型](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#supported-types-for-avro---spark-sql-conversion)
  - [Spark SQL支持的类型-> Avro转换](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#supported-types-for-spark-sql---avro-conversion)
- [整个二进制文件](http://spark.apache.org/docs/latest/sql-data-sources-binaryFile.html)
- [故障排除](http://spark.apache.org/docs/latest/sql-data-sources-troubleshooting.html)

# 通用加载/保存功能

- [手动指定选项](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#manually-specifying-options)
- [直接在文件上运行SQL](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#run-sql-on-files-directly)
- [保存模式](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#save-modes)
- [保存到永久表](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#saving-to-persistent-tables)
- [分组，分类和分区](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#bucketing-sorting-and-partitioning)

以最简单的形式，所有操作都将使用默认数据源（`parquet`除非另有配置 `spark.sql.sources.default`）。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_r_0)

```scala
val usersDF = spark.read.load("examples/src/main/resources/users.parquet")
usersDF.select("name", "favorite_color").write.save("namesAndFavColors.parquet")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

### 手动指定选项

您还可以手动指定将要使用的数据源以及要传递给数据源的任何其他选项。数据源通过其全名指定（即`org.apache.spark.sql.parquet`），但内置的来源，你也可以使用自己的短名称（`json`，`parquet`，`jdbc`，`orc`，`libsvm`，`csv`，`text`）。从任何数据源类型加载的DataFrame都可以使用此语法转换为其他类型。

请参阅API文档以获取内置源的可用选项，例如 `org.apache.spark.sql.DataFrameReader`和`org.apache.spark.sql.DataFrameWriter`。此处记录的选项也应通过非Scala Spark API（例如PySpark）应用。对于其他格式，请参阅特定格式的API文档。

要加载JSON文件，您可以使用：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_1)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_1)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_1)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_r_1)

```scala
val peopleDF = spark.read.format("json").load("examples/src/main/resources/people.json")
peopleDF.select("name", "age").write.format("parquet").save("namesAndAges.parquet")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

要加载CSV文件，您可以使用：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_2)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_2)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_2)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_r_2)

```scala
val peopleDFCsv = spark.read.format("csv")
  .option("sep", ";")
  .option("inferSchema", "true")
  .option("header", "true")
  .load("examples/src/main/resources/people.csv")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

额外选项在写操作期间也使用。例如，您可以控制ORC数据源的Bloom过滤器和字典编码。以下ORC示例将创建Bloom过滤器，并仅将字典编码用于`favorite_color`。对于镶木地板，也存在`parquet.enable.dictionary`。要查找有关其他ORC / Parquet选项的更多详细信息，请访问Apache ORC / Parquet官方网站。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_3)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_3)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_3)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_r_3)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_SQL_3)

```scala
usersDF.write.format("orc")
  .option("orc.bloom.filter.columns", "favorite_color")
  .option("orc.dictionary.key.threshold", "1.0")
  .option("orc.column.encoding.direct", "name")
  .save("users_with_options.orc")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

### 直接在文件上运行SQL

除了使用读取API将文件加载到DataFrame中并进行查询之外，您还可以直接使用SQL查询该文件。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_4)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_4)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_4)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_r_4)

```scala
val sqlDF = spark.sql("SELECT * FROM parquet.`examples/src/main/resources/users.parquet`")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

### 保存模式

保存操作可以选择带`SaveMode`，指定如何处理现有数据（如果存在）。重要的是要意识到这些保存模式不使用任何锁定并且不是原子的。此外，执行时`Overwrite`，将在写出新数据之前删除数据。

| Scala / Java                      | 任何语言                              | 含义                                                         |
| :-------------------------------- | :------------------------------------ | :----------------------------------------------------------- |
| `SaveMode.ErrorIfExists` （默认） | `"error" or "errorifexists"` （默认） | 将DataFrame保存到数据源时，如果已经存在数据，则将引发异常。  |
| `SaveMode.Append`                 | `"append"`                            | 将DataFrame保存到数据源时，如果已经存在数据/表，则应该将DataFrame的内容附加到现有数据中。 |
| `SaveMode.Overwrite`              | `"overwrite"`                         | 覆盖模式意味着将DataFrame保存到数据源时，如果已经存在数据/表，则预期现有数据将被DataFrame的内容覆盖。 |
| `SaveMode.Ignore`                 | `"ignore"`                            | 忽略模式意味着在将DataFrame保存到数据源时，如果已经存在数据，则预期保存操作将不保存DataFrame的内容并且不更改现有数据。这类似于`CREATE TABLE IF NOT EXISTS`SQL中的。 |

### 保存到永久表

`DataFrames`也可以使用以下`saveAsTable` 命令作为持久表保存到Hive Metastore中。请注意，使用此功能不需要现有的Hive部署。Spark将为您创建一个默认的本地Hive Metastore（使用Derby）。与`createOrReplaceTempView`命令不同， `saveAsTable`它将具体化DataFrame的内容并在Hive元存储中创建一个指向数据的指针。即使您重新启动Spark程序，持久表仍将存在，只要您保持与同一metastore的连接即可。可以通过使用表名`table`在上调用方法来创建持久表的DataFrame `SparkSession`。

对于基于文件的数据源，例如文本，镶木地板，json等，您可以通过`path`选项指定自定义表路径 ，例如`df.write.option("path", "/some/path").saveAsTable("t")`。删除表后，自定义表路径将不会删除，并且表数据仍然存在。如果未指定自定义表路径，Spark会将数据写入仓库目录下的默认表路径。删除表时，默认表路径也将被删除。

从Spark 2.1开始，持久性数据源表在Hive元存储中存储了按分区的元数据。这带来了几个好处：

- 由于元存储只能返回查询的必要分区，因此不再需要在第一个查询中将所有分区发现到表中。
- Hive DDL，例如`ALTER TABLE PARTITION ... SET LOCATION`现在可用于使用Datasource API创建的表。

请注意，在创建外部数据源表（带有`path`选项的表）时，默认情况下不会收集分区信息。要同步元存储中的分区信息，可以调用`MSCK REPAIR TABLE`。

### 分组，分类和分区

对于基于文件的数据源，也可以对输出进行存储和分类或分区。存储桶和排序仅适用于持久表：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_5)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_5)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_5)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_SQL_5)

```scala
peopleDF.write.bucketBy(42, "name").sortBy("age").saveAsTable("people_bucketed")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

分区可同时使用`save`和`saveAsTable`使用数据集API时使用。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_6)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_6)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_6)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_SQL_6)

```scala
usersDF.write.partitionBy("favorite_color").format("parquet").save("namesPartByColor.parquet")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

可以对单个表使用分区和存储桶：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_scala_7)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_java_7)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_python_7)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#tab_SQL_7)

```scala
usersDF
  .write
  .partitionBy("favorite_color")
  .bucketBy(42, "name")
  .saveAsTable("users_partitioned_bucketed")
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

`partitionBy`按照“[分区发现”](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#partition-discovery)部分中的描述创建目录结构。因此，它对具有高基数的列的适用性有限。相反， `bucketBy`将数据分布在固定数量的存储桶中，并且当唯一值的数量不受限制时可以使用。

# 通用文件源选项

- [忽略损坏的文件](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#ignore-corrupt-files)
- [忽略丢失的文件](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#ignore-missing-files)
- [路径全局过滤器](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#path-global-filter)
- [递归文件查找](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#recursive-file-lookup)

这些通用选项/配置仅在使用基于文件的源（parquet，orc，avro，json，csv，text）时才有效。

请注意，以下示例中使用的目录层次结构为：

```shell
dir1/
 ├── dir2/
 │    └── file2.parquet (schema: <file: string>, content: "file2.parquet")
 └── file1.parquet (schema: <file, string>, content: "file1.parquet")
 └── file3.json (schema: <file, string>, content: "{'file':'corrupt.json'}")
```

### 忽略损坏的文件

使用Spark，您可以`spark.sql.files.ignoreCorruptFiles`从文件中读取数据时忽略损坏的文件。设置为true时，当遇到损坏的文件时，Spark作业将继续运行，并且已读取的内容仍将返回。

要在读取数据文件时忽略损坏的文件，可以使用：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_r_0)

```scala
// enable ignore corrupt files
spark.sql("set spark.sql.files.ignoreCorruptFiles=true")
// dir1/file3.json is corrupt from parquet's view
val testCorruptDF = spark.read.parquet(
  "examples/src/main/resources/dir1/",
  "examples/src/main/resources/dir1/dir2/")
testCorruptDF.show()
// +-------------+
// |         file|
// +-------------+
// |file1.parquet|
// |file2.parquet|
// +-------------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

### 忽略丢失的文件

使用Spark，您可以`spark.sql.files.ignoreMissingFiles`从文件中读取数据时忽略丢失的文件。在这里，丢失的文件实际上意味着在构建`DataFrame`。之后目录下的已删除文件 。设置为true时，遇到丢失的文件时，Spark作业将继续运行，并且已读取的内容仍将返回。

### 路径全局过滤器

`pathGlobFilter`用于仅包含文件名与模式匹配的文件。语法如下`org.apache.hadoop.fs.GlobFilter`。它不会更改分区发现的行为。

要加载具有与给定全局模式匹配的路径的文件，同时保持分区发现的行为，可以使用：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_scala_1)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_java_1)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_python_1)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_r_1)

```scala
val testGlobFilterDF = spark.read.format("parquet")
  .option("pathGlobFilter", "*.parquet") // json file should be filtered out
  .load("examples/src/main/resources/dir1")
testGlobFilterDF.show()
// +-------------+
// |         file|
// +-------------+
// |file1.parquet|
// +-------------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

### 递归文件查找

`recursiveFileLookup`用于递归加载文件，并且禁用分区推断。默认值为`false`。如果数据源显式指定`partitionSpec`when`recursiveFileLookup`值为true，则将引发异常。

要递归加载所有文件，可以使用：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_scala_2)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_java_2)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_python_2)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#tab_r_2)

```scala
val recursiveLoadedDF = spark.read.format("parquet")
  .option("recursiveFileLookup", "true")
  .load("examples/src/main/resources/dir1")
recursiveLoadedDF.show()
// +-------------+
// |         file|
// +-------------+
// |file1.parquet|
// |file2.parquet|
// +-------------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

# Parquet 文件

- [以编程方式加载数据](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#loading-data-programmatically)
- [分区发现](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#partition-discovery)
- [模式合并](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#schema-merging)
- Hive Metastore Parquet表转换
  - [hive/parquet 架构协调](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#hiveparquet-schema-reconciliation)
  - [元数据刷新](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#metadata-refreshing)
- [配置](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#configuration)

[Parquet](http://parquet.io/)是许多其他数据处理系统支持的列格式。Spark SQL提供对读取和写入Parquet文件的支持，这些文件会自动保留原始数据的架构。读取Parquet文件时，出于兼容性原因，所有列都将自动转换为可为空。

### 以编程方式加载数据

使用上面示例中的数据：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_r_0)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_SQL_0)

```scala
// Encoders for most common types are automatically provided by importing spark.implicits._
import spark.implicits._

val peopleDF = spark.read.json("examples/src/main/resources/people.json")

// DataFrames can be saved as Parquet files, maintaining the schema information
peopleDF.write.parquet("people.parquet")

// Read in the parquet file created above
// Parquet files are self-describing so the schema is preserved
// The result of loading a Parquet file is also a DataFrame
val parquetFileDF = spark.read.parquet("people.parquet")

// Parquet files can also be used to create a temporary view and then used in SQL statements
parquetFileDF.createOrReplaceTempView("parquetFile")
val namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19")
namesDF.map(attributes => "Name: " + attributes(0)).show()
// +------------+
// |       value|
// +------------+
// |Name: Justin|
// +------------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

### 分区发现

表分区是Hive等系统中常用的优化方法。在分区表中，数据通常存储在不同的目录中，分区列值编码在每个分区目录的路径中。所有内置文件源（包括Text / CSV / JSON / ORC / Parquet）都能够自动发现和推断分区信息。例如，我们可以使用以下目录结构将之前使用的所有填充数据存储到一个分区表中，该目录结构具有两个额外的列`gender`并`country`作为分区列：

```shell
path
└── to
    └── table
        ├── gender=male
        │   ├── ...
        │   │
        │   ├── country=US
        │   │   └── data.parquet
        │   ├── country=CN
        │   │   └── data.parquet
        │   └── ...
        └── gender=female
            ├── ...
            │
            ├── country=US
            │   └── data.parquet
            ├── country=CN
            │   └── data.parquet
            └── ...
```

通过传递`path/to/table`给`SparkSession.read.parquet`或`SparkSession.read.load`，Spark SQL将自动从路径中提取分区信息。现在，返回的DataFrame的架构变为：

```shell
root
|-- name: string (nullable = true)
|-- age: long (nullable = true)
|-- gender: string (nullable = true)
|-- country: string (nullable = true)
```

请注意，分区列的数据类型是自动推断的。当前，支持数字数据类型，日期，时间戳和字符串类型。有时用户可能不希望自动推断分区列的数据类型。对于这些用例，可以使用来配置自动类型推断 `spark.sql.sources.partitionColumnTypeInference.enabled`，默认为`true`。禁用类型推断时，字符串类型将用于分区列。

从Spark 1.6.0开始，默认情况下，分区发现仅在给定路径下查找分区。对于上面的示例，如果用户传递`path/to/table/gender=male`给 `SparkSession.read.parquet`或`SparkSession.read.load`，`gender`则不会被视为分区列。如果用户需要指定分区发现应开始的基本路径，则可以`basePath`在数据源选项中进行设置。例如，当`path/to/table/gender=male`数据路径是且用户设置`basePath`为时`path/to/table/`，`gender`将是一个分区列。

### 模式合并

与协议缓冲区，Avro和Thrift一样，Parquet也支持架构演变。用户可以从简单的架构开始，然后根据需要逐渐向架构中添加更多列。这样，用户可能最终得到具有不同但相互兼容的架构的多个Parquet文件。现在，Parquet数据源能够自动检测到这种情况并合并所有这些文件的模式。

由于模式合并是一项相对昂贵的操作，并且在大多数情况下不是必需的，因此默认情况下，我们从1.5.0开始将其关闭。您可以通过以下方式启用它

1. 将数据源选项设置`mergeSchema`为`true`在读取Parquet文件时（如下例所示），或者
2. 将全局SQL选项设置`spark.sql.parquet.mergeSchema`为`true`。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_scala_1)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_java_1)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_python_1)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_r_1)

```
// This is used to implicitly convert an RDD to a DataFrame.
import spark.implicits._

// Create a simple DataFrame, store into a partition directory
val squaresDF = spark.sparkContext.makeRDD(1 to 5).map(i => (i, i * i)).toDF("value", "square")
squaresDF.write.parquet("data/test_table/key=1")

// Create another DataFrame in a new partition directory,
// adding a new column and dropping an existing column
val cubesDF = spark.sparkContext.makeRDD(6 to 10).map(i => (i, i * i * i)).toDF("value", "cube")
cubesDF.write.parquet("data/test_table/key=2")

// Read the partitioned table
val mergedDF = spark.read.option("mergeSchema", "true").parquet("data/test_table")
mergedDF.printSchema()

// The final schema consists of all 3 columns in the Parquet files together
// with the partitioning column appeared in the partition directory paths
// root
//  |-- value: int (nullable = true)
//  |-- square: int (nullable = true)
//  |-- cube: int (nullable = true)
//  |-- key: int (nullable = true)
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

### Hive Metastore Parquet表转换

从Hive Metastore Parquet表读取并写入未分区的Hive Metastore Parquet表时，Spark SQL将尝试使用其自己的Parquet支持而不是Hive SerDe以获得更好的性能。此行为由`spark.sql.hive.convertMetastoreParquet` 配置控制，并且默认情况下处于启用状态。

#### Hive/Parquet架构协调

从表模式处理的角度来看，Hive和Parquet之间有两个关键区别。

1. Hive不区分大小写，而Parquet不区分大小写
2. Hive认为所有列都可为空，而Parquet中的可为空性很重要

由于这个原因，在将Hive Metastore Parquet表转换为Spark SQL Parquet表时，我们必须使Hive Metastore模式与Parquet模式一致。对帐规则为：

1. 在两个模式中具有相同名称的字段必须具有相同的数据类型，而不考虑可为空性。协调字段应具有Parquet端的数据类型，以便遵守可空性。
2. 协调的架构完全包含在Hive Metastore架构中定义的那些字段。
   - 仅出现在Parquet模式中的所有字段都将被放入对帐模式中。
   - 仅在Hive Metastore模式中出现的所有字段都将添加为已对帐模式中的可为空字段。

#### 元数据刷新

Spark SQL缓存Parquet元数据以获得更好的性能。启用Hive metastore Parquet表转换后，这些转换表的元数据也会被缓存。如果这些表是通过Hive或其他外部工具更新的，则需要手动刷新它们以确保元数据一致。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_scala_2)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_java_2)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_python_2)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_r_2)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-parquet.html#tab_SQL_2)

```
// spark is an existing SparkSession
spark.catalog.refreshTable("my_table")
```

### 配置

可以使用`setConf`on中的方法`SparkSession`或`SET key=value`使用SQL运行 命令来完成Parquet的配置。

| 物业名称                                 | 默认   | 含义                                                         | 自版本 |
| :--------------------------------------- | :----- | :----------------------------------------------------------- | :----- |
| `spark.sql.parquet.binaryAsString`       | 假     | 编写Parquet模式时，其他一些Parquet产生系统，尤其是Impala，Hive和旧版的Spark SQL，不会区分二进制数据和字符串。此标志告诉Spark SQL将二进制数据解释为字符串，以提供与这些系统的兼容性。 | 1.1.1  |
| `spark.sql.parquet.int96AsTimestamp`     | 真正   | 一些镶木地板生产系统，特别是Impala和Hive，将时间戳存储到INT96中。该标志告诉Spark SQL将INT96数据解释为时间戳，以提供与这些系统的兼容性。 | 1.3.0  |
| `spark.sql.parquet.compression.codec`    | 活泼的 | 设置编写Parquet文件时使用的压缩编解码器。如果任一`compression`或 `parquet.compression`在特定的表的选项/属性被指定时，所述优先级将是 `compression`，`parquet.compression`，`spark.sql.parquet.compression.codec`。可接受的值包括：none，未压缩，snappy，gzip，lzo，brotli，lz4，zstd。需要注意的是`zstd`需要`ZStandardCodec`Hadoop的2.9.0之前安装，`brotli`需要 `BrotliCodec`进行安装。 | 1.1.1  |
| `spark.sql.parquet.filterPushdown`       | 真正   | 设置为true时启用Parquet过滤器下推优化。                      | 1.2.0  |
| `spark.sql.hive.convertMetastoreParquet` | 真正   | 设置为false时，Spark SQL将使用Hive SerDe用于镶木表，而不是内置支持。 | 1.1.1  |
| `spark.sql.parquet.mergeSchema`          | 假     | 设置为true时，Parquet数据源合并从所有数据文件收集的架构，否则从摘要文件或随机数据文件（如果没有摘要文件可用）中选取该架构。 | 1.5.0  |
| `spark.sql.parquet.writeLegacyFormat`    | 假     | 如果为true，将以Spark 1.4及更早版本的方式写入数据。例如，十进制值将以Apache Parquet的固定长度字节数组格式编写，其他系统（例如Apache Hive和Apache Impala）将使用该格式。如果为false，将使用Parquet中较新的格式。例如，小数将以基于int的格式编写。如果Parquet输出打算用于不支持这种较新格式的系统，请设置为true。 | 1.6.0  |

# ORC文件

从Spark 2.3开始，Spark支持矢量化ORC读取器，其ORC文件具有新的ORC文件格式。为此，新添加了以下配置。`USING ORC`当`spark.sql.orc.impl` 设置为`native`和`spark.sql.orc.enableVectorizedReader`设置为时，矢量化阅读器用于本机ORC表（例如，使用子句创建的表）`true`。对于Hive ORC Serde表（例如，使用子句创建的表`USING HIVE OPTIONS (fileFormat 'ORC')`），将矢量化阅读器`spark.sql.hive.convertMetastoreOrc`还设置为`true`。

| **物业名称**                           | **默认** | **含义**                                                     | **自版本** |
| :------------------------------------- | :------- | :----------------------------------------------------------- | :--------- |
| `spark.sql.orc.impl`                   | `native` | ORC实现的名称。可以是`native`和之一`hive`。 `native`表示本机ORC支持。`hive`表示Hive中的ORC库。 | 2.3.0      |
| `spark.sql.orc.enableVectorizedReader` | `true`   | 在`native`实现中启用向量化orc解码。如果为`false`，则在`native`实现中使用新的非矢量化ORC读取器。为了`hive`实现，这被忽略。 | 2.3.0      |

# JSON文件

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-json.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-json.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-json.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-json.html#tab_r_0)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-json.html#tab_SQL_0)

Spark SQL可以自动推断JSON数据集的架构并将其作为加载`DatasetRow]`。这种转换是可以做到用`SparkSession.read.json()`在任一`Dataset[String]`或JSON文件。

请注意，作为*json文件*提供*的文件*不是典型的JSON文件。每行必须包含一个单独的，自包含的有效JSON对象。有关更多信息，请参见 [JSON Lines文本格式，也称为newline分隔的JSON](http://jsonlines.org/)。

对于常规的多行JSON文件，请将`multiLine`选项设置为`true`。

```scala
// Primitive types (Int, String, etc) and Product types (case classes) encoders are
// supported by importing this when creating a Dataset.
import spark.implicits._

// A JSON dataset is pointed to by path.
// The path can be either a single text file or a directory storing text files
val path = "examples/src/main/resources/people.json"
val peopleDF = spark.read.json(path)

// The inferred schema can be visualized using the printSchema() method
peopleDF.printSchema()
// root
//  |-- age: long (nullable = true)
//  |-- name: string (nullable = true)

// Creates a temporary view using the DataFrame
peopleDF.createOrReplaceTempView("people")

// SQL statements can be run by using the sql methods provided by spark
val teenagerNamesDF = spark.sql("SELECT name FROM people WHERE age BETWEEN 13 AND 19")
teenagerNamesDF.show()
// +------+
// |  name|
// +------+
// |Justin|
// +------+

// Alternatively, a DataFrame can be created for a JSON dataset represented by
// a Dataset[String] storing one JSON object per string
val otherPeopleDataset = spark.createDataset(
  """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
val otherPeople = spark.read.json(otherPeopleDataset)
otherPeople.show()
// +---------------+----+
// |        address|name|
// +---------------+----+
// |[Columbus,Ohio]| Yin|
// +---------------+----+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

# Hive表

- [指定Hive表的存储格式](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#specifying-storage-format-for-hive-tables)
- [与Hive Metastore的不同版本进行交互](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#interacting-with-different-versions-of-hive-metastore)

Spark SQL还支持读写存储在[Apache Hive中的](http://hive.apache.org/)数据。但是，由于Hive具有大量依赖关系，因此默认的Spark分发中不包含这些依赖关系。如果可以在类路径上找到Hive依赖项，Spark将自动加载它们。请注意，这些Hive依赖项也必须存在于所有工作节点上，因为它们将需要访问Hive序列化和反序列化库（SerDes）才能访问存储在Hive中的数据。

通过将`hive-site.xml`，`core-site.xml`（对于安全性配置）和`hdfs-site.xml`（对于HDFS配置）文件放置在中来配置Hive `conf/`。

使用Hive时，必须实例化`SparkSession`Hive支持，包括与持久性Hive元存储库的连接，对Hive Serdes的支持以及Hive用户定义的功能。没有现有Hive部署的用户仍可以启用Hive支持。如果未由配置`hive-site.xml`，则上下文会自动`metastore_db`在当前目录中创建并创建一个由配置`spark.sql.warehouse.dir`的目录`spark-warehouse`，该目录默认 为启动Spark应用程序的当前目录中的目录。请注意，自Spark 2.0.0起不推荐使用`hive.metastore.warehouse.dir`in的属性`hive-site.xml`。而是使用`spark.sql.warehouse.dir`指定仓库中数据库的默认位置。您可能需要向启动Spark应用程序的用户授予写权限。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-hive-tables.html#tab_r_0)

```scala
import java.io.File

import org.apache.spark.sql.{Row, SaveMode, SparkSession}

case class Record(key: Int, value: String)

// warehouseLocation points to the default location for managed databases and tables
val warehouseLocation = new File("spark-warehouse").getAbsolutePath

val spark = SparkSession
  .builder()
  .appName("Spark Hive Example")
  .config("spark.sql.warehouse.dir", warehouseLocation)
  .enableHiveSupport()
  .getOrCreate()

import spark.implicits._
import spark.sql

sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) USING hive")
sql("LOAD DATA LOCAL INPATH 'examples/src/main/resources/kv1.txt' INTO TABLE src")

// Queries are expressed in HiveQL
sql("SELECT * FROM src").show()
// +---+-------+
// |key|  value|
// +---+-------+
// |238|val_238|
// | 86| val_86|
// |311|val_311|
// ...

// Aggregation queries are also supported.
sql("SELECT COUNT(*) FROM src").show()
// +--------+
// |count(1)|
// +--------+
// |    500 |
// +--------+

// The results of SQL queries are themselves DataFrames and support all normal functions.
val sqlDF = sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key")

// The items in DataFrames are of type Row, which allows you to access each column by ordinal.
val stringsDS = sqlDF.map {
  case Row(key: Int, value: String) => s"Key: $key, Value: $value"
}
stringsDS.show()
// +--------------------+
// |               value|
// +--------------------+
// |Key: 0, Value: val_0|
// |Key: 0, Value: val_0|
// |Key: 0, Value: val_0|
// ...

// You can also use DataFrames to create temporary views within a SparkSession.
val recordsDF = spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
recordsDF.createOrReplaceTempView("records")

// Queries can then join DataFrame data with data stored in Hive.
sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show()
// +---+------+---+------+
// |key| value|key| value|
// +---+------+---+------+
// |  2| val_2|  2| val_2|
// |  4| val_4|  4| val_4|
// |  5| val_5|  5| val_5|
// ...

// Create a Hive managed Parquet table, with HQL syntax instead of the Spark SQL native syntax
// `USING hive`
sql("CREATE TABLE hive_records(key int, value string) STORED AS PARQUET")
// Save DataFrame to the Hive managed table
val df = spark.table("src")
df.write.mode(SaveMode.Overwrite).saveAsTable("hive_records")
// After insertion, the Hive managed table has data now
sql("SELECT * FROM hive_records").show()
// +---+-------+
// |key|  value|
// +---+-------+
// |238|val_238|
// | 86| val_86|
// |311|val_311|
// ...

// Prepare a Parquet data directory
val dataDir = "/tmp/parquet_data"
spark.range(10).write.parquet(dataDir)
// Create a Hive external Parquet table
sql(s"CREATE EXTERNAL TABLE hive_bigints(id bigint) STORED AS PARQUET LOCATION '$dataDir'")
// The Hive external table should already have data
sql("SELECT * FROM hive_bigints").show()
// +---+
// | id|
// +---+
// |  0|
// |  1|
// |  2|
// ... Order may vary, as spark processes the partitions in parallel.

// Turn on flag for Hive Dynamic Partitioning
spark.sqlContext.setConf("hive.exec.dynamic.partition", "true")
spark.sqlContext.setConf("hive.exec.dynamic.partition.mode", "nonstrict")
// Create a Hive partitioned table using DataFrame API
df.write.partitionBy("key").format("hive").saveAsTable("hive_part_tbl")
// Partitioned column `key` will be moved to the end of the schema.
sql("SELECT * FROM hive_part_tbl").show()
// +-------+---+
// |  value|key|
// +-------+---+
// |val_238|238|
// | val_86| 86|
// |val_311|311|
// ...

spark.stop()
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / hive / SparkHiveExample.scala”中找到完整的示例代码。

### 指定Hive表的存储格式

创建Hive表时，需要定义该表应如何从文件系统读取/写入数据，即“输入格式”和“输出格式”。您还需要定义该表应如何将数据反序列化为行，或将行序列化为数据，即“ serde”。以下选项可用于指定存储格式（“ serde”，“ input format”，“ output format”），例如`CREATE TABLE src(id int) USING hive OPTIONS(fileFormat 'parquet')`。默认情况下，我们将以纯文本形式读取表文件。请注意，创建表时尚不支持Hive存储处理程序，您可以在Hive端使用存储处理程序创建表，并使用Spark SQL读取表。

| 物业名称                                                     | 含义                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| `fileFormat`                                                 | fileFormat是一种存储格式规范的软件包，其中包括“ serde”，“ input format”和“ output format”。目前，我们支持6种文件格式：“ sequencefile”，“ rcfile”，“ orc”，“ parquet”，“ textfile”和“ avro”。 |
| `inputFormat, outputFormat`                                  | 这2个选项将对应名称`InputFormat`和`OutputFormat`类的名称指定为字符串文字，例如`org.apache.hadoop.hive.ql.io.orc.OrcInputFormat`。这两个选项必须成对出现，如果您已经指定了该`fileFormat`选项，则不能指定它们。 |
| `serde`                                                      | 此选项指定Serde类的名称。当`fileFormat`指定的选项，如果给定不指定此选项`fileFormat`已经包括SERDE的信息。当前，“ sequencefile”，“ textfile”和“ rcfile”不包含Serde信息，您可以将此选项与这3种fileFormats一起使用。 |
| `fieldDelim, escapeDelim, collectionDelim, mapkeyDelim, lineDelim` | 这些选项只能与“文本文件” fileFormat一起使用。它们定义了如何将分隔的文件读取为行。 |

用定义的所有其他属性`OPTIONS`将被视为Hive serde属性。

### 与Hive Metastore的不同版本进行交互

与Hive Metastore的交互是Spark SQL对Hive的最重要支持之一，它使Spark SQL能够访问Hive表的元数据。从Spark 1.4.0开始，使用以下描述的配置，可以使用Spark SQL的单个二进制版本来查询Hive元存储库的不同版本。请注意，与用于与metastore进行通信的Hive版本无关，Spark SQL在内部将针对内置Hive进行编译，并将这些类用于内部执行（serdes，UDF，UDAF等）。

以下选项可用于配置用于检索元数据的Hive版本：

| 物业名称                                   | 默认                                                         | 含义                                                         | 自版本 |
| :----------------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| `spark.sql.hive.metastore.version`         | `2.3.7`                                                      | Hive Metastore的版本。可用选项是`0.12.0`通过`2.3.7`和`3.0.0`通过`3.1.2`。 | 1.4.0  |
| `spark.sql.hive.metastore.jars`            | `builtin`                                                    | 用于实例化HiveMetastoreClient的jar的位置。此属性可以是以下三个选项之一：`builtin`使用Hive 2.3.7，它在`-Phive`启用后与Spark组件捆绑在一起。选择此选项时，`spark.sql.hive.metastore.version`必须`2.3.7`定义或未定义。`maven`使用从Maven存储库下载的指定版本的Hive jar。通常不建议将此配置用于生产部署。JVM的标准格式的类路径。该类路径必须包括所有Hive及其依赖项，包括正确的Hadoop版本。这些罐子只需要存在于驱动程序中，但是如果您以纱线簇模式运行，则必须确保将它们与您的应用程序打包在一起。 | 1.4.0  |
| `spark.sql.hive.metastore.sharedPrefixes`  | `com.mysql.jdbc,org.postgresql,com.microsoft.sqlserver,oracle.jdbc` | 以逗号分隔的类前缀列表，应使用在Spark SQL和特定版本的Hive之间共享的类加载器加载。应该共享的类的一个示例是与元存储区对话所需的JDBC驱动程序。需要共享的其他类是那些与已经共享的类进行交互的类。例如，log4j使用的自定义追加程序。 | 1.4.0  |
| `spark.sql.hive.metastore.barrierPrefixes` | `(empty)`                                                    | 用逗号分隔的类前缀列表，应为Spark SQL与之通信的每个Hive版本显式重新加载。例如，在通常会被共享的前缀中声明的Hive UDF（即`org.apache.spark.*`）。 | 1.4.0  |

# JDBC到其他数据库

Spark SQL还包括一个数据源，该数据源可以使用JDBC从其他数据库读取数据。与使用[JdbcRDD相比，](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/rdd/JdbcRDD.html)应该首选此功能。这是因为结果以DataFrame的形式返回，并且可以轻松地在Spark SQL中进行处理或与其他数据源合并。JDBC数据源也更易于从Java或Python使用，因为它不需要用户提供ClassTag。（请注意，这与Spark SQL JDBC服务器不同，后者允许其他应用程序使用Spark SQL运行查询）。

首先，您需要在spark类路径上包含特定数据库的JDBC驱动程序。例如，要从Spark Shell连接到postgres，您可以运行以下命令：

```shell
./bin/spark-shell --driver-class-path postgresql-9.4.1207.jar --jars postgresql-9.4.1207.jar
```

可以使用数据源API将远程数据库中的表加载为DataFrame或Spark SQL临时视图。用户可以在数据源选项中指定JDBC连接属性。 `user`和`password`通常用于登录到数据源提供为连接属性。除连接属性外，Spark还支持以下不区分大小写的选项：

| 物业名称                                  | 含义                                                         |
| :---------------------------------------- | :----------------------------------------------------------- |
| `url`                                     | 要连接的JDBC URL。特定于源的连接属性可以在URL中指定。例如，`jdbc:postgresql://localhost/test?user=fred&password=secret` |
| `dbtable`                                 | 应该从中读取或写入的JDBC表。请注意，在读取路径中使用它时，可以使用在`FROM`SQL查询子句中有效的任何东西。例如，除了完整表之外，您还可以在括号中使用子查询。不允许同时指定`dbtable`和`query`选项。 |
| `query`                                   | 用于将数据读入Spark的查询。指定的查询将加括号，并在`FROM`子句中用作子查询。Spark还将为子查询子句分配一个别名。例如，spark将向JDBC源发出以下形式的查询。  `SELECT <columns> FROM (<user_specified_query>) spark_gen_alias`  以下是使用此选项时的一些限制。 不允许同时指定`dbtable`和`query`选项。不允许同时指定`query`和`partitionColumn`选项。如果`partitionColumn`需要指定 option，则可以使用`dbtable`option代替子查询，并且可以使用一部分提供的子查询别名来限定分区列`dbtable`。 例： `spark.read.format("jdbc").option("url", jdbcUrl).option("query", "select c1, c2 from t1").load()` |
| `driver`                                  | 用于连接到该URL的JDBC驱动程序的类名。                        |
| `partitionColumn, lowerBound, upperBound` | 如果指定了这些选项，则必须全部指定。另外， `numPartitions`必须指定。它们描述了从多个工作程序并行读取时如何对表进行分区。 `partitionColumn`必须是相关表格中的数字，日期或时间戳列。请注意，`lowerBound`和`upperBound`仅用于确定分区的步幅，而不是用于过滤表中的行。因此，表中的所有行都将被分区并返回。此选项仅适用于阅读。 |
| `numPartitions`                           | 可用于表读写的并行性的最大分区数。这也确定了并发JDBC连接的最大数量。如果要写入的分区数超过此限制，我们可以通过`coalesce(numPartitions)`在写入之前进行调用将其降低到此限制。 |
| `queryTimeout`                            | 驱动程序将等待Statement对象执行到给定秒数的秒数。零表示没有限制。在写路径中，此选项取决于JDBC驱动程序如何实现API `setQueryTimeout`，例如，h2 JDBC驱动程序将检查每个查询的超时，而不是整个JDBC批处理的超时。默认为`0`。 |
| `fetchsize`                               | JDBC的获取大小，它确定每次往返要获取多少行。这可以帮助提高JDBC驱动程序的性能，该驱动程序默认为较小的访存大小（例如，具有10行的Oracle）。此选项仅适用于阅读。 |
| `batchsize`                               | JDBC批处理大小，它确定每次往返要插入多少行。这可以帮助提高JDBC驱动程序的性能。此选项仅适用于写作。默认为`1000`。 |
| `isolationLevel`                          | 事务隔离级别，适用于当前连接。它可以是一个`NONE`，`READ_COMMITTED`，`READ_UNCOMMITTED`，`REPEATABLE_READ`，或`SERIALIZABLE`，对应于由JDBC的连接对象定义，缺省值为标准事务隔离级别`READ_UNCOMMITTED`。此选项仅适用于写作。请参阅中的文档`java.sql.Connection`。 |
| `sessionInitStatement`                    | 在向远程数据库打开每个数据库会话之后且开始读取数据之前，此选项将执行自定义SQL语句（或PL / SQL块）。使用它来实现会话初始化代码。例：`option("sessionInitStatement", """BEGIN execute immediate 'alter session set "_serial_direct_read"=true'; END;""")` |
| `truncate`                                | 这是与JDBC编写器相关的选项。当`SaveMode.Overwrite`启用时，该选项的原因星火截断，而不是删除和重建其现有的表。这可以更有效，并防止删除表元数据（例如索引）。但是，在某些情况下，例如新数据具有不同的架构时，它将无法使用。默认为`false`。此选项仅适用于写作。 |
| `cascadeTruncate`                         | 这是与JDBC编写器相关的选项。如果由JDBC数据库（当前为PostgreSQL和Oracle）启用并支持，则此选项允许执行a `TRUNCATE TABLE t CASCADE`（在PostgreSQL`TRUNCATE TABLE ONLY t CASCADE`中，执行a可以防止无意中截断后代表）。这将影响其他表，因此应谨慎使用。此选项仅适用于写作。它默认为`isCascadeTruncate`每个JDBCDialect中指定的有关JDBC数据库的默认级联截断行为。 |
| `createTableOptions`                      | 这是与JDBC编写器相关的选项。如果指定，则此选项允许在创建表（例如`CREATE TABLE t (name string) ENGINE=InnoDB.`）时设置特定于数据库的表和分区选项。此选项仅适用于写作。 |
| `createTableColumnTypes`                  | 创建表时要使用的数据库列数据类型，而不是缺省值。数据类型信息应以与CREATE TABLE列语法相同的格式指定（例如：`"name CHAR(64), comments VARCHAR(1024)")`。指定的类型应为有效的spark sql数据类型。此选项仅适用于写入。 |
| `customSchema`                            | 用于从JDBC连接器读取数据的自定义架构。例如，`"id DECIMAL(38, 0), name STRING"`。您还可以指定部分字段，其他部分使用默认类型映射。例如，`"id DECIMAL(38, 0)"`。列名应与JDBC表的相应列名相同。用户可以指定Spark SQL的相应数据类型，而不必使用默认值。此选项仅适用于阅读。 |
| `pushDownPredicate`                       | 用于启用或禁用谓词下推到JDBC数据源的选项。默认值为true，在这种情况下，Spark将尽可能将过滤器下推到JDBC数据源。否则，如果设置为false，则不会将任何过滤器下推到JDBC数据源，因此所有过滤器将由Spark处理。当Spark进行谓词筛选的速度比JDBC数据源执行谓词筛选的速度快时，通常会关闭谓词下推。 |

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-jdbc.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-jdbc.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-jdbc.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-jdbc.html#tab_r_0)
- [**SQL**](http://spark.apache.org/docs/latest/sql-data-sources-jdbc.html#tab_SQL_0)

```scala
// Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
// Loading data from a JDBC source
val jdbcDF = spark.read
  .format("jdbc")
  .option("url", "jdbc:postgresql:dbserver")
  .option("dbtable", "schema.tablename")
  .option("user", "username")
  .option("password", "password")
  .load()

val connectionProperties = new Properties()
connectionProperties.put("user", "username")
connectionProperties.put("password", "password")
val jdbcDF2 = spark.read
  .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)
// Specifying the custom data types of the read schema
connectionProperties.put("customSchema", "id DECIMAL(38, 0), name STRING")
val jdbcDF3 = spark.read
  .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)

// Saving data to a JDBC source
jdbcDF.write
  .format("jdbc")
  .option("url", "jdbc:postgresql:dbserver")
  .option("dbtable", "schema.tablename")
  .option("user", "username")
  .option("password", "password")
  .save()

jdbcDF2.write
  .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)

// Specifying create table column data types on write
jdbcDF.write
  .option("createTableColumnTypes", "name CHAR(64), comments VARCHAR(1024)")
  .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties)
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SQLDataSourceExample.scala”中找到完整的示例代码。

# Apache Avro数据源指南

- [部署中](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#deploying)
- [加载和保存功能](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#load-and-save-functions)
- [to_avro（）和from_avro（）](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#to_avro-and-from_avro)
- [数据源选项](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#data-source-option)
- [配置](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#configuration)
- [与Databricks spark-avro的兼容性](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#compatibility-with-databricks-spark-avro)
- [Avro-> Spark SQL转换支持的类型](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#supported-types-for-avro---spark-sql-conversion)
- [Spark SQL支持的类型-> Avro转换](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#supported-types-for-spark-sql---avro-conversion)

自Spark 2.4发布以来，[Spark SQL](https://spark.apache.org/docs/latest/sql-programming-guide.html)提供了内置支持，用于读写Apache Avro数据。

## 部署中

该`spark-avro`模块是外部并且不包括在`spark-submit`或`spark-shell`默认。

与任何Spark应用程序一样，`spark-submit`用于启动您的应用程序。`spark-avro_2.12` 其依赖项可以直接添加到`spark-submit`使用中`--packages`，例如，

```shell
./bin/spark-submit --packages org.apache.spark:spark-avro_2.12:3.0.1 ...
```

为了进行实验`spark-shell`，您还可以直接使用`--packages`add`org.apache.spark:spark-avro_2.12`及其依赖项，

```shell
./bin/spark-shell --packages org.apache.spark:spark-avro_2.12:3.0.1 ...
```

有关提交具有外部依赖关系的应用程序的更多详细信息，请参见《[应用程序提交指南》](http://spark.apache.org/docs/latest/submitting-applications.html)。

## 加载和保存功能

由于`spark-avro`模块是外部模块，因此或中没有`.avro`API 。`DataFrameReader``DataFrameWriter`

要以Avro格式加载/保存数据，您需要将数据源选项指定`format`为`avro`（或`org.apache.spark.sql.avro`）。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#tab_r_0)

```scala
val usersDF = spark.read.format("avro").load("examples/src/main/resources/users.avro")
usersDF.select("name", "favorite_color").write.format("avro").save("namesAndFavColors.avro")
```

## to_avro（）和from_avro（）

Avro软件包提供`to_avro`了将列编码为Avro格式的二进制，以及`from_avro()`将Avro二进制数据解码为列的功能。这两个函数都将一列转换为另一列，并且输入/输出SQL数据类型可以是复杂类型或原始类型。

从诸如Kafka的流媒体源读取或写入时，将Avro记录用作列很有用。每条卡夫卡键值记录都将添加一些元数据，例如，卡夫卡的提取时间戳，卡夫卡的偏移量等。

- 如果包含数据的“值”字段位于Avro中，则可以`from_avro()`用来提取数据，充实数据，清理数据，然后将其再次向下游推送到Kafka或写到文件中。
- `to_avro()`可用于将结构转换为Avro记录。当您希望在将数据写出到Kafka时将多列重新编码为单个列时，此方法特别有用。

这两个功能目前仅在Scala和Java中可用。

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#tab_scala_1)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#tab_java_1)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-avro.html#tab_python_1)

```scala
import org.apache.spark.sql.avro.functions._

// `from_avro` requires Avro schema in JSON string format.
val jsonFormatSchema = new String(Files.readAllBytes(Paths.get("./examples/src/main/resources/user.avsc")))

val df = spark
  .readStream
  .format("kafka")
  .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
  .option("subscribe", "topic1")
  .load()

// 1. Decode the Avro data into a struct;
// 2. Filter by column `favorite_color`;
// 3. Encode the column `name` in Avro format.
val output = df
  .select(from_avro('value, jsonFormatSchema) as 'user)
  .where("user.favorite_color == \"red\"")
  .select(to_avro($"user.name") as 'value)

val query = output
  .writeStream
  .format("kafka")
  .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
  .option("topic", "topic2")
  .start()
```

## 数据源选项

可以通过以下方式设置Avro的数据源选项：

- 或`.option`上的方法。`DataFrameReader``DataFrameWriter`
- `options`函数中的参数`from_avro`。

| **物业名称**      | **默认**       | **含义**                                                     | **范围**                 |
| :---------------- | :------------- | :----------------------------------------------------------- | :----------------------- |
| `avroSchema`      | 没有           | 用户以JSON格式提供的可选架构。读取Avro时，可以将此选项设置为演进的架构，该架构与实际的Avro架构兼容但有所不同。反序列化方案将与演进方案保持一致。例如，如果我们设置一个包含默认值的另外一列的演进模式，Spark中的读取结果也将包含新列。编写Avro时，如果预期的输出Avro模式与Spark转换的模式不匹配，则可以设置此选项。例如，一列的预期模式为“枚举”类型，而不是默认转换模式中的“字符串”类型。 | 读，写和功能 `from_avro` |
| `recordName`      | topLevelRecord | 写结果中的顶级记录名称，这在Avro规范中是必需的。             | 写                       |
| `recordNamespace` | ”              | 在写结果中记录名称空间。                                     | 写                       |
| `ignoreExtension` | 真正           | 该选项控制忽略`.avro`读取中没有扩展名的文件。 如果启用该选项，则将`.avro`加载所有文件（带有和不带有扩展名）。 该选项已被弃用，它将在将来的版本中删除。请使用常规数据源选项[pathGlobFilter](http://spark.apache.org/docs/latest/sql-data-sources-generic-options.html#path-global-filter)过滤文件名。 | 读                       |
| `compression`     | 活泼的         | 该`compression`选项允许指定写入中使用的压缩编解码器。 目前支持的编解码器`uncompressed`，`snappy`，`deflate`，`bzip2`和`xz`。 如果未设置该选项，那么将`spark.sql.avro.compression.codec`考虑配置config。 | 写                       |
| `mode`            | 失败           | 该`mode`选项允许为function指定解析模式`from_avro`。 当前支持的模式是：`FAILFAST`：在处理损坏的记录时引发异常。`PERMISSIVE`：损坏的记录被处理为空结果。因此，数据模式被强制为完全可为空，这可能与所提供的一个用户不同。 | 功能 `from_avro`         |

## 配置

可以使用`setConf`SparkSession上的方法或`SET key=value`使用SQL运行命令来完成Avro的配置。

| **偏好名称**                                        | **默认** | **含义**                                                     | **自版本** |
| :-------------------------------------------------- | :------- | :----------------------------------------------------------- | :--------- |
| spark.sql.legacy.replaceDatabricksSparkAvro.enabled | 真正     | 如果将其设置为true，则数据源提供程序`com.databricks.spark.avro`将映射到内置但外部的Avro数据源模块，以实现向后兼容性。 | 2.4.0      |
| spark.sql.avro.compression.codec                    | 活泼的   | 编写AVRO文件时使用的压缩编解码器。支持的编解码器：未压缩，放气，快照，bzip2和xz。默认编解码器是快照。 | 2.4.0      |
| spark.sql.avro.deflate.level                        | -1       | 编写AVRO文件时使用的放气编解码器的压缩级别。有效值必须在1到9（含1）或-1的范围内。默认值为-1，对应于当前实现中的6级。 | 2.4.0      |

## 与Databricks spark-avro的兼容性

该Avro数据源模块最初来自Databricks的开源存储库[spark-avro](https://github.com/databricks/spark-avro)并与之兼容 。

默认情况下，`spark.sql.legacy.replaceDatabricksSparkAvro.enabled`启用SQL配置后，数据源提供程序`com.databricks.spark.avro`将映射到此内置Avro模块。对于在目录元存储中使用`Provider`属性创建的Spark表`com.databricks.spark.avro`，如果使用此内置Avro模块，则映射对于加载这些表必不可少。

在Databricks注[火花Avro的](https://github.com/databricks/spark-avro)，隐含的类 `AvroDataFrameWriter`和`AvroDataFrameReader`用于快捷功能创建`.avro()`。在此内置但外部模块中，两个隐式类均被删除。请使用`.format("avro")`in `DataFrameWriter`或`DataFrameReader`代替，它应该干净并且足够好。

如果您喜欢使用自己的`spark-avro`jar文件构建，则只需禁用配置 `spark.sql.legacy.replaceDatabricksSparkAvro.enabled`，然后`--jars`在部署应用程序时使用该选项。有关更多详细信息，请阅读《应用程序提交指南》中的“[高级依赖关系管理”](https://spark.apache.org/docs/latest/submitting-applications.html#advanced-dependency-management)部分。

## Avro-> Spark SQL转换支持的类型

目前，Spark支持读取Avro记录下的所有[原始类型](https://avro.apache.org/docs/1.8.2/spec.html#schema_primitive)和[复杂类型](https://avro.apache.org/docs/1.8.2/spec.html#schema_complex)。

| **Avro type** | **Spark SQL type** |
| :------------ | :----------------- |
| boolean       | BooleanType        |
| int           | IntegerType        |
| long          | LongType           |
| float         | FloatType          |
| double        | DoubleType         |
| string        | StringType         |
| enum          | StringType         |
| fixed         | BinaryType         |
| bytes         | BinaryType         |
| record        | StructType         |
| array         | ArrayType          |
| map           | MapType            |
| union         | See below          |

除了上面列出的类型之外，它还支持阅读`union`类型。以下三种类型被视为基本`union`类型：

1. `union(int, long)` 将被映射到LongType。
2. `union(float, double)` 将被映射到DoubleType。
3. `union(something, null)`，其中任何受支持的Avro类型。这将被映射为与某些事物相同的Spark SQL类型，并将nullable设置为true。所有其他联合类型都被认为是复杂的。根据联合的成员，它们将被映射到字段名称为member0，member1等的StructType。这与在Avro和Parquet之间转换时的行为一致。

它还支持读取以下Avro[逻辑类型](https://avro.apache.org/docs/1.8.2/spec.html#Logical+Types)：

| **Avro 逻辑类型** | **Avro type** | **Spark SQL type** |
| :---------------- | :------------ | :----------------- |
| date              | int           | DateType           |
| timestamp-millis  | long          | TimestampType      |
| timestamp-micros  | long          | TimestampType      |
| decimal           | fixed         | DecimalType        |
| decimal           | bytes         | DecimalType        |

目前，它会忽略Avro文件中存在的文档，别名和其他属性。

## Spark SQL支持的类型-> Avro转换

Spark支持将所有Spark SQL类型写入Avro。对于大多数类型，从Spark类型到Avro类型的映射非常简单（例如，将IntegerType转换为int）。但是，下面列出了一些特殊情况：

| **Spark SQL type** | **Avro type** | **Avro logical type** |
| :----------------- | :------------ | :-------------------- |
| ByteType           | int           |                       |
| ShortType          | int           |                       |
| BinaryType         | bytes         |                       |
| DateType           | int           | date                  |
| TimestampType      | long          | timestamp-micros      |
| DecimalType        | fixed         | decimal               |

您还可以使用选项指定整个输出Avro模式`avroSchema`，以便可以将Spark SQL类型转换为其他Avro类型。默认情况下，不应用以下转换，并且需要用户指定的Avro模式：

| **Spark SQL type** | **Avro type** | **Avro logical type** |
| :----------------- | :------------ | :-------------------- |
| BinaryType         | fixed         |                       |
| StringType         | enum          |                       |
| TimestampType      | long          | timestamp-millis      |
| DecimalType        | bytes         | decimal               |

# 二进制文件数据源

从Spark 3.0开始，Spark支持二进制文件数据源，该文件源读取二进制文件并将每个文件转换为包含该文件的原始内容和元数据的单个记录。它产生一个具有以下列以及可能的分区列的DataFrame：

- `path`：StringType
- `modificationTime`：TimestampType
- `length`：长型
- `content`：BinaryType

要读取整个二进制文件，您需要将数据源指定`format`为`binaryFile`。要加载路径与给定glob模式匹配的文件，同时保持分区发现的行为，可以使用general data source选项`pathGlobFilter`。例如，以下代码从输入目录读取所有PNG文件：

- [**Scala**](http://spark.apache.org/docs/latest/sql-data-sources-binaryFile.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-data-sources-binaryFile.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-data-sources-binaryFile.html#tab_python_0)
- [**R**](http://spark.apache.org/docs/latest/sql-data-sources-binaryFile.html#tab_r_0)

```scala
spark.read.format("binaryFile").option("pathGlobFilter", "*.png").load("/path/to/data")
```

二进制文件数据源不支持将DataFrame写回原始文件。

# 故障排除

- JDBC驱动程序类在客户端会话和所有执行程序上必须对原始类加载器可见。这是因为Java的DriverManager类进行了安全检查，导致它忽略了当打开连接时原始类加载器不可见的所有驱动程序。一种方便的方法是修改所有工作程序节点上的compute_classpath.sh以包括您的驱动程序JAR。
- 某些数据库（例如H2）会将所有名称都转换为大写。您需要使用大写字母在Spark SQL中引用这些名称。
- 用户可以在数据源选项中指定特定于供应商的JDBC连接属性，以进行特殊处理。例如，`spark.read.format("jdbc").option("url", oracleJdbcUrl).option("oracle.jdbc.mapDateToTimestamp", "false")`。`oracle.jdbc.mapDateToTimestamp`默认为true，用户通常需要禁用此标志，以避免将Oracle日期解析为时间戳。