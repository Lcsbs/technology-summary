# 标量用户定义函数（UDF）

### 描述

用户定义函数（UDF）是作用于一行的用户可编程例程。本文档列出了创建和注册UDF所需的类。它还包含一些示例，这些示例演示了如何定义和注册UDF以及如何在Spark SQL中调用它们。

### UserDefinedFunction

要定义用户定义函数的属性，用户可以使用此类中定义的某些方法。

- **asNonNullable（）：UserDefinedFunction**

  将UserDefinedFunction更新为不可为空。

- **asNondeterministic（）：UserDefinedFunction**

  将UserDefinedFunction更新为不确定的。

- **withName（name：String）：UserDefinedFunction**

  用给定名称更新UserDefinedFunction。

### 例子

- [**Scala**](http://spark.apache.org/docs/latest/sql-ref-functions-udf-scalar.html#tab_scala_0)
- [**Java**](http://spark.apache.org/docs/latest/sql-ref-functions-udf-scalar.html#tab_java_0)

```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.udf

val spark = SparkSession
  .builder()
  .appName("Spark SQL UDF scalar example")
  .getOrCreate()

// Define and register a zero-argument non-deterministic UDF
// UDF is deterministic by default, i.e. produces the same result for the same input.
val random = udf(() => Math.random())
spark.udf.register("random", random.asNondeterministic())
spark.sql("SELECT random()").show()
// +-------+
// |UDF()  |
// +-------+
// |xxxxxxx|
// +-------+

// Define and register a one-argument UDF
val plusOne = udf((x: Int) => x + 1)
spark.udf.register("plusOne", plusOne)
spark.sql("SELECT plusOne(5)").show()
// +------+
// |UDF(5)|
// +------+
// |     6|
// +------+

// Define a two-argument UDF and register it with Spark in one step
spark.udf.register("strLenScala", (_: String).length + (_: Int))
spark.sql("SELECT strLenScala('test', 1)").show()
// +--------------------+
// |strLenScala(test, 1)|
// +--------------------+
// |                   5|
// +--------------------+

// UDF in a WHERE clause
spark.udf.register("oneArgFilter", (n: Int) => { n > 5 })
spark.range(1, 10).createOrReplaceTempView("test")
spark.sql("SELECT * FROM test WHERE oneArgFilter(id)").show()
// +---+
// | id|
// +---+
// |  6|
// |  7|
// |  8|
// |  9|
// +---+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / UserDefinedScalar.scala”中找到完整的示例代码。

### 相关陈述

- [用户定义的汇总函数（UDAF）](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html)
- [与Hive UDF / UDAF / UDTF集成](http://spark.apache.org/docs/latest/sql-ref-functions-udf-hive.html)