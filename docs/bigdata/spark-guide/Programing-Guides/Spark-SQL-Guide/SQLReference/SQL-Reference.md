# SQL参考

Spark SQL是Apache Spark的用于处理结构化数据的模块。本指南是结构化查询语言（SQL）的参考，包括语法，语义，关键字和常见SQL使用示例。它包含有关以下主题的信息：

- [符合ANSI](http://spark.apache.org/docs/latest/sql-ref-ansi-compliance.html)
- [数据类型](http://spark.apache.org/docs/latest/sql-ref-datatypes.html)
- [日期时间模式](http://spark.apache.org/docs/latest/sql-ref-datetime-pattern.html)
- 功能
  - [内建功能](http://spark.apache.org/docs/latest/sql-ref-functions-builtin.html)
  - [标量用户定义函数（UDF）](http://spark.apache.org/docs/latest/sql-ref-functions-udf-scalar.html)
  - [用户定义的聚合函数（UDAF）](http://spark.apache.org/docs/latest/sql-ref-functions-udf-aggregate.html)
  - [与Hive UDF / UDAF / UDTF集成](http://spark.apache.org/docs/latest/sql-ref-functions-udf-hive.html)
- [身份标识](http://spark.apache.org/docs/latest/sql-ref-identifier.html)
- [文字](http://spark.apache.org/docs/latest/sql-ref-literals.html)
- [空语义](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html)
- SQL语法
  - [DDL陈述式](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl.html)
  - [DML语句](http://spark.apache.org/docs/latest/sql-ref-syntax-dml.html)
  - [数据检索语句](http://spark.apache.org/docs/latest/sql-ref-syntax-qry.html)
  - [辅助声明](http://spark.apache.org/docs/latest/sql-ref-syntax-aux.html)

# 符合ANSI

从Spark 3.0开始，Spark SQL引入了两个实验性选项来符合SQL标准：`spark.sql.ansi.enabled`和`spark.sql.storeAssignmentPolicy`（有关详细信息，请参见下表）。

当`spark.sql.ansi.enabled`设置`true`为时，Spark SQL在基本行为（例如算术运算，类型转换，SQL函数和SQL解析）方面遵循标准。此外，Spark SQL有一个独立的选项来控制在表中插入行时的隐式转换行为。铸造行为在标准中定义为商店分配规则。

当`spark.sql.storeAssignmentPolicy`设置`ANSI`为时，Spark SQL符合ANSI存储分配规则。这是一个单独的配置，因为其默认值为`ANSI`，而`spark.sql.ansi.enabled`默认情况下禁用该配置。

| 物业名称                          | 默认  | 含义                                                         | 自版本 |
| :-------------------------------- | :---- | :----------------------------------------------------------- | :----- |
| `spark.sql.ansi.enabled`          | false | （实验性）为true时，Spark尝试符合ANSI SQL规范： 1.如果整数/小数字段上的任何操作发生溢出，Spark都会引发运行时异常。 2. Spark将禁止使用ANSI SQL的保留关键字作为SQL解析器中的标识符。 | 3.0.0  |
| `spark.sql.storeAssignmentPolicy` | ANSI  | （实验性的）将值插入具有不同数据类型的列时，Spark将执行类型强制。目前，我们为类型强制规则支持3种策略：ANSI，传统和严格。使用ANSI策略，Spark根据ANSI SQL执行类型强制转换。实际上，其行为与PostgreSQL基本相同。它不允许某些不合理的类型转换，例如将字符串转换为int或将double转换为boolean。使用旧版策略，Spark允许类型强制，只要它是有效的Cast，就非常宽松。例如，允许将字符串转换为int或将double转换为boolean。这也是Spark 2.x中的唯一行为，并且与Hive兼容。使用严格的策略，Spark不允许类型强制转换中的任何可能的精度损失或数据截断，例如，不允许将double转换为int或将十进制转换为double。 | 3.0.0  |

以下各节介绍了启用ANSI模式时算术运算，类型转换和SQL解析中的行为更改。

### 算术运算

在Spark SQL中，默认情况下不会检查对数字类型（十进制除外）执行的算术运算是否有溢出。这意味着在某个操作导致溢出的情况下，结果与Java / Scala程序中的相应操作相同（例如，如果2个整数的总和大于可表示的最大值，则结果为负数）。另一方面，Spark SQL对于十进制溢出返回null。当`spark.sql.ansi.enabled`设置为`true`且数值和间隔算术运算中发生溢出时，它将在运行时引发算术异常。

```scala
-- `spark.sql.ansi.enabled=true`
SELECT 2147483647 + 1;
java.lang.ArithmeticException: integer overflow

-- `spark.sql.ansi.enabled=false`
SELECT 2147483647 + 1;
+----------------+
|(2147483647 + 1)|
+----------------+
|     -2147483648|
+----------------+
```

### 类型转换

Spark SQL具有三种类型转换：显式转换，类型强制转换和存储分配转换。当`spark.sql.ansi.enabled`设置`true`为时，按`CAST`语法进行的显式强制转换会为标准中定义的非法强制转换模式（例如，从字符串强制转换为整数）抛出运行时异常。另一方面，`INSERT INTO`当通过启用ANSI模式时，语法会引发分析异常`spark.sql.storeAssignmentPolicy=ANSI`。

当前，ANSI模式仅影响显式强制转换和赋值强制转换。在将来的版本中，类型强制的行为可能会与其他两个类型转换规则一起变化。

```scala
-- Examples of explicit casting

-- `spark.sql.ansi.enabled=true`
SELECT CAST('a' AS INT);
java.lang.NumberFormatException: invalid input syntax for type numeric: a

SELECT CAST(2147483648L AS INT);
java.lang.ArithmeticException: Casting 2147483648 to int causes overflow

-- `spark.sql.ansi.enabled=false` (This is a default behaviour)
SELECT CAST('a' AS INT);
+--------------+
|CAST(a AS INT)|
+--------------+
|          null|
+--------------+

SELECT CAST(2147483648L AS INT);
+-----------------------+
|CAST(2147483648 AS INT)|
+-----------------------+
|            -2147483648|
+-----------------------+

-- Examples of store assignment rules
CREATE TABLE t (v INT);

-- `spark.sql.storeAssignmentPolicy=ANSI`
INSERT INTO t VALUES ('1');
org.apache.spark.sql.AnalysisException: Cannot write incompatible data to table '`default`.`t`':
- Cannot safely cast 'v': string to int;

-- `spark.sql.storeAssignmentPolicy=LEGACY` (This is a legacy behaviour until Spark 2.x)
INSERT INTO t VALUES ('1');
SELECT * FROM t;
+---+
|  v|
+---+
|  1|
+---+
```

### SQL函数

在ANSI模式（`spark.sql.ansi.enabled=true`）下，某些SQL函数的行为可能有所不同。

- `size`：对于ANSI模式下的空输入，此函数返回空。

### SQL关键字

如果`spark.sql.ansi.enabled`为true，Spark SQL将使用ANSI模式解析器。在这种模式下，Spark SQL具有两种关键字：

- 保留关键字：保留的关键字，不能用作表，视图，列，函数，别名等的标识符。
- 非保留关键字：仅在特定上下文中具有特殊含义并且可以在其他上下文中用作标识符的关键字。例如，`EXPLAIN SELECT ...`是命令，但是EXPLAIN可以在其他地方用作标识符。

禁用ANSI模式时，Spark SQL具有两种关键字：

- 非保留关键字：与启用ANSI模式时的定义相同。
- 严格非保留关键字：非保留关键字的严格版本，不能用作表别名。

默认情况下`spark.sql.ansi.enabled`为false。

以下是Spark SQL中所有关键字的列表。

| Keyword           | Spark SQL ANSI Mode | Spark SQL Default Mode | SQL-2016      |
| :---------------- | :------------------ | :--------------------- | :------------ |
| ADD               | non-reserved        | non-reserved           | non-reserved  |
| AFTER             | non-reserved        | non-reserved           | non-reserved  |
| ALL               | reserved            | non-reserved           | reserved      |
| ALTER             | non-reserved        | non-reserved           | reserved      |
| ANALYZE           | non-reserved        | non-reserved           | non-reserved  |
| AND               | reserved            | non-reserved           | reserved      |
| ANTI              | non-reserved        | strict-non-reserved    | non-reserved  |
| ANY               | reserved            | non-reserved           | reserved      |
| ARCHIVE           | non-reserved        | non-reserved           | non-reserved  |
| ARRAY             | non-reserved        | non-reserved           | reserved      |
| AS                | reserved            | non-reserved           | reserved      |
| ASC               | non-reserved        | non-reserved           | non-reserved  |
| AT                | non-reserved        | non-reserved           | reserved      |
| AUTHORIZATION     | reserved            | non-reserved           | reserved      |
| BETWEEN           | non-reserved        | non-reserved           | reserved      |
| BOTH              | reserved            | non-reserved           | reserved      |
| BUCKET            | non-reserved        | non-reserved           | non-reserved  |
| BUCKETS           | non-reserved        | non-reserved           | non-reserved  |
| BY                | non-reserved        | non-reserved           | reserved      |
| CACHE             | non-reserved        | non-reserved           | non-reserved  |
| CASCADE           | non-reserved        | non-reserved           | non-reserved  |
| CASE              | reserved            | non-reserved           | reserved      |
| CAST              | reserved            | non-reserved           | reserved      |
| CHANGE            | non-reserved        | non-reserved           | non-reserved  |
| CHECK             | reserved            | non-reserved           | reserved      |
| CLEAR             | non-reserved        | non-reserved           | non-reserved  |
| CLUSTER           | non-reserved        | non-reserved           | non-reserved  |
| CLUSTERED         | non-reserved        | non-reserved           | non-reserved  |
| CODEGEN           | non-reserved        | non-reserved           | non-reserved  |
| COLLATE           | reserved            | non-reserved           | reserved      |
| COLLECTION        | non-reserved        | non-reserved           | non-reserved  |
| COLUMN            | reserved            | non-reserved           | reserved      |
| COLUMNS           | non-reserved        | non-reserved           | non-reserved  |
| COMMENT           | non-reserved        | non-reserved           | non-reserved  |
| COMMIT            | non-reserved        | non-reserved           | reserved      |
| COMPACT           | non-reserved        | non-reserved           | non-reserved  |
| COMPACTIONS       | non-reserved        | non-reserved           | non-reserved  |
| COMPUTE           | non-reserved        | non-reserved           | non-reserved  |
| CONCATENATE       | non-reserved        | non-reserved           | non-reserved  |
| CONSTRAINT        | reserved            | non-reserved           | reserved      |
| COST              | non-reserved        | non-reserved           | non-reserved  |
| CREATE            | reserved            | non-reserved           | reserved      |
| CROSS             | reserved            | strict-non-reserved    | reserved      |
| CUBE              | non-reserved        | non-reserved           | reserved      |
| CURRENT           | non-reserved        | non-reserved           | reserved      |
| CURRENT_DATE      | reserved            | non-reserved           | reserved      |
| CURRENT_TIME      | reserved            | non-reserved           | reserved      |
| CURRENT_TIMESTAMP | reserved            | non-reserved           | reserved      |
| CURRENT_USER      | reserved            | non-reserved           | reserved      |
| DATA              | non-reserved        | non-reserved           | non-reserved  |
| DATABASE          | non-reserved        | non-reserved           | non-reserved  |
| DATABASES         | non-reserved        | non-reserved           | non-reserved  |
| DAY               | reserved            | non-reserved           | reserved      |
| DBPROPERTIES      | non-reserved        | non-reserved           | non-reserved  |
| DEFINED           | non-reserved        | non-reserved           | non-reserved  |
| DELETE            | non-reserved        | non-reserved           | reserved      |
| DELIMITED         | non-reserved        | non-reserved           | non-reserved  |
| DESC              | non-reserved        | non-reserved           | non-reserved  |
| DESCRIBE          | non-reserved        | non-reserved           | reserved      |
| DFS               | non-reserved        | non-reserved           | non-reserved  |
| DIRECTORIES       | non-reserved        | non-reserved           | non-reserved  |
| DIRECTORY         | non-reserved        | non-reserved           | non-reserved  |
| DISTINCT          | reserved            | non-reserved           | reserved      |
| DISTRIBUTE        | non-reserved        | non-reserved           | non-reserved  |
| DIV               | non-reserved        | non-reserved           | not a keyword |
| DROP              | non-reserved        | non-reserved           | reserved      |
| ELSE              | reserved            | non-reserved           | reserved      |
| END               | reserved            | non-reserved           | reserved      |
| ESCAPE            | reserved            | non-reserved           | reserved      |
| ESCAPED           | non-reserved        | non-reserved           | non-reserved  |
| EXCEPT            | reserved            | strict-non-reserved    | reserved      |
| EXCHANGE          | non-reserved        | non-reserved           | non-reserved  |
| EXISTS            | non-reserved        | non-reserved           | reserved      |
| EXPLAIN           | non-reserved        | non-reserved           | non-reserved  |
| EXPORT            | non-reserved        | non-reserved           | non-reserved  |
| EXTENDED          | non-reserved        | non-reserved           | non-reserved  |
| EXTERNAL          | non-reserved        | non-reserved           | reserved      |
| EXTRACT           | non-reserved        | non-reserved           | reserved      |
| FALSE             | reserved            | non-reserved           | reserved      |
| FETCH             | reserved            | non-reserved           | reserved      |
| FIELDS            | non-reserved        | non-reserved           | non-reserved  |
| FILTER            | reserved            | non-reserved           | reserved      |
| FILEFORMAT        | non-reserved        | non-reserved           | non-reserved  |
| FIRST             | non-reserved        | non-reserved           | non-reserved  |
| FOLLOWING         | non-reserved        | non-reserved           | non-reserved  |
| FOR               | reserved            | non-reserved           | reserved      |
| FOREIGN           | reserved            | non-reserved           | reserved      |
| FORMAT            | non-reserved        | non-reserved           | non-reserved  |
| FORMATTED         | non-reserved        | non-reserved           | non-reserved  |
| FROM              | reserved            | non-reserved           | reserved      |
| FULL              | reserved            | strict-non-reserved    | reserved      |
| FUNCTION          | non-reserved        | non-reserved           | reserved      |
| FUNCTIONS         | non-reserved        | non-reserved           | non-reserved  |
| GLOBAL            | non-reserved        | non-reserved           | reserved      |
| GRANT             | reserved            | non-reserved           | reserved      |
| GROUP             | reserved            | non-reserved           | reserved      |
| GROUPING          | non-reserved        | non-reserved           | reserved      |
| HAVING            | reserved            | non-reserved           | reserved      |
| HOUR              | reserved            | non-reserved           | reserved      |
| IF                | non-reserved        | non-reserved           | not a keyword |
| IGNORE            | non-reserved        | non-reserved           | non-reserved  |
| IMPORT            | non-reserved        | non-reserved           | non-reserved  |
| IN                | reserved            | non-reserved           | reserved      |
| INDEX             | non-reserved        | non-reserved           | non-reserved  |
| INDEXES           | non-reserved        | non-reserved           | non-reserved  |
| INNER             | reserved            | strict-non-reserved    | reserved      |
| INPATH            | non-reserved        | non-reserved           | non-reserved  |
| INPUTFORMAT       | non-reserved        | non-reserved           | non-reserved  |
| INSERT            | non-reserved        | non-reserved           | reserved      |
| INTERSECT         | reserved            | strict-non-reserved    | reserved      |
| INTERVAL          | non-reserved        | non-reserved           | reserved      |
| INTO              | reserved            | non-reserved           | reserved      |
| IS                | reserved            | non-reserved           | reserved      |
| ITEMS             | non-reserved        | non-reserved           | non-reserved  |
| JOIN              | reserved            | strict-non-reserved    | reserved      |
| KEYS              | non-reserved        | non-reserved           | non-reserved  |
| LAST              | non-reserved        | non-reserved           | non-reserved  |
| LATERAL           | non-reserved        | non-reserved           | reserved      |
| LAZY              | non-reserved        | non-reserved           | non-reserved  |
| LEADING           | reserved            | non-reserved           | reserved      |
| LEFT              | reserved            | strict-non-reserved    | reserved      |
| LIKE              | non-reserved        | non-reserved           | reserved      |
| LIMIT             | non-reserved        | non-reserved           | non-reserved  |
| LINES             | non-reserved        | non-reserved           | non-reserved  |
| LIST              | non-reserved        | non-reserved           | non-reserved  |
| LOAD              | non-reserved        | non-reserved           | non-reserved  |
| LOCAL             | non-reserved        | non-reserved           | reserved      |
| LOCATION          | non-reserved        | non-reserved           | non-reserved  |
| LOCK              | non-reserved        | non-reserved           | non-reserved  |
| LOCKS             | non-reserved        | non-reserved           | non-reserved  |
| LOGICAL           | non-reserved        | non-reserved           | non-reserved  |
| MACRO             | non-reserved        | non-reserved           | non-reserved  |
| MAP               | non-reserved        | non-reserved           | non-reserved  |
| MATCHED           | non-reserved        | non-reserved           | non-reserved  |
| MERGE             | non-reserved        | non-reserved           | non-reserved  |
| MINUS             | non-reserved        | strict-non-reserved    | non-reserved  |
| MINUTE            | reserved            | non-reserved           | reserved      |
| MONTH             | reserved            | non-reserved           | reserved      |
| MSCK              | non-reserved        | non-reserved           | non-reserved  |
| NAMESPACE         | non-reserved        | non-reserved           | non-reserved  |
| NAMESPACES        | non-reserved        | non-reserved           | non-reserved  |
| NATURAL           | reserved            | strict-non-reserved    | reserved      |
| NO                | non-reserved        | non-reserved           | reserved      |
| NOT               | reserved            | non-reserved           | reserved      |
| NULL              | reserved            | non-reserved           | reserved      |
| NULLS             | non-reserved        | non-reserved           | non-reserved  |
| OF                | non-reserved        | non-reserved           | reserved      |
| ON                | reserved            | strict-non-reserved    | reserved      |
| ONLY              | reserved            | non-reserved           | reserved      |
| OPTION            | non-reserved        | non-reserved           | non-reserved  |
| OPTIONS           | non-reserved        | non-reserved           | non-reserved  |
| OR                | reserved            | non-reserved           | reserved      |
| ORDER             | reserved            | non-reserved           | reserved      |
| OUT               | non-reserved        | non-reserved           | reserved      |
| OUTER             | reserved            | non-reserved           | reserved      |
| OUTPUTFORMAT      | non-reserved        | non-reserved           | non-reserved  |
| OVER              | non-reserved        | non-reserved           | non-reserved  |
| OVERLAPS          | reserved            | non-reserved           | reserved      |
| OVERLAY           | non-reserved        | non-reserved           | non-reserved  |
| OVERWRITE         | non-reserved        | non-reserved           | non-reserved  |
| PARTITION         | non-reserved        | non-reserved           | reserved      |
| PARTITIONED       | non-reserved        | non-reserved           | non-reserved  |
| PARTITIONS        | non-reserved        | non-reserved           | non-reserved  |
| PERCENT           | non-reserved        | non-reserved           | non-reserved  |
| PIVOT             | non-reserved        | non-reserved           | non-reserved  |
| PLACING           | non-reserved        | non-reserved           | non-reserved  |
| POSITION          | non-reserved        | non-reserved           | reserved      |
| PRECEDING         | non-reserved        | non-reserved           | non-reserved  |
| PRIMARY           | reserved            | non-reserved           | reserved      |
| PRINCIPALS        | non-reserved        | non-reserved           | non-reserved  |
| PROPERTIES        | non-reserved        | non-reserved           | non-reserved  |
| PURGE             | non-reserved        | non-reserved           | non-reserved  |
| QUERY             | non-reserved        | non-reserved           | non-reserved  |
| RANGE             | non-reserved        | non-reserved           | reserved      |
| RECORDREADER      | non-reserved        | non-reserved           | non-reserved  |
| RECORDWRITER      | non-reserved        | non-reserved           | non-reserved  |
| RECOVER           | non-reserved        | non-reserved           | non-reserved  |
| REDUCE            | non-reserved        | non-reserved           | non-reserved  |
| REFERENCES        | reserved            | non-reserved           | reserved      |
| REFRESH           | non-reserved        | non-reserved           | non-reserved  |
| REGEXP            | non-reserved        | non-reserved           | not a keyword |
| RENAME            | non-reserved        | non-reserved           | non-reserved  |
| REPAIR            | non-reserved        | non-reserved           | non-reserved  |
| REPLACE           | non-reserved        | non-reserved           | non-reserved  |
| RESET             | non-reserved        | non-reserved           | non-reserved  |
| RESTRICT          | non-reserved        | non-reserved           | non-reserved  |
| REVOKE            | non-reserved        | non-reserved           | reserved      |
| RIGHT             | reserved            | strict-non-reserved    | reserved      |
| RLIKE             | non-reserved        | non-reserved           | non-reserved  |
| ROLE              | non-reserved        | non-reserved           | non-reserved  |
| ROLES             | non-reserved        | non-reserved           | non-reserved  |
| ROLLBACK          | non-reserved        | non-reserved           | reserved      |
| ROLLUP            | non-reserved        | non-reserved           | reserved      |
| ROW               | non-reserved        | non-reserved           | reserved      |
| ROWS              | non-reserved        | non-reserved           | reserved      |
| SCHEMA            | non-reserved        | non-reserved           | non-reserved  |
| SCHEMAS           | non-reserved        | non-reserved           | not a keyword |
| SECOND            | reserved            | non-reserved           | reserved      |
| SELECT            | reserved            | non-reserved           | reserved      |
| SEMI              | non-reserved        | strict-non-reserved    | non-reserved  |
| SEPARATED         | non-reserved        | non-reserved           | non-reserved  |
| SERDE             | non-reserved        | non-reserved           | non-reserved  |
| SERDEPROPERTIES   | non-reserved        | non-reserved           | non-reserved  |
| SESSION_USER      | reserved            | non-reserved           | reserved      |
| SET               | non-reserved        | non-reserved           | reserved      |
| SETS              | non-reserved        | non-reserved           | non-reserved  |
| SHOW              | non-reserved        | non-reserved           | non-reserved  |
| SKEWED            | non-reserved        | non-reserved           | non-reserved  |
| SOME              | reserved            | non-reserved           | reserved      |
| SORT              | non-reserved        | non-reserved           | non-reserved  |
| SORTED            | non-reserved        | non-reserved           | non-reserved  |
| START             | non-reserved        | non-reserved           | reserved      |
| STATISTICS        | non-reserved        | non-reserved           | non-reserved  |
| STORED            | non-reserved        | non-reserved           | non-reserved  |
| STRATIFY          | non-reserved        | non-reserved           | non-reserved  |
| STRUCT            | non-reserved        | non-reserved           | non-reserved  |
| SUBSTR            | non-reserved        | non-reserved           | non-reserved  |
| SUBSTRING         | non-reserved        | non-reserved           | non-reserved  |
| TABLE             | reserved            | non-reserved           | reserved      |
| TABLES            | non-reserved        | non-reserved           | non-reserved  |
| TABLESAMPLE       | non-reserved        | non-reserved           | reserved      |
| TBLPROPERTIES     | non-reserved        | non-reserved           | non-reserved  |
| TEMP              | non-reserved        | non-reserved           | not a keyword |
| TEMPORARY         | non-reserved        | non-reserved           | non-reserved  |
| TERMINATED        | non-reserved        | non-reserved           | non-reserved  |
| THEN              | reserved            | non-reserved           | reserved      |
| TO                | reserved            | non-reserved           | reserved      |
| TOUCH             | non-reserved        | non-reserved           | non-reserved  |
| TRAILING          | reserved            | non-reserved           | reserved      |
| TRANSACTION       | non-reserved        | non-reserved           | non-reserved  |
| TRANSACTIONS      | non-reserved        | non-reserved           | non-reserved  |
| TRANSFORM         | non-reserved        | non-reserved           | non-reserved  |
| TRIM              | non-reserved        | non-reserved           | non-reserved  |
| TRUE              | non-reserved        | non-reserved           | reserved      |
| TRUNCATE          | non-reserved        | non-reserved           | reserved      |
| TYPE              | non-reserved        | non-reserved           | non-reserved  |
| UNARCHIVE         | non-reserved        | non-reserved           | non-reserved  |
| UNBOUNDED         | non-reserved        | non-reserved           | non-reserved  |
| UNCACHE           | non-reserved        | non-reserved           | non-reserved  |
| UNION             | reserved            | strict-non-reserved    | reserved      |
| UNIQUE            | reserved            | non-reserved           | reserved      |
| UNKNOWN           | reserved            | non-reserved           | reserved      |
| UNLOCK            | non-reserved        | non-reserved           | non-reserved  |
| UNSET             | non-reserved        | non-reserved           | non-reserved  |
| UPDATE            | non-reserved        | non-reserved           | reserved      |
| USE               | non-reserved        | non-reserved           | non-reserved  |
| USER              | reserved            | non-reserved           | reserved      |
| USING             | reserved            | strict-non-reserved    | reserved      |
| VALUES            | non-reserved        | non-reserved           | reserved      |
| VIEW              | non-reserved        | non-reserved           | non-reserved  |
| VIEWS             | non-reserved        | non-reserved           | non-reserved  |
| WHEN              | reserved            | non-reserved           | reserved      |
| WHERE             | reserved            | non-reserved           | reserved      |
| WINDOW            | non-reserved        | non-reserved           | reserved      |
| WITH              | reserved            | non-reserved           | reserved      |
| YEAR              | reserved            | non-reserved           | reserved      |

# 数据类型

### 支持的数据类型

Spark SQL和DataFrames支持以下数据类型：

- 数值类型
  - `ByteType`：表示1个字节的有符号整数。数字范围是从`-128`到`127`。
  - `ShortType`：表示2个字节的有符号整数。数字范围是从`-32768`到`32767`。
  - `IntegerType`：表示4字节有符号整数。数字范围是从`-2147483648`到`2147483647`。
  - `LongType`：表示8字节有符号整数。数字范围是从`-9223372036854775808`到`9223372036854775807`。
  - `FloatType`：表示4字节单精度浮点数。
  - `DoubleType`：表示8字节的双精度浮点数。
  - `DecimalType`：表示任意精度的带符号十进制数字。内部支持`java.math.BigDecimal`。A`BigDecimal`由任意精度的整数无标度值和32位整数标度组成。
- 字符串类型
  - `StringType`：表示字符串值。
- 二进制类型
  - `BinaryType`：表示字节序列值。
- 布尔型
  - `BooleanType`：表示布尔值。
- 日期时间类型
  - `TimestampType`：代表具有年，月，日，小时，分钟和秒字段的值，并带有会话本地时区。时间戳记值表示绝对时间点。
  - `DateType`：代表包含年，月和日字段值的值，没有时区。
- 复杂类型
  - `ArrayType(elementType, containsNull)`：代表包含类型为的元素序列的值`elementType`。`containsNull`用于指示`ArrayType`值中的元素是否可以具有`null`值。
  - `MapType(keyType, valueType, valueContainsNull)`：表示包含一组键值对的值。键的数据类型用来描述，`keyType`而值的数据类型用来描述`valueType`。对于`MapType`值，键不允许具有`null`值。`valueContainsNull` 用于指示值的`MapType`值是否可以具有`null`值。`StructType(fields)`：表示具有由`StructField`s（`fields`）序列描述的结构的值。
  - `StructField(name, dataType, nullable)`：表示中的字段`StructType`。字段的名称用表示`name`。字段的数据类型由表示`dataType`。`nullable`用于指示这些字段的`null`值是否可以具有 值。

包中包含Spark SQL的所有数据类型`org.apache.spark.sql.types`。您可以通过以下方式访问它们

```scala
import org.apache.spark.sql.types._
```

在Spark存储库中的“ examples / src / main / scala / org / apache / spark / examples / sql / SparkSQLExample.scala”中找到完整的示例代码。

| 数据类型          | **Scala中的值类型**                                          | **用于访问或创建数据类型的API**                              |
| :---------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| **ByteType**      | Byte                                                         | ByteType                                                     |
| **ShortType**     | Short                                                        | ShortType                                                    |
| **IntegerType**   | Int                                                          | IntegerType                                                  |
| **LongType**      | Long                                                         | LongType                                                     |
| **FloatType**     | Float                                                        | FloatType                                                    |
| **DoubleType**    | Double                                                       | DoubleType                                                   |
| **DecimalType**   | java.math.BigDecimal                                         | DecimalType                                                  |
| **StringType**    | String                                                       | StringType                                                   |
| **BinaryType**    | Array[Byte]                                                  | BinaryType                                                   |
| **BooleanType**   | Boolean                                                      | BooleanType                                                  |
| **TimestampType** | java.sql.Timestamp                                           | TimestampType                                                |
| **DateType**      | java.sql.Date                                                | DateType                                                     |
| **ArrayType**     | scala.collection.Seq                                         | ArrayType(*elementType*, [*containsNull]*) **Note:** The default value of *containsNull* is true. |
| **MapType**       | scala.collection.Map                                         | MapType(*keyType*, *valueType*, [*valueContainsNull]*) **Note:** The default value of *valueContainsNull* is true. |
| **StructType**    | org.apache.spark.sql.Row                                     | StructType(*fields*) **Note:** *fields* is a Seq of StructFields. Also, two fields with the same name are not allowed. |
| **StructField**   | The value type in Scala of the data type of this field(For example, Int for a StructField with the data type IntegerType) | StructField(*name*, *dataType*, [*nullable*]) **Note:** The default value of *nullable* is true. |

### 浮点特殊值

Spark SQL以不区分大小写的方式支持几个特殊的浮点值：

- Inf / + Inf / Infinity / + Infinity：正无穷大
  - `FloatType`：相当于Scala `Float.PositiveInfinity`。
  - `DoubleType`：相当于Scala `Double.PositiveInfinity`。
- -Inf / -Infinity：负无穷大
  - `FloatType`：相当于Scala `Float.NegativeInfinity`。
  - `DoubleType`：相当于Scala `Double.NegativeInfinity`。
- NaN：不是数字
  - `FloatType`：相当于Scala `Float.NaN`。
  - `DoubleType`：相当于Scala `Double.NaN`。

#### 正/负无穷语义

对正和负无穷大有特殊处理。它们具有以下语义：

- 正无穷大乘以任何正值将返回正无穷大。
- 负无穷乘以任何正值将返回负无穷。
- 正无穷大乘以任何负值将返回负无穷大。
- 负无穷乘以任何负值将返回正无穷。
- 正/负无穷乘以0将返回NaN。
- 正/负无穷大等于自身。
- 在聚合中，所有正无穷大值都分组在一起。同样，所有负无穷大值都分组在一起。
- 正无穷大和负无穷大在联接键中被视为普通值。
- 正无穷大排序低于NaN且高于任何其他值。
- 负无穷比其他任何值都低。

#### NaN语义学

在处理非数字（NaN）`float`或`double`类型与标准浮点语义不完全匹配的类型时，要进行特殊处理。特别：

- NaN = NaN返回true。
- 在聚合中，所有NaN值都分组在一起。
- NaN在连接键中被视为普通值。
- NaN值按升序排在最后，大于任何其他数值。

#### 例子

```sql
SELECT double('infinity') AS col;
+--------+
|     col|
+--------+
|Infinity|
+--------+

SELECT float('-inf') AS col;
+---------+
|      col|
+---------+
|-Infinity|
+---------+

SELECT float('NaN') AS col;
+---+
|col|
+---+
|NaN|
+---+

SELECT double('infinity') * 0 AS col;
+---+
|col|
+---+
|NaN|
+---+

SELECT double('-infinity') * (-1234567) AS col;
+--------+
|     col|
+--------+
|Infinity|
+--------+

SELECT double('infinity') < double('NaN') AS col;
+----+
| col|
+----+
|true|
+----+

SELECT double('NaN') = double('NaN') AS col;
+----+
| col|
+----+
|true|
+----+

SELECT double('inf') = double('infinity') AS col;
+----+
| col|
+----+
|true|
+----+

CREATE TABLE test (c1 int, c2 double);
INSERT INTO test VALUES (1, double('infinity'));
INSERT INTO test VALUES (2, double('infinity'));
INSERT INTO test VALUES (3, double('inf'));
INSERT INTO test VALUES (4, double('-inf'));
INSERT INTO test VALUES (5, double('NaN'));
INSERT INTO test VALUES (6, double('NaN'));
INSERT INTO test VALUES (7, double('-infinity'));
SELECT COUNT(*), c2 FROM test GROUP BY c2;
+---------+---------+
| count(1)|       c2|
+---------+---------+
|        2|      NaN|
|        2|-Infinity|
|        3| Infinity|
+---------+---------+
```

# 用于格式化和解析的日期时间模式

Spark中有几种常见的日期时间用法场景：

- CSV / JSON数据源使用模式字符串来解析和格式化日期时间内容。
- 与转换`StringType`为`DateType`或转换为或转换的日期时间函数`TimestampType`。例如，`unix_timestamp`，`date_format`，`to_unix_timestamp`，`from_unixtime`，`to_date`，`to_timestamp`，`from_utc_timestamp`，`to_utc_timestamp`，等。

Spark在下表中使用模式字母进行日期和时间戳的解析和格式化：

| 符号    | 含义                         | 展示        | 例子                                           |
| :------ | :--------------------------- | :---------- | :--------------------------------------------- |
| **G**   | era                          | text        | AD; Anno Domini                                |
| **y**   | year                         | year        | 2020; 20                                       |
| **D**   | day-of-year                  | number(3)   | 189                                            |
| **M/L** | month-of-year                | month       | 7; 07; Jul; July                               |
| **d**   | day-of-month                 | number(3)   | 28                                             |
| **Q/q** | quarter-of-year              | number/text | 3; 03; Q3; 3rd quarter                         |
| **E**   | day-of-week                  | text        | Tue; Tuesday                                   |
| **F**   | aligned day of week in month | number(1)   | 3                                              |
| **a**   | am-pm-of-day                 | am-pm       | PM                                             |
| **h**   | clock-hour-of-am-pm (1-12)   | number(2)   | 12                                             |
| **K**   | hour-of-am-pm (0-11)         | number(2)   | 0                                              |
| **k**   | clock-hour-of-day (1-24)     | number(2)   | 0                                              |
| **H**   | hour-of-day (0-23)           | number(2)   | 0                                              |
| **m**   | minute-of-hour               | number(2)   | 30                                             |
| **s**   | second-of-minute             | number(2)   | 55                                             |
| **S**   | fraction-of-second           | fraction    | 978                                            |
| **V**   | time-zone ID                 | zone-id     | America/Los_Angeles; Z; -08:30                 |
| **z**   | time-zone name               | zone-name   | Pacific Standard Time; PST                     |
| **O**   | localized zone-offset        | offset-O    | GMT+8; GMT+08:00; UTC-08:00;                   |
| **X**   | zone-offset ‘Z’ for zero     | offset-X    | Z; -08; -0830; -08:30; -083015; -08:30:15;     |
| **x**   | zone-offset                  | offset-x    | +0000; -08; -0830; -08:30; -083015; -08:30:15; |
| **Z**   | zone-offset                  | offset-Z    | +0000; -0800; -08:00;                          |
| **‘**   | escape for text              | delimiter   |                                                |
| **’‘**  | single quote                 | literal     | ’                                              |
| **[**   | optional section start       |             |                                                |
| **]**   | optional section end         |             |                                                |

图案字母的数量决定格式。

- 文字：文字样式取决于所使用的图案字母的数量。少于4个图案字母将使用短文本形式，通常是缩写形式，例如，星期几（星期一）可能会输出“ Mon”。恰好4个模式字母将使用全文格式，通常是完整说明，例如，星期几，星期一可能会输出“星期一”。5个或更多字母将失败。

- Number（n）：此处的n表示可以使用这种类型的日期时间模式的最大字母数。如果字母数为1，则使用最少的位数输出该值，并且不带填充。否则，将数字计数用作输出字段的宽度，并在必要时将值补零。

- 数字/文字：如果图案字母的数量为3个或更多，请使用上面的文字规则。否则，请使用上面的数字规则。

- 小数：使用一个或多个（最多9个）连续`'S'`字符（例如，`SSSSSS`）来解析和格式化秒的小数部分。对于解析，可接受的分数长度可以是[1，连续的'S'数]。对于格式化，分数长度将被填充为具有零的连续“ S”的数量。Spark支持毫秒级精度的日期时间，该日期时间最多具有6位有效数字，但可以解析出纳秒级，其中超过的部分将被截断。

- 年：字母的数量确定最小字段宽度，在该最小字段宽度以下使用填充。如果字母数为2，则使用简化的两位数形式。对于打印，这将输出最右边的两位数字。对于解析，这将使用2000的基值进行解析，从而得出2000到2099（含）之间的一年。如果字母数少于四个（但不是两个），则仅在负数年输出该符号。否则，如果在不存在“ G”的情况下超出焊盘宽度，则输出符号。7个或更多字母将失败。

- 月：遵循数字/文本规则。文本格式取决于字母-“ M”表示“标准”格式，“ L”表示“独立”格式。这两种形式仅在某些特定语言中有所不同。例如，在俄语中，“Июль”是7月的独立格式，而“Июля”是标准格式。以下是所有受支持的模式字母的示例：

  - `'M'`或`'L'`：一年中从1开始的月份数字。“ M”和“ L”之间没有区别。从1到9的月份打印无填充。

  ```shell
  spark-sql> select date_format(date '1970-01-01', "M");
  1
  spark-sql> select date_format(date '1970-12-01', "L");
  12
  ```

`'MM'`或`'LL'`：一年中从1开始的月份数字。为1-9月份添加零填充。

```
  spark-sql> select date_format(date '1970-1-01', "LL");
  01
  spark-sql> select date_format(date '1970-09-01', "MM");
  09
```

'MMM'：标准格式的简短文字表示。月份模式应该是日期模式的一部分，而不仅仅是独立的月份，除了语言区域外，在语言环境中，独立形式和独立形式之间没有区别，例如英语。

```
spark-sql> select date_format(date '1970-01-01', "d MMM");
1 Jan
spark-sql> select to_csv(named_struct('date', date '1970-01-01'), map('dateFormat', 'dd MMM', 'locale', 'RU'));
01 янв.
```

'LLL' ：独立形式的简短文字说明。它仅可用于格式化/解析月份，而没有任何其他日期字段。

```
spark-sql> select date_format(date '1970-01-01', "LLL");
Jan
spark-sql> select to_csv(named_struct('date', date '1970-01-01'), map('dateFormat', 'LLL', 'locale', 'RU'));
янв.
```

'MMMM' ：标准格式的完整文本月份表示形式。它用于将月份解析/格式化为日期/时间戳的一部分。

```
spark-sql> select date_format(date '1970-01-01', "d MMMM");
1 January
spark-sql> select to_csv(named_struct('date', date '1970-01-01'), map('dateFormat', 'd MMMM', 'locale', 'RU'));
1 января
```

'LLLL' ：完整的文本月份表示形式（独立形式）。该模式只能用于格式化/解析月份。

```
spark-sql> select date_format(date '1970-01-01', "LLLL");
January
spark-sql> select to_csv(named_struct('date', date '1970-01-01'), map('dateFormat', 'LLLL', 'locale', 'RU'));
январь
```

- am-pm：输出一天的am-pm。图案字母数必须为1。
- 时区ID（V）：这将显示时区ID。图案字母计数必须为2。
- 区域名称（z）：输出时区ID的显示文本名称。如果字母数为1、2或3，则输出简称。如果字母数为4，则输出全名。五个或更多字母将失败。
- 偏移X和x：这将根据图案字母的数量来格式化偏移。除非分钟非零，否则一个字母仅输出小时，例如“ +01”，在这种情况下，分钟也会输出，例如“ +0130”。两个字母输出小时和分钟，不带冒号，例如'+0130'。三个字母输出小时和分钟，并带有冒号，例如“ +01：30”。四个字母输出小时和分钟，可选秒，不带冒号，例如“ +013015”。五个字母输出小时和分钟，可选秒，并带有冒号，例如“ +01：30：15”。六个或更多字母将失败。当要输出的偏移量为零时，模式字母“ X”（大写）将输出“ Z”，而模式字母“ x”（小写）将输出“ +00”，“ + 0000”或“ +00” ：00'。
- 偏移量O：这会根据图案字母的数量来格式化本地化的偏移量。一个字母输出本地化偏移量的简称，即本地化偏移量文本，例如“ GMT”，其中小时不带前导零，可选的两位数分钟和秒（如果非零）和冒号，例如“ GMT + 8” '。四个字母输出完整的格式，即本地化的偏移文本，例如'GMT，带有2位数的小时和分钟字段，第二个字段（非零）（如果非零则可选）和冒号，例如'GMT + 08：00'。其他任何字母计数都会失败。
- 偏移Z：根据格式字母的数量设置偏移格式。一个，两个或三个字母输出小时和分钟，不带冒号，例如“ +0130”。当偏移量为零时，输出为“ +0000”。四个字母输出完整的本地化偏移量形式，相当于四个字母的Offset-O。如果偏移量为零，则输出将是相应的本地化偏移量文本。五个字母输出小时，分钟，如果非零，则可选秒，带冒号。如果偏移为零，则输出“ Z”。六个或更多字母将失败。
- 可选节的开始和结束：`[]`用于定义可选节，也可以嵌套。在格式化期间，即使在可选部分中，也会输出所有有效数据。在解析过程中，整个部分可能会从解析的字符串中丢失。可选节的开头`[`和结尾是使用`]`（或在模式的结尾）。
- “ E”，“ F”，“ q”和“ Q”的符号只能用于日期时间格式，例如`date_format`。不允许将它们用于日期时间解析，例如`to_timestamp`。

# 身份标识

### 描述

标识符是用于标识数据库对象（例如表，视图，架构，列等）的字符串。Spark SQL具有常规标识符和带分隔符的标识符，这些标识符包含在反引号内。常规标识符和定界标识符都不区分大小写。

### 句法

#### 常规标识符

```
{ letter | digit | '_' } [ , ... ]
```

**注意：**如果`spark.sql.ansi.enabled`设置为true，则ANSI SQL保留关键字不能用作标识符。有关更多详细信息，请参阅[ANSI合规性](http://spark.apache.org/docs/latest/sql-ref-ansi-compliance.html)。

#### 分隔标识符

```
`c [ ... ]`
```

### 参量

- **信件**

  AZ或az的任何字母。

- **数字**

  0到9之间的任何数字。

- **C**

  字符集中的任何字符。使用```转义特殊字符（例如，```）。

### 例子

```sql
-- This CREATE TABLE fails with ParseException because of the illegal identifier name a.b
CREATE TABLE test (a.b int);
org.apache.spark.sql.catalyst.parser.ParseException:
no viable alternative at input 'CREATE TABLE test (a.'(line 1, pos 20)

-- This CREATE TABLE works
CREATE TABLE test (`a.b` int);

-- This CREATE TABLE fails with ParseException because special character ` is not escaped
CREATE TABLE test1 (`a`b` int);
org.apache.spark.sql.catalyst.parser.ParseException:
no viable alternative at input 'CREATE TABLE test (`a`b`'(line 1, pos 23)

-- This CREATE TABLE works
CREATE TABLE test (`a``b` int);
```

# 文字

文字（也称为常量）表示固定的数据值。Spark SQL支持以下文字：

- [字符串字面量](http://spark.apache.org/docs/latest/sql-ref-literals.html#string-literal)
- [二进制文字](http://spark.apache.org/docs/latest/sql-ref-literals.html#binary-literal)
- [空字面量](http://spark.apache.org/docs/latest/sql-ref-literals.html#null-literal)
- [布尔文字](http://spark.apache.org/docs/latest/sql-ref-literals.html#boolean-literal)
- [数字文字](http://spark.apache.org/docs/latest/sql-ref-literals.html#numeric-literal)
- [日期时间文字](http://spark.apache.org/docs/latest/sql-ref-literals.html#datetime-literal)
- [间隔字面量](http://spark.apache.org/docs/latest/sql-ref-literals.html#interval-literal)

### 字符串字面量

字符串文字用于指定字符串值。

#### 句法

```
'char [ ... ]' | "char [ ... ]"
```

#### 参量

- **烧焦**

  字符集中的一个字符。使用`\`转义特殊字符（例如，`'`或`\`）。

#### 例子

```
SELECT 'Hello, World!' AS col;
+-------------+
|          col|
+-------------+
|Hello, World!|
+-------------+

SELECT "SPARK SQL" AS col;
+---------+
|      col|
+---------+
|Spark SQL|
+---------+

SELECT 'it\'s $10.' AS col;
+---------+
|      col|
+---------+
|It's $10.|
+---------+
```

### 二进制文字

二进制文字用于指定字节序列值。

#### 句法

```
X { 'num [ ... ]' | "num [ ... ]" }
```

#### 参量

- **数**

  从0到F的任何十六进制数。

#### 例子

```
SELECT X'123456' AS col;
+----------+
|       col|
+----------+
|[12 34 56]|
+----------+
```

### 空字面量

空文字用于指定空值。

#### 句法

```
NULL
```

#### 例子

```
SELECT NULL AS col;
+----+
| col|
+----+
|NULL|
+----+
```

### 布尔文字

布尔文字用于指定布尔值。

#### 句法

```
TRUE | FALSE
```

#### 例子

```
SELECT TRUE AS col;
+----+
| col|
+----+
|true|
+----+
```

### 数字文字

数字文字用于指定固定或浮点数。

#### 整体字面量

##### 句法

```
[ + | - ] digit [ ... ] [ L | S | Y ]
```

##### 参量

- **数字**

  0到9之间的任何数字。

- **大号**

  不区分大小写，表示`BIGINT`，它是一个8字节有符号整数。

- **小号**

  不区分大小写，表示`SMALLINT`，这是一个2字节有符号整数。

- **ÿ**

  不区分大小写，表示`TINYINT`，它是一个1字节的有符号整数。

- **默认（无后缀）**

  表示一个4字节有符号整数。

##### 例子

```
SELECT -2147483648 AS col;
+-----------+
|        col|
+-----------+
|-2147483648|
+-----------+

SELECT 9223372036854775807l AS col;
+-------------------+
|                col|
+-------------------+
|9223372036854775807|
+-------------------+

SELECT -32Y AS col;
+---+
|col|
+---+
|-32|
+---+

SELECT 482S AS col;
+---+
|col|
+---+
|482|
+---+
```

#### 分数文字

##### 句法

十进制文字：

```
decimal_digits { [ BD ] | [ exponent BD ] } | digit [ ... ] [ exponent ] BD
```

双重文字：

```
decimal_digits  { D | exponent [ D ] }  | digit [ ... ] { exponent [ D ] | [ exponent ] D }
```

而十进制数字定义为

```
[ + | - ] { digit [ ... ] . [ digit [ ... ] ] | . digit [ ... ] }
```

指数定义为

```
E [ + | - ] digit [ ... ]
```

##### 参量

- **数字**

  0到9之间的任何数字。

- **d**

  不区分大小写，表示`DOUBLE`，这是一个8字节的双精度浮点数。

- **蓝光**

  不区分大小写，表示`DECIMAL`，其位数为精度，小数点右边的位数为小数位数。

##### 例子

```
SELECT 12.578 AS col;
+------+
|   col|
+------+
|12.578|
+------+

SELECT -0.1234567 AS col;
+----------+
|       col|
+----------+
|-0.1234567|
+----------+

SELECT -.1234567 AS col;
+----------+
|       col|
+----------+
|-0.1234567|
+----------+

SELECT 123. AS col;
+---+
|col|
+---+
|123|
+---+

SELECT 123.BD AS col;
+---+
|col|
+---+
|123|
+---+

SELECT 5E2 AS col;
+-----+
|  col|
+-----+
|500.0|
+-----+

SELECT 5D AS col;
+---+
|col|
+---+
|5.0|
+---+

SELECT -5BD AS col;
+---+
|col|
+---+
| -5|
+---+

SELECT 12.578e-2d AS col;
+-------+
|    col|
+-------+
|0.12578|
+-------+

SELECT -.1234567E+2BD AS col;
+---------+
|      col|
+---------+
|-12.34567|
+---------+

SELECT +3.e+3 AS col;
+------+
|   col|
+------+
|3000.0|
+------+

SELECT -3.E-3D AS col;
+------+
|   col|
+------+
|-0.003|
+------+
```

### 日期时间文字

Datetime文字用于指定日期时间值。

#### 日期文字

##### 句法

```
DATE { 'yyyy' |
       'yyyy-[m]m' |
       'yyyy-[m]m-[d]d' |
       'yyyy-[m]m-[d]d[T]' }
```

**注意：**`01`如果未指定月或日，则默认为。

##### 例子

```
SELECT DATE '1997' AS col;
+----------+
|       col|
+----------+
|1997-01-01|
+----------+

SELECT DATE '1997-01' AS col;
+----------+
|       col|
+----------+
|1997-01-01|
+----------+

SELECT DATE '2011-11-11' AS col;
+----------+
|       col|
+----------+
|2011-11-11|
+----------+
```

#### 时间戳字面量

##### 句法

```
TIMESTAMP { 'yyyy' |
            'yyyy-[m]m' |
            'yyyy-[m]m-[d]d' |
            'yyyy-[m]m-[d]d ' |
            'yyyy-[m]m-[d]d[T][h]h[:]' |
            'yyyy-[m]m-[d]d[T][h]h:[m]m[:]' |
            'yyyy-[m]m-[d]d[T][h]h:[m]m:[s]s[.]' |
            'yyyy-[m]m-[d]d[T][h]h:[m]m:[s]s.[ms][ms][ms][us][us][us][zone_id]'}
```

**注意：**`00`如果未指定小时，分钟或秒，则默认为。 `zone_id`应该具有以下形式之一：

- Z-祖鲁时区UTC + 0
- `+|-[h]h:[m]m`
- 一个ID，其前缀为UTC +，UTC-，GMT +，GMT-，UT +或UT-，并带有以下格式的后缀：
  - `+|-h[h]`
  - `+|-hh[:]mm`
  - `+|-hh:mm:ss`
  - `+|-hhmmss`
- 格式的基于区域的区域ID `area/city`，例如`Europe/Paris`

**注意：**`spark.sql.session.timeZone`如果`zone_id`未指定，则默认为会话本地时区（通过设置）。

##### 例子

```
SELECT TIMESTAMP '1997-01-31 09:26:56.123' AS col;
+-----------------------+
|                    col|
+-----------------------+
|1997-01-31 09:26:56.123|
+-----------------------+

SELECT TIMESTAMP '1997-01-31 09:26:56.66666666UTC+08:00' AS col;
+--------------------------+
|                      col |
+--------------------------+
|1997-01-30 17:26:56.666666|
+--------------------------+

SELECT TIMESTAMP '1997-01' AS col;
+-------------------+
|                col|
+-------------------+
|1997-01-01 00:00:00|
+-------------------+
```

### 间隔字面量

间隔文字用于指定固定的时间段。

```
INTERVAL interval_value interval_unit [ interval_value interval_unit ... ] |
INTERVAL 'interval_value interval_unit [ interval_value interval_unit ... ]' |
INTERVAL interval_string_value interval_unit TO interval_unit
```

#### 参量

- **interval_value**

  **句法：**

  ```
  [ + | - ] number_value | '[ + | - ] number_value'
  ```

- **interval_string_value**

  年-月/日-时间间隔字符串。

- **interval_unit**

  **句法：**

  ```
  YEAR[S] | MONTH[S] | WEEK[S] | DAY[S] | HOUR[S] | MINUTE[S] | SECOND[S] |
  MILLISECOND[S] | MICROSECOND[S]
  ```

#### 例子

```sql
SELECT INTERVAL 3 YEAR AS col;
+-------+
|    col|
+-------+
|3 years|
+-------+

SELECT INTERVAL -2 HOUR '3' MINUTE AS col;
+--------------------+
|                 col|
+--------------------+
|-1 hours -57 minutes|
+--------------------+

SELECT INTERVAL '1 YEAR 2 DAYS 3 HOURS';
+----------------------+
|                   col|
+----------------------+
|1 years 2 days 3 hours|
+----------------------+

SELECT INTERVAL 1 YEARS 2 MONTH 3 WEEK 4 DAYS 5 HOUR 6 MINUTES 7 SECOND 8
    MILLISECOND 9 MICROSECONDS AS col;
+-----------------------------------------------------------+
|                                                        col|
+-----------------------------------------------------------+
|1 years 2 months 25 days 5 hours 6 minutes 7.008009 seconds|
+-----------------------------------------------------------+

SELECT INTERVAL '2-3' YEAR TO MONTH AS col;
+----------------+
|             col|
+----------------+
|2 years 3 months|
+----------------+

SELECT INTERVAL '20 15:40:32.99899999' DAY TO SECOND AS col;
+---------------------------------------------+
|                                          col|
+---------------------------------------------+
|20 days 15 hours 40 minutes 32.998999 seconds|
+---------------------------------------------+
```

# 空语义

### 描述

一个表由一组行组成，每行包含一组列。列与数据类型相关联，并代表实体的特定属性（例如，`age`是名为的实体的列`person`）。有时，特定于某行的列的值在该行存在时是未知的。在中`SQL`，这些值表示为`NULL`。本节详细介绍了`NULL`各种运算符，表达式和其他`SQL`构造中的值处理的语义。

1. [比较运算符中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#comp-operators)
2. [逻辑运算符中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#logical-operators)
3. 表达式中的空处理
   1. [空无效表达式中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#null-intolerant)
   2. [空处理可以处理空值操作数的表达式](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#can-process-null)
   3. [内置聚合表达式中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#built-in-aggregate)
4. [在WHERE，HAVING和JOIN条件下进行空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#condition-expressions)
5. [GROUP BY和DISTINCT中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#aggregate-operator)
6. [ORDER BY中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#order-by)
7. [UNION，INTERSECT，EXCEPT中的Null处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#set-operators)
8. [EXISTS和NOT EXISTS子查询中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#exists-not-exists)
9. [IN和NOT IN子查询中的空处理](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#in-not-in)

以下说明了名为的表的模式布局和数据`person`。数据`NULL`在`age`列中包含值，该表将在以下各节的各种示例中使用。
**表格：人**

| ID   | 名称     | 年龄 |
| :--- | :------- | :--- |
| 100  | 乔       | 30   |
| 200  | 结婚     | 空值 |
| 300  | 麦克风   | 18   |
| 400  | 弗雷德   | 50   |
| 500  | 阿尔伯特 | 空值 |
| 600  | 蜜雪儿   | 30   |
| 700  | 担       | 50   |

### 比较运算符 

Apache spark支持标准比较运算符，例如'>'，'> ='，'='，'<'和'<='。这些运算符的结果未知，或者`NULL`其中一个操作数或两个操作数均未知或`NULL`。为了比较`NULL`相等性的值，Spark提供了一个空安全的相等运算符（'<=>'），该运算符`False`在操作数之一为时`NULL`返回并返回'True `when both the operands are `NULL `. The following table illustrates the behaviour of comparison operators when one or both operands are `NULL`：

| 左操作数 | 右操作数 | >    | > =  | =    | <    | <=   | <=>  |
| :------- | :------- | :--- | :--- | :--- | :--- | :--- | :--- |
| 空值     | 任何值   | 空值 | 空值 | 空值 | 空值 | 空值 | 假   |
| 任何值   | 空值     | 空值 | 空值 | 空值 | 空值 | 空值 | 假   |
| 空值     | 空值     | 空值 | 空值 | 空值 | 空值 | 空值 | 真正 |

### 例子

```
-- Normal comparison operators return `NULL` when one of the operand is `NULL`.
SELECT 5 > null AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+

-- Normal comparison operators return `NULL` when both the operands are `NULL`.
SELECT null = null AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+

-- Null-safe equal operator return `False` when one of the operand is `NULL`
SELECT 5 <=> null AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|            false|
+-----------------+

-- Null-safe equal operator return `True` when one of the operand is `NULL`
SELECT NULL <=> NULL;
+-----------------+
|expression_output|
+-----------------+
|             true|
+-----------------+
```

### 逻辑运算符 

火花支持标准的逻辑运算符，例如`AND`，`OR`和`NOT`。这些运算符将`Boolean`表达式作为参数并返回一个`Boolean`值。

下表说明了一个或两个操作数均为时逻辑运算符的行为`NULL`。

| 左操作数 | 右操作数 | 要么 | 和   |
| :------- | :------- | :--- | :--- |
| 真正     | 空值     | 真正 | 空值 |
| 假       | 空值     | 空值 | 假   |
| 空值     | 真正     | 真正 | 空值 |
| 空值     | 假       | 空值 | 空值 |
| 空值     | 空值     | 空值 | 空值 |

| 操作数 | 不   |
| :----- | :--- |
| 空值   | 空值 |

### 例子

```
-- Normal comparison operators return `NULL` when one of the operands is `NULL`.
SELECT (true OR null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             true|
+-----------------+

-- Normal comparison operators return `NULL` when both the operands are `NULL`.
SELECT (null OR false) AS expression_output
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+

-- Null-safe equal operator returns `False` when one of the operands is `NULL`
SELECT NOT(null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+
```

### 表达方式 

比较运算符和逻辑运算符在Spark中被视为表达式。除了这两种表达式，Spark还支持其他形式的表达式，例如函数表达式，强制转换表达式等。Spark中的表达式可大致分为：

- 零容忍表达

- 可以处理

  ```
  NULL
  ```

  值操作数的 表达式

  - 这些表达式的结果取决于表达式本身。

#### 空容忍的表达式 

`NULL`当一个或多个表达式参数为`NULL`且大多数表达式属于此类别时，将返回不耐空表达式。

##### 例子

```
SELECT concat('John', null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+

SELECT positive(null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+

SELECT to_date(null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+
```

#### 可以处理空值操作数的表达式 

此类表达式旨在处理`NULL`值。表达式的结果取决于表达式本身。例如，函数表达式在空输入和非空输入上`isnull` 返回一个`true`，`false`其中函数在其操作数列表中`coalesce` 返回第一个非`NULL`值。但是，当其所有操作数均为时`coalesce`返回 。以下是此类别的表达式的不完整列表。`NULL``NULL`

- 合并
- 空IF
- 空值
- NVL
- NVL2
- 伊斯南
- 南极
- 一片空白
- ISNOTNULL
- ATLEASTNNONNULLS
- 在

##### 例子

```
SELECT isnull(null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             true|
+-----------------+

-- Returns the first occurrence of non `NULL` value.
SELECT coalesce(null, null, 3, null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|                3|
+-----------------+

-- Returns `NULL` as all its operands are `NULL`. 
SELECT coalesce(null, null, null, null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|             null|
+-----------------+

SELECT isnan(null) AS expression_output;
+-----------------+
|expression_output|
+-----------------+
|            false|
+-----------------+
```

#### 内置聚合表达式 

聚合函数通过处理一组输入行来计算单个结果。以下是`NULL`聚合函数如何处理值的规则。

- ```
  NULL
  ```

   所有聚合函数都会忽略这些值。

  - 唯一例外的是COUNT（*）函数。

- 

  ```
  NULL
  ```

  当所有输入值均为

  ```
  NULL
  ```

  或输入数据集为空时，某些聚合函数将返回。

  这些功能的列表是：

  - 最大
  - 最小
  - 和
  - AVG
  - 每一个
  - 任何
  - 一些

#### 例子

```
-- `count(*)` does not skip `NULL` values.
SELECT count(*) FROM person;
+--------+
|count(1)|
+--------+
|       7|
+--------+

-- `NULL` values in column `age` are skipped from processing.
SELECT count(age) FROM person;
+----------+
|count(age)|
+----------+
|         5|
+----------+

-- `count(*)` on an empty input set returns 0. This is unlike the other
-- aggregate functions, such as `max`, which return `NULL`.
SELECT count(*) FROM person where 1 = 0;
+--------+
|count(1)|
+--------+
|       0|
+--------+

-- `NULL` values are excluded from computation of maximum value.
SELECT max(age) FROM person;
+--------+
|max(age)|
+--------+
|      50|
+--------+

-- `max` returns `NULL` on an empty input set.
SELECT max(age) FROM person where 1 = 0;
+--------+
|max(age)|
+--------+
|    null|
+--------+
```

### WHERE，HAVING和JOIN子句中的条件表达式 

`WHERE`，`HAVING`运算符根据用户指定的条件过滤行。一个`JOIN`操作符用于行基于连接条件的两个表结合起来。对于所有这三个运算符，条件表达式都是布尔表达式，可以返回 `True, False or Unknown (NULL)`。如果条件的结果是，他们将“满意” `True`。

#### 例子

```
-- Persons whose age is unknown (`NULL`) are filtered out from the result set.
SELECT * FROM person WHERE age > 0;
+--------+---+
|    name|age|
+--------+---+
|Michelle| 30|
|    Fred| 50|
|    Mike| 18|
|     Dan| 50|
|     Joe| 30|
+--------+---+

-- `IS NULL` expression is used in disjunction to select the persons
-- with unknown (`NULL`) records.
SELECT * FROM person WHERE age > 0 OR age IS NULL;
+--------+----+
|    name| age|
+--------+----+
|  Albert|null|
|Michelle|  30|
|    Fred|  50|
|    Mike|  18|
|     Dan|  50|
|   Marry|null|
|     Joe|  30|
+--------+----+

-- Person with unknown(`NULL`) ages are skipped from processing.
SELECT * FROM person GROUP BY age HAVING max(age) > 18;
+---+--------+
|age|count(1)|
+---+--------+
| 50|       2|
| 30|       2|
+---+--------+

-- A self join case with a join condition `p1.age = p2.age AND p1.name = p2.name`.
-- The persons with unknown age (`NULL`) are filtered out by the join operator.
SELECT * FROM person p1, person p2
    WHERE p1.age = p2.age
    AND p1.name = p2.name;
+--------+---+--------+---+
|    name|age|    name|age|
+--------+---+--------+---+
|Michelle| 30|Michelle| 30|
|    Fred| 50|    Fred| 50|
|    Mike| 18|    Mike| 18|
|     Dan| 50|     Dan| 50|
|     Joe| 30|     Joe| 30|
+--------+---+--------+---+

-- The age column from both legs of join are compared using null-safe equal which
-- is why the persons with unknown age (`NULL`) are qualified by the join.
SELECT * FROM person p1, person p2
    WHERE p1.age <=> p2.age
    AND p1.name = p2.name;
+--------+----+--------+----+
|    name| age|    name| age|
+--------+----+--------+----+
|  Albert|null|  Albert|null|
|Michelle|  30|Michelle|  30|
|    Fred|  50|    Fred|  50|
|    Mike|  18|    Mike|  18|
|     Dan|  50|     Dan|  50|
|   Marry|null|   Marry|null|
|     Joe|  30|     Joe|  30|
+--------+----+--------+----+
```

### 合计运算符（GROUP BY，DISTINCT） 

如上一节[比较运算符所述](http://spark.apache.org/docs/latest/sql-ref-null-semantics.html#comparison-operators)，两个`NULL`值不相等。但是，出于分组和不同处理的目的，将具有的两个或多个值一起`NULL data`分组到同一存储桶中。此行为符合SQL标准和其他企业数据库管理系统。

#### 例子

```
-- `NULL` values are put in one bucket in `GROUP BY` processing.
SELECT age, count(*) FROM person GROUP BY age;
+----+--------+
| age|count(1)|
+----+--------+
|null|       2|
|  50|       2|
|  30|       2|
|  18|       1|
+----+--------+

-- All `NULL` ages are considered one distinct value in `DISTINCT` processing.
SELECT DISTINCT age FROM person;
+----+
| age|
+----+
|null|
|  50|
|  30|
|  18|
+----+
```

### 排序运算符（ORDER BY子句） 

Spark SQL在`ORDER BY`子句中支持空排序规范。Spark`ORDER BY`通过将所有`NULL`值放在第一个或最后一个（取决于空排序规范）来处理该子句。默认情况下，所有`NULL`值都放在第一位。

#### 例子

```
-- `NULL` values are shown at first and other values
-- are sorted in ascending way.
SELECT age, name FROM person ORDER BY age;
+----+--------+
| age|    name|
+----+--------+
|null|   Marry|
|null|  Albert|
|  18|    Mike|
|  30|Michelle|
|  30|     Joe|
|  50|    Fred|
|  50|     Dan|
+----+--------+

-- Column values other than `NULL` are sorted in ascending
-- way and `NULL` values are shown at the last.
SELECT age, name FROM person ORDER BY age NULLS LAST;
+----+--------+
| age|    name|
+----+--------+
|  18|    Mike|
|  30|Michelle|
|  30|     Joe|
|  50|     Dan|
|  50|    Fred|
|null|   Marry|
|null|  Albert|
+----+--------+

-- Columns other than `NULL` values are sorted in descending
-- and `NULL` values are shown at the last.
SELECT age, name FROM person ORDER BY age DESC NULLS LAST;
+----+--------+
| age|    name|
+----+--------+
|  50|    Fred|
|  50|     Dan|
|  30|Michelle|
|  30|     Joe|
|  18|    Mike|
|null|   Marry|
|null|  Albert|
+----+--------+
```

### 集合运算符（UNION，INTERSECT，EXCEPT） 

`NULL`在设置操作的上下文中，以null安全的方式比较值是否相等。这意味着在比较行时，`NULL`与常规`EqualTo`（`=`）运算符不同，两个值被视为相等。

#### 例子

```
CREATE VIEW unknown_age SELECT * FROM person WHERE age IS NULL;

-- Only common rows between two legs of `INTERSECT` are in the 
-- result set. The comparison between columns of the row are done
-- in a null-safe manner.
SELECT name, age FROM person
    INTERSECT
    SELECT name, age from unknown_age;
+------+----+
|  name| age|
+------+----+
|Albert|null|
| Marry|null|
+------+----+

-- `NULL` values from two legs of the `EXCEPT` are not in output. 
-- This basically shows that the comparison happens in a null-safe manner.
SELECT age, name FROM person
    EXCEPT
    SELECT age FROM unknown_age;
+---+--------+
|age|    name|
+---+--------+
| 30|     Joe|
| 50|    Fred|
| 30|Michelle|
| 18|    Mike|
| 50|     Dan|
+---+--------+

-- Performs `UNION` operation between two sets of data. 
-- The comparison between columns of the row ae done in
-- null-safe manner.
SELECT name, age FROM person
    UNION 
    SELECT name, age FROM unknown_age;
+--------+----+
|    name| age|
+--------+----+
|  Albert|null|
|     Joe|  30|
|Michelle|  30|
|   Marry|null|
|    Fred|  50|
|    Mike|  18|
|     Dan|  50|
+--------+----+
```

### EXISTS / NOT EXISTS子查询 

在Spark中，可以在WHERE子句中使用EXISTS和NOT EXISTS表达式。这些是布尔表达式，它们返回`TRUE`或 `FALSE`。换句话说，EXISTS是成员资格条件，并且`TRUE` 当它引用的子查询返回一个或多个行时返回。同样，NOT EXISTS是非成员条件，当子查询没有返回行或零行时，返回TRUE。

这两个表达式不受子查询结果中是否存在NULL的影响。它们通常更快，因为它们可以转换为半联接/反半联接，而无需特殊设置以了解无效。

#### 例子

```
-- Even if subquery produces rows with `NULL` values, the `EXISTS` expression
-- evaluates to `TRUE` as the subquery produces 1 row.
SELECT * FROM person WHERE EXISTS (SELECT null);
+--------+----+
|    name| age|
+--------+----+
|  Albert|null|
|Michelle|  30|
|    Fred|  50|
|    Mike|  18|
|     Dan|  50|
|   Marry|null|
|     Joe|  30|
+--------+----+

-- `NOT EXISTS` expression returns `FALSE`. It returns `TRUE` only when
-- subquery produces no rows. In this case, it returns 1 row.
SELECT * FROM person WHERE NOT EXISTS (SELECT null);
+----+---+
|name|age|
+----+---+
+----+---+

-- `NOT EXISTS` expression returns `TRUE`.
SELECT * FROM person WHERE NOT EXISTS (SELECT 1 WHERE 1 = 0);
+--------+----+
|    name| age|
+--------+----+
|  Albert|null|
|Michelle|  30|
|    Fred|  50|
|    Mike|  18|
|     Dan|  50|
|   Marry|null|
|     Joe|  30|
+--------+----+
```

### IN / NOT IN子查询 

火花，`IN`和`NOT IN`表达式提供了一个WHERE查询子句中。不同于`EXISTS`表达式，`IN`表达式可以返回一个`TRUE`， `FALSE`或`UNKNOWN (NULL)`值。从概念上讲，`IN`表达式在语义上等效于由析取运算符（`OR`）分隔的一组相等条件。例如，c1 IN（1、2、3）在语义上等效于`(C1 = 1 OR c1 = 2 OR c1 = 3)`。

就处理`NULL`值而言，可以从`NULL`比较运算符（`=`）和逻辑运算符（`OR`）中的值处理中推导出语义。总而言之，下面是计算`IN`表达式结果的规则。

- 在列表中找到有问题的非NULL值时，返回TRUE
- 如果在列表中未找到非NULL值且列表不包含NULL值，则返回FALSE
- 当值为`NULL`或在列表中未找到非NULL值且列表包含至少一个`NULL`值时，返回UNKNOWN

当列表包含时`NULL`，无论输入值如何，NOT IN总是返回UNKNOWN 。这是因为如果该值不在包含的列表中`NULL`，则IN返回UNKNOWN ，并且因为NOT UNKNOWN再次为UNKNOWN。

#### 例子

```sql
-- The subquery has only `NULL` value in its result set. Therefore,
-- the result of `IN` predicate is UNKNOWN.
SELECT * FROM person WHERE age IN (SELECT null);
+----+---+
|name|age|
+----+---+
+----+---+

-- The subquery has `NULL` value in the result set as well as a valid 
-- value `50`. Rows with age = 50 are returned. 
SELECT * FROM person
    WHERE age IN (SELECT age FROM VALUES (50), (null) sub(age));
+----+---+
|name|age|
+----+---+
|Fred| 50|
| Dan| 50|
+----+---+

-- Since subquery has `NULL` value in the result set, the `NOT IN`
-- predicate would return UNKNOWN. Hence, no rows are
-- qualified for this query.
SELECT * FROM person
    WHERE age NOT IN (SELECT age FROM VALUES (50), (null) sub(age));
+----+---+
|name|age|
+----+---+
+----+---+
```

# SQL语法

Spark SQL是Apache Spark的用于处理结构化数据的模块。“ SQL语法”部分详细描述了SQL语法以及适用的用法示例。本文档提供了数据定义和数据处理语句以及数据检索和辅助语句的列表。

### DDL陈述式

- [修改数据库](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-alter-database.html)
- [更改表](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-alter-table.html)
- [变更检视](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-alter-view.html)
- [创建数据库](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-create-database.html)
- [创建功能](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-create-function.html)
- [创建表](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-create-table.html)
- [创建视图](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-create-view.html)
- [删除数据库](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-drop-database.html)
- [下降功能](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-drop-function.html)
- [滴台](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-drop-table.html)
- [下拉视图](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-drop-view.html)
- [维修台](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-repair-table.html)
- [截断表](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-truncate-table.html)
- [使用数据库](http://spark.apache.org/docs/latest/sql-ref-syntax-ddl-usedb.html)

### DML语句

- [插入](http://spark.apache.org/docs/latest/sql-ref-syntax-dml-insert-into.html)
- [插入覆盖](http://spark.apache.org/docs/latest/sql-ref-syntax-dml-insert-overwrite-table.html)
- [插入覆盖目录](http://spark.apache.org/docs/latest/sql-ref-syntax-dml-insert-overwrite-directory.html)
- [使用Hive格式插入覆盖目录](http://spark.apache.org/docs/latest/sql-ref-syntax-dml-insert-overwrite-directory-hive.html)
- [加载](http://spark.apache.org/docs/latest/sql-ref-syntax-dml-load.html)

### 数据检索语句

- SELECT陈述式
  - [公用表表达式](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-cte.html)
  - [子句集](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-clusterby.html)
  - [按条款分配](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-distribute-by.html)
  - [按条款分组](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-groupby.html)
  - [有条款](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-having.html)
  - [提示](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-hints.html)
  - [内联表](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-inline-table.html)
  - [加入](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-join.html)
  - [像谓词](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-like.html)
  - [限制条款](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-limit.html)
  - [按条款订购](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-orderby.html)
  - [集合运算符](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-setops.html)
  - [按条款排序](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-sortby.html)
  - [表样本](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-sampling.html)
  - [表值函数](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-tvf.html)
  - [条款](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-where.html)
  - [视窗功能](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-window.html)
  - [案例条款](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-case.html)
  - [PIVOT条款](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-pivot.html)
  - [横向视图条款](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-select-lateral-view.html)
- [说明](http://spark.apache.org/docs/latest/sql-ref-syntax-qry-explain.html)

### 辅助声明

- [添加文件](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-resource-mgmt-add-file.html)
- [添加JAR](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-resource-mgmt-add-jar.html)
- [分析表](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-analyze-table.html)
- [缓存表](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-cache-cache-table.html)
- [清除缓存](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-cache-clear-cache.html)
- [描述数据库](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-describe-database.html)
- [描述功能](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-describe-function.html)
- [描述查询](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-describe-query.html)
- [描述表](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-describe-table.html)
- [清单文件](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-resource-mgmt-list-file.html)
- [列表罐](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-resource-mgmt-list-jar.html)
- [刷新](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-cache-refresh.html)
- [刷新表](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-cache-refresh-table.html)
- [重启](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-conf-mgmt-reset.html)
- [组](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-conf-mgmt-set.html)
- [显示栏](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-columns.html)
- [显示创建表](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-create-table.html)
- [显示数据库](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-databases.html)
- [显示功能](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-functions.html)
- [显示分区](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-partitions.html)
- [显示表已扩展](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-table.html)
- [展示桌](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-tables.html)
- [显示TBL属性](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-tblproperties.html)
- [显示视图](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-show-views.html)
- [拆表](http://spark.apache.org/docs/latest/sql-ref-syntax-aux-cache-uncache-table.html)