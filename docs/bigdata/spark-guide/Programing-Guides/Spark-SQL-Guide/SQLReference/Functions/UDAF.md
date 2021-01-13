# 用户定义的汇总函数（UDAF）

### 描述

用户定义的聚合函数（UDAF）是用户可编程的例程，该例程一次作用于多行，并因此返回单个聚合值。本文档列出了创建和注册UDAF所需的类。它还包含一些示例，这些示例演示了如何在Scala中定义和注册UDAF，以及如何在Spark SQL中调用它们。

### 聚合器[-IN，BUF，OUT]

用户定义的聚合的基类，可以在数据集操作中使用该基类来获取组中的所有元素并将它们减少为单个值。

***IN-\***聚合的输入类型。

***BUF-\***减少的中间值的类型。

***OUT-\***最终输出结果的类型。

- **bufferEncoder：编码器[BUF]**

  指定中间值类型的编码器。

- **完成（减少：BUF）：OUT**

  转换归约的输出。

- **merge（b1：BUF，b2：BUF）：BUF**

  合并两个中间值。

- **outputEncoder：编码器[OUT]**

  指定最终输出值类型的编码器。

- **reduce（b：BUF，a：IN）：BUF**

  将输入值汇总`a`为当前中间值。为了提高性能，该函数可以修改`b`并返回它，而不是为构建新对象`b`。

- **零：BUF**

  此聚合的中间结果的初始值。

### 例子

#### 类型安全的用户定义的聚合函数

用户定义的强类型数据集的[聚合](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/expressions/Aggregator.html)围绕[Aggregator](http://spark.apache.org/docs/latest/api/scala/org/apache/spark/sql/expressions/Aggregator.html)抽象类展开。例如，类型安全的用户定义平均值可以如下所示：

- [**斯卡拉**](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html#tab_scala_0)
- [**爪哇**](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html#tab_java_0)

```
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator

case class Employee(name: String, salary: Long)
case class Average(var sum: Long, var count: Long)

object MyAverage extends Aggregator[Employee, Average, Double] {
  // A zero value for this aggregation. Should satisfy the property that any b + zero = b
  def zero: Average = Average(0L, 0L)
  // Combine two values to produce a new value. For performance, the function may modify `buffer`
  // and return it instead of constructing a new object
  def reduce(buffer: Average, employee: Employee): Average = {
    buffer.sum += employee.salary
    buffer.count += 1
    buffer
  }
  // Merge two intermediate values
  def merge(b1: Average, b2: Average): Average = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }
  // Transform the output of the reduction
  def finish(reduction: Average): Double = reduction.sum.toDouble / reduction.count
  // Specifies the Encoder for the intermediate value type
  def bufferEncoder: Encoder[Average] = Encoders.product
  // Specifies the Encoder for the final output value type
  def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

val ds = spark.read.json("examples/src/main/resources/employees.json").as[Employee]
ds.show()
// +-------+------+
// |   name|salary|
// +-------+------+
// |Michael|  3000|
// |   Andy|  4500|
// | Justin|  3500|
// |  Berta|  4000|
// +-------+------+

// Convert the function to a `TypedColumn` and give it a name
val averageSalary = MyAverage.toColumn.name("average_salary")
val result = ds.select(averageSalary)
result.show()
// +--------------+
// |average_salary|
// +--------------+
// |        3750.0|
// +--------------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / UserDefinedTypedAggregation.scala”中找到完整的示例代码。

#### 未类型化的用户定义的聚合函数

如上所述，类型化聚合也可以注册为与数据帧一起使用的非类型化聚合UDF。例如，用户定义的无类型DataFrame的平均值如下所示：

- [**斯卡拉**](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html#tab_scala_1)
- [**爪哇**](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html#tab_java_1)
- [**的SQL**](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html#tab_SQL_1)

```scala
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.functions

case class Average(var sum: Long, var count: Long)

object MyAverage extends Aggregator[Long, Average, Double] {
  // A zero value for this aggregation. Should satisfy the property that any b + zero = b
  def zero: Average = Average(0L, 0L)
  // Combine two values to produce a new value. For performance, the function may modify `buffer`
  // and return it instead of constructing a new object
  def reduce(buffer: Average, data: Long): Average = {
    buffer.sum += data
    buffer.count += 1
    buffer
  }
  // Merge two intermediate values
  def merge(b1: Average, b2: Average): Average = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }
  // Transform the output of the reduction
  def finish(reduction: Average): Double = reduction.sum.toDouble / reduction.count
  // Specifies the Encoder for the intermediate value type
  def bufferEncoder: Encoder[Average] = Encoders.product
  // Specifies the Encoder for the final output value type
  def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

// Register the function to access it
spark.udf.register("myAverage", functions.udaf(MyAverage))

val df = spark.read.json("examples/src/main/resources/employees.json")
df.createOrReplaceTempView("employees")
df.show()
// +-------+------+
// |   name|salary|
// +-------+------+
// |Michael|  3000|
// |   Andy|  4500|
// | Justin|  3500|
// |  Berta|  4000|
// +-------+------+

val result = spark.sql("SELECT myAverage(salary) as average_salary FROM employees")
result.show()
// +--------------+
// |average_salary|
// +--------------+
// |        3750.0|
// +--------------+
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / UserDefinedUntypedAggregation.scala”中找到完整的示例代码。

### 相关陈述

- [标量用户定义函数（UDF）](http://spark.apache.org/docs/latest/sql-ref-functions-udf-scalar.html)
- [与Hive UDF / UDAF / UDTF集成](http://spark.apache.org/docs/latest/sql-ref-functions-udf-hive.html)