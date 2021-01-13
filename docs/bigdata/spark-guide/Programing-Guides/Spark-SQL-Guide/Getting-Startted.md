# 入门

- [起点：SparkSession](http://spark.apache.org/docs/latest/sql-getting-started.html#starting-point-sparksession)
- [创建DataFrames](http://spark.apache.org/docs/latest/sql-getting-started.html#creating-dataframes)
- [无类型的数据集操作（也称为DataFrame操作）](http://spark.apache.org/docs/latest/sql-getting-started.html#untyped-dataset-operations-aka-dataframe-operations)
- [以编程方式运行SQL查询](http://spark.apache.org/docs/latest/sql-getting-started.html#running-sql-queries-programmatically)
- [全局临时视图](http://spark.apache.org/docs/latest/sql-getting-started.html#global-temporary-view)
- [创建数据集](http://spark.apache.org/docs/latest/sql-getting-started.html#creating-datasets)
- 与RDD互操作
  - [使用反射推断结构](http://spark.apache.org/docs/latest/sql-getting-started.html#inferring-the-schema-using-reflection)
  - [以编程方式指定结构](http://spark.apache.org/docs/latest/sql-getting-started.html#programmatically-specifying-the-schema)
- [标量函数](http://spark.apache.org/docs/latest/sql-getting-started.html#scalar-functions)
- [汇总功能](http://spark.apache.org/docs/latest/sql-getting-started.html#aggregate-functions)

## 起点：SparkSession

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_0)
- [**Python**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_python_0)
- [**[R**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_r_0)

[`SparkSession`](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/SparkSession.html)类是Spark中所有功能的入口点。要创建一个基本的`SparkSession`，只需使用`SparkSession.builder()`：

```scala
import org.apache.spark.sql.SparkSession

val spark = SparkSession
  .builder()
  .appName("Spark SQL basic example")
  .config("spark.some.config.option", "some-value")
  .getOrCreate()

// For implicit conversions like converting RDDs to DataFrames
import spark.implicits._
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

`SparkSession`Spark 2.0中的内置支持Hive功能，包括使用HiveQL编写查询，访问Hive UDF以及从Hive表读取数据的功能。要使用这些功能，您不需要现有的Hive设置。

## 创建 DataFrames

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_1)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_1)
- [**Python**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_python_1)
- [**R**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_r_1) 

使用`SparkSession`，应用程序可以从[现有的`RDD`](http://spark.apache.org/docs/latest/sql-getting-started.html#interoperating-with-rdds)，Hive表的或[Spark数据源](http://spark.apache.org/docs/latest/sql-data-sources.html)创建DataFrame 。

例如，以下内容基于JSON文件的内容创建一个DataFrame：

```scala
val df = spark.read.json("examples/src/main/resources/people.json")

// Displays the content of the DataFrame to stdout
df.show()
// +----+-------+
// | age|   name|
// +----+-------+
// |null|Michael|
// |  30|   Andy|
// |  19| Justin|
// +----+-------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

## 无类型的数据集操作（也称为DataFrame操作）

DataFrames为[Scala](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/Dataset.html)，[Java](http://spark.apache.org/docs/latest/api/java/index.html?org/apache/spark/sql/Dataset.html)，[Python](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.DataFrame)和[R中的](http://spark.apache.org/docs/latest/api/R/SparkDataFrame.html)结构化数据操作提供了一种特定于域的语言。

如上所述，在Spark 2.0中，DataFrames只是`Row`Scala和Java API中的的Dataset 。与强类型的Scala / Java数据集附带的“类型转换”相反，这些操作也称为“非类型转换”。

这里我们提供一些使用数据集进行结构化数据处理的基本示例：

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_2)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_2)
- [**Python**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_python_2)
- [**R**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_r_2)

```scala
// This import is needed to use the $-notation
import spark.implicits._
// Print the schema in a tree format
df.printSchema()
// root
// |-- age: long (nullable = true)
// |-- name: string (nullable = true)

// Select only the "name" column
df.select("name").show()
// +-------+
// |   name|
// +-------+
// |Michael|
// |   Andy|
// | Justin|
// +-------+

// Select everybody, but increment the age by 1
df.select($"name", $"age" + 1).show()
// +-------+---------+
// |   name|(age + 1)|
// +-------+---------+
// |Michael|     null|
// |   Andy|       31|
// | Justin|       20|
// +-------+---------+

// Select people older than 21
df.filter($"age" > 21).show()
// +---+----+
// |age|name|
// +---+----+
// | 30|Andy|
// +---+----+

// Count people by age
df.groupBy("age").count().show()
// +----+-----+
// | age|count|
// +----+-----+
// |  19|    1|
// |null|    1|
// |  30|    1|
// +----+-----+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

有关可对数据集执行的操作类型的完整列表，请参阅[API文档](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/Dataset.html)。

除了简单的列引用和表达式外，数据集还具有丰富的函数库，包括字符串处理，日期算术，通用数学运算等。完整列表可在[DataFrame Function Reference中找到](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/functions$.html)。

## 以编程方式运行SQL查询

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_3)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_3)
- [**Python**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_python_3)
- [**[R**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_r_3)

上的`sql`函数`SparkSession`使应用程序能够以编程方式运行SQL查询，并以形式返回结果`DataFrame`。

```scala
// Register the DataFrame as a SQL temporary view
df.createOrReplaceTempView("people")

val sqlDF = spark.sql("SELECT * FROM people")
sqlDF.show()
// +----+-------+
// | age|   name|
// +----+-------+
// |null|Michael|
// |  30|   Andy|
// |  19| Justin|
// +----+-------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

## 全局临时视图

Spark SQL中的临时视图是会话作用域的，如果创建它的会话终止，它将消失。如果您希望拥有一个在所有会话之间共享的临时视图，并且在Spark应用程序终止之前一直保持活动状态，则可以创建全局临时视图。全局临时视图与系统保留的数据库相关联`global_temp`，我们必须使用限定名称来引用它，例如`SELECT * FROM global_temp.view1`。

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_4)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_4)
- [**Python**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_python_4)
- [**的SQL**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_SQL_4)

```scala
// Register the DataFrame as a global temporary view
df.createGlobalTempView("people")

// Global temporary view is tied to a system preserved database `global_temp`
spark.sql("SELECT * FROM global_temp.people").show()
// +----+-------+
// | age|   name|
// +----+-------+
// |null|Michael|
// |  30|   Andy|
// |  19| Justin|
// +----+-------+

// Global temporary view is cross-session
spark.newSession().sql("SELECT * FROM global_temp.people").show()
// +----+-------+
// | age|   name|
// +----+-------+
// |null|Michael|
// |  30|   Andy|
// |  19| Justin|
// +----+-------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

## 创建数据集

数据集与RDD相似，但是它们不是使用Java序列化或Kryo，而是使用专用的[Encoder](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/Encoder.html)对对象进行序列化以进行网络处理或传输。虽然编码器和标准序列化都负责将对象转换为字节，但是编码器是动态生成的代码，并使用一种格式，该格式允许Spark执行许多操作，如过滤，排序和哈希处理，而无需将字节反序列化为对象。

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_5)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_5)

```scala
case class Person(name: String, age: Long)

// Encoders are created for case classes
val caseClassDS = Seq(Person("Andy", 32)).toDS()
caseClassDS.show()
// +----+---+
// |name|age|
// +----+---+
// |Andy| 32|
// +----+---+

// Encoders for most common types are automatically provided by importing spark.implicits._
val primitiveDS = Seq(1, 2, 3).toDS()
primitiveDS.map(_ + 1).collect() // Returns: Array(2, 3, 4)

// DataFrames can be converted to a Dataset by providing a class. Mapping will be done by name
val path = "examples/src/main/resources/people.json"
val peopleDS = spark.read.json(path).as[Person]
peopleDS.show()
// +----+-------+
// | age|   name|
// +----+-------+
// |null|Michael|
// |  30|   Andy|
// |  19| Justin|
// +----+-------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

## 与RDD互操作

Spark SQL支持两种将现有RDD转换为数据集的方法。第一种方法使用反射来推断包含特定对象类型的RDD的架构。这种基于反射的方法可以使代码更简洁，当您在编写Spark应用程序时已经了解架构时，可以很好地工作。

创建数据集的第二种方法是通过编程界面，该界面允许您构造模式，然后将其应用于现有的RDD。尽管此方法较为冗长，但可以在运行时才知道列及其类型的情况下构造数据集。

### 使用反射推断架构

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_6)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_6)
- [**Python**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_python_6)

Spark SQL的Scala接口支持将包含案例类的RDD自动转换为DataFrame。案例类定义表的架构。案例类的参数名称使用反射读取，并成为列的名称。Case类也可以嵌套或包含`Seq`s或`Array`s之类的复杂类型。可以将该RDD隐式转换为DataFrame，然后将其注册为表。可以在后续的SQL语句中使用表。

```scala
// For implicit conversions from RDDs to DataFrames
import spark.implicits._

// Create an RDD of Person objects from a text file, convert it to a Dataframe
val peopleDF = spark.sparkContext
  .textFile("examples/src/main/resources/people.txt")
  .map(_.split(","))
  .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
  .toDF()
// Register the DataFrame as a temporary view
peopleDF.createOrReplaceTempView("people")

// SQL statements can be run by using the sql methods provided by Spark
val teenagersDF = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 19")

// The columns of a row in the result can be accessed by field index
teenagersDF.map(teenager => "Name: " + teenager(0)).show()
// +------------+
// |       value|
// +------------+
// |Name: Justin|
// +------------+

// or by field name
teenagersDF.map(teenager => "Name: " + teenager.getAs[String]("name")).show()
// +------------+
// |       value|
// +------------+
// |Name: Justin|
// +------------+

// No pre-defined encoders for Dataset[Map[K,V]], define explicitly
implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
// Primitive types and case classes can be also defined as
// implicit val stringIntMapEncoder: Encoder[Map[String, Any]] = ExpressionEncoder()

// row.getValuesMap[T] retrieves multiple columns at once into a Map[String, T]
teenagersDF.map(teenager => teenager.getValuesMap[Any](List("name", "age"))).collect()
// Array(Map("name" -> "Justin", "age" -> 19))
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

### 以编程方式指定架构

- [**Scala**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_scala_7)
- [**Java**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_java_7)
- [**Python**](http://spark.apache.org/docs/latest/sql-getting-started.html#tab_python_7)

如果无法提前定义案例类（例如，记录的结构编码为字符串，或者将解析文本数据集，并且针对不同的用户对字段进行不同的投影），则可以通过三个步骤以编程方式创建DataFrame 。

1. `Row`从原始RDD创建一个的RDD； 
2. 在步骤1中创建的RDD中，创建`StructType`与`Row`s的结构匹配 的模式。
3. `Row`通过`createDataFrame`提供的方法将架构应用于的RDD `SparkSession`。

例如：

```scala
import org.apache.spark.sql.Row

import org.apache.spark.sql.types._

// Create an RDD
val peopleRDD = spark.sparkContext.textFile("examples/src/main/resources/people.txt")

// The schema is encoded in a string
val schemaString = "name age"

// Generate the schema based on the string of schema
val fields = schemaString.split(" ")
  .map(fieldName => StructField(fieldName, StringType, nullable = true))
val schema = StructType(fields)

// Convert records of the RDD (people) to Rows
val rowRDD = peopleRDD
  .map(_.split(","))
  .map(attributes => Row(attributes(0), attributes(1).trim))

// Apply the schema to the RDD
val peopleDF = spark.createDataFrame(rowRDD, schema)

// Creates a temporary view using the DataFrame
peopleDF.createOrReplaceTempView("people")

// SQL can be run over a temporary view created using DataFrames
val results = spark.sql("SELECT name FROM people")

// The results of SQL queries are DataFrames and support all the normal RDD operations
// The columns of a row in the result can be accessed by field index or by field name
results.map(attributes => "Name: " + attributes(0)).show()
// +-------------+
// |        value|
// +-------------+
// |Name: Michael|
// |   Name: Andy|
// | Name: Justin|
// +-------------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

## 标量函数

标量函数是每行返回一个值的函数，而聚合函数则返回一组行的值。Spark SQL支持多种[内置标量函数](http://spark.apache.org/docs/latest/sql-ref-functions.html#scalar-functions)。它还支持[用户定义的标量函数](http://spark.apache.org/docs/latest/sql-ref-functions-udf-scalar.html)。

## 汇总功能

聚合函数是在一组行上返回单个值的函数。该[内置聚合函数](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#aggregate-functions)提供通用聚合如`count()`，`countDistinct()`，`avg()`，`max()`，`min()`，等用户不限于预定义的聚集功能，可以创建自己的。有关用户定义的聚合函数的更多详细信息，请参阅《[用户定义的聚合函数](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html)》文档 。