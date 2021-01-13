# 与Hive UDF / UDAF / UDTF集成

### 描述

Spark SQL支持Hive UDF，UDAF和UDTF的集成。与Spark UDF和UDAF相似，Hive UDF作为输入在单行上工作，并作为输出生成单行，而Hive UDAF在多行上工作并因此返回单个聚合行。此外，Hive还支持UDTF（用户定义的表格函数），它们作用于一行作为输入，并返回多行作为输出。要使用Hive UDF / UDAF / UTF，用户应在Spark中注册它们，然后在Spark SQL查询中使用它们。

### 例子

Hive有两个UDF接口：[UDF](https://github.com/apache/hive/blob/master/udf/src/java/org/apache/hadoop/hive/ql/exec/UDF.java)和[GenericUDF](https://github.com/apache/hive/blob/master/ql/src/java/org/apache/hadoop/hive/ql/udf/generic/GenericUDF.java)。下面的示例使用源自的[GenericUDFAb](https://github.com/apache/hive/blob/master/ql/src/java/org/apache/hadoop/hive/ql/udf/generic/GenericUDFAbs.java)`GenericUDF`。

```sql
-- Register `GenericUDFAbs` and use it in Spark SQL.
-- Note that, if you use your own programmed one, you need to add a JAR containing it
-- into a classpath,
-- e.g., ADD JAR yourHiveUDF.jar;
CREATE TEMPORARY FUNCTION testUDF AS 'org.apache.hadoop.hive.ql.udf.generic.GenericUDFAbs';

SELECT * FROM t;
+-----+
|value|
+-----+
| -1.0|
|  2.0|
| -3.0|
+-----+

SELECT testUDF(value) FROM t;
+--------------+
|testUDF(value)|
+--------------+
|           1.0|
|           2.0|
|           3.0|
+--------------+
```

下面使用一个例子[GenericUDTFExplode](https://github.com/apache/hive/blob/master/ql/src/java/org/apache/hadoop/hive/ql/udf/generic/GenericUDTFExplode.java)衍生自[GenericUDTF](https://github.com/apache/hive/blob/master/ql/src/java/org/apache/hadoop/hive/ql/udf/generic/GenericUDF.java)。

```sql
-- Register `GenericUDTFExplode` and use it in Spark SQL
CREATE TEMPORARY FUNCTION hiveUDTF
    AS 'org.apache.hadoop.hive.ql.udf.generic.GenericUDTFExplode';

SELECT * FROM t;
+------+
| value|
+------+
|[1, 2]|
|[3, 4]|
+------+

SELECT hiveUDTF(value) FROM t;
+---+
|col|
+---+
|  1|
|  2|
|  3|
|  4|
+---+
```

蜂巢有两个UDAF接口：[UDAF](https://github.com/apache/hive/blob/master/udf/src/java/org/apache/hadoop/hive/ql/exec/UDAF.java)和[GenericUDAFResolver](https://github.com/apache/hive/blob/master/ql/src/java/org/apache/hadoop/hive/ql/udf/generic/GenericUDAFResolver.java)。以下示例使用源自的[GenericUDAFSum](https://github.com/apache/hive/blob/master/ql/src/java/org/apache/hadoop/hive/ql/udf/generic/GenericUDAFSum.java)`GenericUDAFResolver`。

```sql
-- Register `GenericUDAFSum` and use it in Spark SQL
CREATE TEMPORARY FUNCTION hiveUDAF
    AS 'org.apache.hadoop.hive.ql.udf.generic.GenericUDAFSum';

SELECT * FROM t;
+---+-----+
|key|value|
+---+-----+
|  a|    1|
|  a|    2|
|  b|    3|
+---+-----+

SELECT key, hiveUDAF(value) FROM t GROUP BY key;
+---+---------------+
|key|hiveUDAF(value)|
+---+---------------+
|  b|              3|
|  a|              3|
+---+---------------+
```