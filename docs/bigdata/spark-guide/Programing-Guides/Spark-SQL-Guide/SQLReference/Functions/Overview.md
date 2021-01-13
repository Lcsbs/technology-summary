# 功能

Spark SQL提供了两个函数功能来满足广泛的用户需求：内置函数和用户定义函数（UDF）。内置函数是Spark SQL预定义的常用例程，可以在“[内置函数](http://spark.apache.org/docs/latest/api/sql/)API”文档中找到函数的完整列表。当系统的内置功能不足以执行所需的任务时，UDF允许用户定义自己的功能。

### 内建功能

Spark SQL具有一些类别的常用内置函数，用于聚合，数组/映射，日期/时间戳和JSON数据。本小节介绍了这些功能的用法和说明。

#### 标量函数

- [数组函数](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#array-functions)
- [地图功能](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#map-functions)
- [日期和时间戳功能](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#date-and-timestamp-functions)
- [JSON函数](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#json-functions)

#### 类聚集函数

- [汇总功能](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#aggregate-functions)
- [视窗功能](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html#window-functions)

### UDF（用户定义的功能）

用户定义函数（UDF）是Spark SQL的一项功能，当系统的内置功能不足以执行所需任务时，该功能允许用户定义自己的功能。要在Spark SQL中使用UDF，用户必须首先定义函数，然后在Spark中注册该函数，最后调用已注册的函数。用户定义的函数可以作用于单行或一次作用于多行。Spark SQL还支持UDF，UDAF和UDTF的现有Hive实现的集成。

- [标量用户定义函数（UDF）](http://spark.apache.org/docs/latest/sql-ref-functions-udf-scalar.html)
- [用户定义的聚合函数（UDAF）](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html)
- [与Hive UDF / UDAF / UDTF集成](http://spark.apache.org/docs/latest/sql-ref-functions-udf-hive.html)