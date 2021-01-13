# PySpark使用Apache Arrow的Pandas 使用指南

- PySpark中的Apache Arrow
  - [确保已安装PyArrow](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#ensure-pyarrow-installed)
- [启用与Pandas 之间的转换](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#enabling-for-conversion-tofrom-pandas)
- Pandas UDF（又名矢量化UDF）
  - [系列到系列](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#series-to-series)
  - [系列迭代器到系列迭代器](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#iterator-of-series-to-iterator-of-series)
  - [多个系列的迭代器到系列的迭代器](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#iterator-of-multiple-series-to-iterator-of-series)
  - [系列到标量](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#series-to-scalar)
- Pandas 函数API
  - [分组地图](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#grouped-map)
  - [地图](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#map)
  - [共同地图](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#co-grouped-map)
- 使用说明
  - [支持的SQL类型](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#supported-sql-types)
  - [设置箭头批处理大小](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#setting-arrow-batch-size)
  - [带时区语义的时间戳](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#timestamp-with-time-zone-semantics)
  - [推荐的Pandas和PyArrow版本](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#recommended-pandas-and-pyarrow-versions)
  - [PyArrow> = 0.15.0和Spark 2.3.x，2.4.x的兼容性设置](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#compatibility-setting-for-pyarrow--0150-and-spark-23x-24x)

## PySpark中的Apache Arrow

Apache Arrow是一种内存中的列式数据格式，在Spark中使用它来有效地在JVM和Python进程之间传输数据。目前，这对于使用Pandas / NumPy数据的Python用户最为有利。它的使用不是自动的，可能需要对配置或代码进行一些小的更改才能充分利用并确保兼容性。本指南将对如何在Spark中使用Arrow进行高层描述，并重点介绍在使用启用了Arrow的数据时的区别。

### 确保已安装PyArrow

要在PySpark中使用Apache Arrow， 应安装[推荐版本的PyArrow](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#recommended-pandas-and-pyarrow-versions)。如果使用pip安装PySpark，则可以使用命令引入PyArrow作为SQL模块的额外依赖项`pip install pyspark[sql]`。否则，您必须确保PyArrow已安装并在所有群集节点上可用。您可以从conda-forge频道使用pip或conda进行安装。有关详细信息，请参见PyArrow [安装](https://arrow.apache.org/docs/python/install.html)。

## 启用与Pandas 之间的转换

使用调用将Spark DataFrame转换为Pandas DataFrame时，`toPandas()`以及使用时从Pandas DataFrame创建Spark DataFrame时， Arrow可用作优化`createDataFrame(pandas_df)`。要在执行这些调用时使用Arrow，用户需要首先将Spark配置设置`spark.sql.execution.arrow.pyspark.enabled`为`true`。默认情况下禁用。

此外，`spark.sql.execution.arrow.pyspark.enabled`如果在Spark内的实际计算之前发生错误，启用的优化可能会自动回退到非箭头优化实现。可以通过控制`spark.sql.execution.arrow.pyspark.fallback.enabled`。

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_0)

```
import numpy as np
import pandas as pd

# Enable Arrow-based columnar data transfers
spark.conf.set("spark.sql.execution.arrow.pyspark.enabled", "true")

# Generate a Pandas DataFrame
pdf = pd.DataFrame(np.random.rand(100, 3))

# Create a Spark DataFrame from a Pandas DataFrame using Arrow
df = spark.createDataFrame(pdf)

# Convert the Spark DataFrame back to a Pandas DataFrame using Arrow
result_pdf = df.select("*").toPandas()
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

将上述优化与Arrow一起使用将产生与未启用Arrow时相同的结果。请注意，即使使用Arrow，也会`toPandas()`导致将DataFrame中的所有记录收集到驱动程序中，并且应该对数据的一小部分进行处理。当前尚不支持所有的Spark数据类型，如果列的类型不受支持，则会引发错误，请参阅[支持的SQL类型](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#supported-sql-types)。如果在期间发生错误`createDataFrame()`，Spark将回退以创建不带箭头的DataFrame。

## Pandas UDF（又名矢量化UDF）

Pandas UDF是用户定义的函数，由Spark使用Arrow来传输数据，并通过Pandas与数据一起使用来执行，从而可以进行矢量化操作。Pandas UDF是使用`pandas_udf`修饰符或包装功能定义的，不需要其他配置。Pandas UDF通常表现为常规的PySpark函数API。

在Spark 3.0之前，Pandas UDF以前是通过定义的`PandasUDFType`。在带有Python 3.6+的Spark 3.0中，您还可以使用[Python类型提示](https://www.python.org/dev/peps/pep-0484)。首选使用Python类型提示`PandasUDFType`，在将来的版本中将不推荐使用。

请注意，`pandas.Series`在所有情况下都应使用`pandas.DataFrame`类型提示，但当输入或输出列为of时，应为其输入或输出类型提示使用一种变体`StructType`。以下示例显示了一个Pandas UDF，它使用长列，字符串列和结构列，并输出一个结构列。它需要的功能，以指定的类型提示`pandas.Series`和`pandas.DataFrame`如下：



- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_1)

```
import pandas as pd

from pyspark.sql.functions import pandas_udf

@pandas_udf("col1 string, col2 long")
def func(s1: pd.Series, s2: pd.Series, s3: pd.DataFrame) -> pd.DataFrame:
    s3['col2'] = s1 + s2.str.len()
    return s3

# Create a Spark DataFrame that has three columns including a sturct column.
df = spark.createDataFrame(
    [[1, "a string", ("a nested string",)]],
    "long_col long, string_col string, struct_col struct<col1:string>")

df.printSchema()
# root
# |-- long_column: long (nullable = true)
# |-- string_column: string (nullable = true)
# |-- struct_column: struct (nullable = true)
# |    |-- col1: string (nullable = true)

df.select(func("long_col", "string_col", "struct_col")).printSchema()
# |-- func(long_col, string_col, struct_col): struct (nullable = true)
# |    |-- col1: string (nullable = true)
# |    |-- col2: long (nullable = true)
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。



在以下各节中，它描述了受支持的类型提示的组合。为了简单起见， `pandas.DataFrame`省略了variant。

### 系列到系列

类型提示可以表示为`pandas.Series`…-> `pandas.Series`。

通过`pandas_udf`与上面具有此类类型提示的函数一起使用，它将创建一个Pandas UDF，其中给定的函数将采用一个或多个`pandas.Series`并输出一个`pandas.Series`。函数的输出应始终与输入具有相同的长度。在内部，PySpark将列拆分成批处理并为每个批处理调用函数作为数据的子集，然后将结果串联在一起，从而执行Pandas UDF。

以下示例显示了如何创建此Pandas UDF来计算2列乘积。

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_2)

```
import pandas as pd

from pyspark.sql.functions import col, pandas_udf
from pyspark.sql.types import LongType

# Declare the function and create the UDF
def multiply_func(a: pd.Series, b: pd.Series) -> pd.Series:
    return a * b

multiply = pandas_udf(multiply_func, returnType=LongType())

# The function for a pandas_udf should be able to execute with local Pandas data
x = pd.Series([1, 2, 3])
print(multiply_func(x, x))
# 0    1
# 1    4
# 2    9
# dtype: int64

# Create a Spark DataFrame, 'spark' is an existing SparkSession
df = spark.createDataFrame(pd.DataFrame(x, columns=["x"]))

# Execute function as a Spark vectorized UDF
df.select(multiply(col("x"), col("x"))).show()
# +-------------------+
# |multiply_func(x, x)|
# +-------------------+
# |                  1|
# |                  4|
# |                  9|
# +-------------------+
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

有关详细用法，请参阅 [`pyspark.sql.functions.pandas_udf`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.functions.pandas_udf)

### 系列迭代器到系列迭代器

类型提示可以表示为`Iterator[pandas.Series]`-> `Iterator[pandas.Series]`。

通过`pandas_udf`与上面具有此类类型提示的函数配合使用，它将创建Pandas UDF，其中给定函数采用的迭代器`pandas.Series`并输出的迭代器`pandas.Series`。该函数的整个输出的长度应与整个输入的长度相同。因此，只要长度相同，它就可以从输入迭代器中预取数据。在这种情况下，调用Pandas UDF时，创建的Pandas UDF需要一个输入列。要使用多个输入列，需要不同的类型提示。请参阅多序列的迭代器到序列的迭代器。

当UDF执行需要初始化某些状态时，它也很有用，尽管在内部它与“系列到系列”的情况相同。下面的伪代码说明了该示例。

```
@pandas_udf("long")
def calculate(iterator: Iterator[pd.Series]) -> Iterator[pd.Series]:
    # Do some expensive initialization with a state
    state = very_expensive_initialization()
    for x in iterator:
        # Use that state for whole iterator.
        yield calculate_with_state(x, state)

df.select(calculate("value")).show()
```

以下示例显示了如何创建此Pandas UDF：

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_3)

```
from typing import Iterator

import pandas as pd

from pyspark.sql.functions import pandas_udf

pdf = pd.DataFrame([1, 2, 3], columns=["x"])
df = spark.createDataFrame(pdf)

# Declare the function and create the UDF
@pandas_udf("long")
def plus_one(iterator: Iterator[pd.Series]) -> Iterator[pd.Series]:
    for x in iterator:
        yield x + 1

df.select(plus_one("x")).show()
# +-----------+
# |plus_one(x)|
# +-----------+
# |          2|
# |          3|
# |          4|
# +-----------+
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

有关详细用法，请参阅 [`pyspark.sql.functions.pandas_udf`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.functions.pandas_udf)

### 多个系列的迭代器到系列的迭代器

类型提示可以表示为`Iterator[Tuple[pandas.Series, ...]]`-> `Iterator[pandas.Series]`。

通过`pandas_udf`与上面具有此类类型提示的函数配合使用，它将创建一个Pandas UDF，其中给定函数采用一个为multiple的元组`pandas.Series`的迭代器，并输出一个的迭代器`pandas.Series`。在这种情况下，当调用Pandas UDF时，创建的Pandas UDF需要多个输入列，其数量与元组中的序列数相同。否则，它具有与“系列迭代器”到“系列迭代器”案例相同的特性和限制。

以下示例显示了如何创建此Pandas UDF：

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_4)

```
from typing import Iterator, Tuple

import pandas as pd

from pyspark.sql.functions import pandas_udf

pdf = pd.DataFrame([1, 2, 3], columns=["x"])
df = spark.createDataFrame(pdf)

# Declare the function and create the UDF
@pandas_udf("long")
def multiply_two_cols(
        iterator: Iterator[Tuple[pd.Series, pd.Series]]) -> Iterator[pd.Series]:
    for a, b in iterator:
        yield a * b

df.select(multiply_two_cols("x", "x")).show()
# +-----------------------+
# |multiply_two_cols(x, x)|
# +-----------------------+
# |                      1|
# |                      4|
# |                      9|
# +-----------------------+
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

有关详细用法，请参阅 [`pyspark.sql.functions.pandas_udf`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.functions.pandas_udf)

### 系列到标量

类型提示可以表示为`pandas.Series`…-> `Any`。

通过`pandas_udf`与上面具有此类类型提示的函数配合使用，它会创建类似于PySpark的聚合函数的Pandas UDF。给定的函数采用`pandas.Series`并返回标量值。返回类型应该是原始数据类型，返回的标量可以是python原始类型（例如）`int`或`float`numpy数据类型（例如`numpy.int64`或）`numpy.float64`。 `Any`理想情况下应相应地为特定的标量类型。

该UDF也可以与`groupBy().agg()`和一起使用[`pyspark.sql.Window`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.Window)。它定义了从一个或多个`pandas.Series`到标量值的聚合，其中每个都`pandas.Series` 代表组或窗口中的一列。

请注意，这种类型的UDF不支持部分聚合，并且组或窗口的所有数据都将加载到内存中。此外，分组聚合Pandas UDF当前仅支持无边界窗口。下面的示例显示如何使用此类UDF通过group-by和window操作来计算均值：

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_5)

```
import pandas as pd

from pyspark.sql.functions import pandas_udf
from pyspark.sql import Window

df = spark.createDataFrame(
    [(1, 1.0), (1, 2.0), (2, 3.0), (2, 5.0), (2, 10.0)],
    ("id", "v"))

# Declare the function and create the UDF
@pandas_udf("double")
def mean_udf(v: pd.Series) -> float:
    return v.mean()

df.select(mean_udf(df['v'])).show()
# +-----------+
# |mean_udf(v)|
# +-----------+
# |        4.2|
# +-----------+

df.groupby("id").agg(mean_udf(df['v'])).show()
# +---+-----------+
# | id|mean_udf(v)|
# +---+-----------+
# |  1|        1.5|
# |  2|        6.0|
# +---+-----------+

w = Window \
    .partitionBy('id') \
    .rowsBetween(Window.unboundedPreceding, Window.unboundedFollowing)
df.withColumn('mean_v', mean_udf(df['v']).over(w)).show()
# +---+----+------+
# | id|   v|mean_v|
# +---+----+------+
# |  1| 1.0|   1.5|
# |  1| 2.0|   1.5|
# |  2| 3.0|   6.0|
# |  2| 5.0|   6.0|
# |  2|10.0|   6.0|
# +---+----+------+
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

有关详细用法，请参阅 [`pyspark.sql.functions.pandas_udf`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.functions.pandas_udf)

## Pandas 函数API

Pandas 函数API可以`DataFrame`通过使用Pandas 实例直接将Python本机函数应用于整个函数。在内部，它与Pandas UDF的工作方式相似，方法是使用Arrow传输数据，并使用Pandas处理数据，从而实现矢量化操作。但是，Pandas Function API在PySpark`DataFrame`而不是PySpark下的行为与常规API相同`Column`，并且Pandas Functions API中的Python类型提示是可选的，尽管将来可能会需要它们，但暂时不会影响它在内部的工作方式。

从Spark 3.0起，分组地图Pandas UDF现在被归类为单独的Pandas 功能API `DataFrame.groupby().applyInPandas()`。它仍然有可能与使用它`PandasUDFType` ，并`DataFrame.groupby().apply()`为它; 但是，最好`DataFrame.groupby().applyInPandas()`直接使用 。使用`PandasUDFType`将在未来被废弃。

### 分组地图

支持具有Pandas实例的分组地图操作，`DataFrame.groupby().applyInPandas()` 该操作要求Python函数接受a`pandas.DataFrame`并返回另一个`pandas.DataFrame`。它将每个组映射到`pandas.DataFrame`Python函数中的每个组。

该API实现了“ split-apply-combine”模式，该模式包括三个步骤：

- 使用将数据分成几组`DataFrame.groupBy`。
- 在每个组上应用功能。函数的输入和输出均为`pandas.DataFrame`。输入数据包含每个组的所有行和列。
- 将结果合并到新的PySpark中`DataFrame`。

要使用`groupBy().applyInPandas()`，用户需要定义以下内容：

- 一个Python函数，用于定义每个组的计算。
- 甲`StructType`对象或定义输出PySpark的模式的字符串`DataFrame`。

`pandas.DataFrame`如果指定为字符串，则返回的列标签必须与定义的输出模式中的字段名称匹配，或者如果不是字符串，则必须按位置匹配字段数据类型，例如整数索引。有关 在构造时如何标记列的信息，请参见[pandas.DataFrame](https://pandas.pydata.org/pandas-docs/stable/generated/pandas.DataFrame.html#pandas.DataFrame)`pandas.DataFrame`。

注意，在应用该功能之前，组的所有数据将被加载到内存中。这可能会导致内存不足异常，尤其是在组大小偏斜的情况下。[maxRecordsPerBatch](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#setting-arrow-batch-size)的配置 不适用于组，并且由用户决定是否将分组的数据放入可用内存中。

下面的示例说明如何使用`groupby().applyInPandas()`从组中的每个值减去平均值。

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_6)

```
df = spark.createDataFrame(
    [(1, 1.0), (1, 2.0), (2, 3.0), (2, 5.0), (2, 10.0)],
    ("id", "v"))

def subtract_mean(pdf):
    # pdf is a pandas.DataFrame
    v = pdf.v
    return pdf.assign(v=v - v.mean())

df.groupby("id").applyInPandas(subtract_mean, schema="id long, v double").show()
# +---+----+
# | id|   v|
# +---+----+
# |  1|-0.5|
# |  1| 0.5|
# |  2|-3.0|
# |  2|-1.0|
# |  2| 4.0|
# +---+----+
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

有关详细用法，请参阅[`pyspark.sql.GroupedData.applyInPandas`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.GroupedData.applyInPandas)。

### 地图

支持使用Pandas实例进行Map操作，`DataFrame.mapInPandas()`该操作将`pandas.DataFrame`s的迭代器映射到s的另一个迭代器，`pandas.DataFrame`该迭代器表示当前PySpark`DataFrame`并将结果作为PySpark返回`DataFrame`。该函数接受并输出的迭代器`pandas.DataFrame`。与某些Pandas UDF相比，它可以返回任意长度的输出，尽管在内部它与Series to Series Pandas UDF类似。

以下示例显示如何使用`mapInPandas()`：

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_7)

```
df = spark.createDataFrame([(1, 21), (2, 30)], ("id", "age"))

def filter_func(iterator):
    for pdf in iterator:
        yield pdf[pdf.id == 1]

df.mapInPandas(filter_func, schema=df.schema).show()
# +---+---+
# | id|age|
# +---+---+
# |  1| 21|
# +---+---+
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

有关详细用法，请参阅[`pyspark.sql.DataFrame.mapsInPandas`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.DataFrame.mapInPandas)。

### 共同地图

支持与Pandas实例的联合分组地图操作，`DataFrame.groupby().cogroup().applyInPandas()`该操作允许`DataFrame`通过一个公用密钥将两个PySpark联合分组，然后将Python函数应用于每个联合分组。它包括以下步骤：

- 对数据进行混洗，以使共享密钥的每个数据帧的组共同分组。
- 将一个功能应用于每个共同组。该函数的输入为2 `pandas.DataFrame`（带有一个可选的表示键的元组）。该函数的输出为`pandas.DataFrame`。
- 将`pandas.DataFrame`所有组中的合并到新的PySpark中`DataFrame`。

要使用`groupBy().cogroup().applyInPandas()`，用户需要定义以下内容：

- 一个Python函数，用于定义每个协同组的计算。
- 甲`StructType`对象或定义输出PySpark的模式的字符串`DataFrame`。

`pandas.DataFrame`如果指定为字符串，则返回的列标签必须与定义的输出模式中的字段名称匹配，或者如果不是字符串，则必须按位置匹配字段数据类型，例如整数索引。有关 在构造时如何标记列的信息，请参见[pandas.DataFrame](https://pandas.pydata.org/pandas-docs/stable/generated/pandas.DataFrame.html#pandas.DataFrame)`pandas.DataFrame`。

请注意，在应用该功能之前，一个cogroup的所有数据将被加载到内存中。这可能会导致内存不足异常，尤其是在组大小偏斜的情况下。 不会应用[maxRecordsPerBatch](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#setting-arrow-batch-size)的配置，[并且](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#setting-arrow-batch-size)该配置[取决于](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#setting-arrow-batch-size)用户，以确保共同分组的数据将适合可用内存。

以下示例显示了如何用于`groupby().cogroup().applyInPandas()`在两个数据集之间执行asof连接。

- [**Python**](http://spark.apache.org/docs/latest/sql-pyspark-pandas-with-arrow.html#tab_python_8)

```
import pandas as pd

df1 = spark.createDataFrame(
    [(20000101, 1, 1.0), (20000101, 2, 2.0), (20000102, 1, 3.0), (20000102, 2, 4.0)],
    ("time", "id", "v1"))

df2 = spark.createDataFrame(
    [(20000101, 1, "x"), (20000101, 2, "y")],
    ("time", "id", "v2"))

def asof_join(l, r):
    return pd.merge_asof(l, r, on="time", by="id")

df1.groupby("id").cogroup(df2.groupby("id")).applyInPandas(
    asof_join, schema="time int, id int, v1 double, v2 string").show()
# +--------+---+---+---+
# |    time| id| v1| v2|
# +--------+---+---+---+
# |20000101|  1|1.0|  x|
# |20000102|  1|3.0|  x|
# |20000101|  2|2.0|  y|
# |20000102|  2|4.0|  y|
# +--------+---+---+---+
```

在Spark存储库中的“ examples / src / main / python / sql / arrow.py”中找到完整的示例代码。

有关详细用法，请参阅[`pyspark.sql.PandasCogroupedOps.applyInPandas()`](http://spark.apache.org/docs/latest/api/python/pyspark.sql.html#pyspark.sql.PandasCogroupedOps.applyInPandas)。

## 使用说明

### 支持的SQL类型

目前，所有Spark SQL数据类型是基于箭转换，除了支持`MapType`， `ArrayType`中`TimestampType`和嵌套`StructType`。

### 设置箭头批处理大小

Spark中的数据分区将转换为Arrow记录批，这可能会暂时导致JVM中的内存使用率很高。为了避免可能的内存不足异常，可以通过将conf“ spark.sql.execution.arrow.maxRecordsPerBatch”设置为整数来调整Arrow记录批的大小，该整数将确定每个批处理的最大行数。默认值为每批10,000条记录。如果列数很大，则应相应地调整该值。使用此限制，每个数据分区将被分为1个或多个记录批次以进行处理。

### 带时区语义的时间戳

Spark在内部将时间戳存储为UTC值，并且在没有指定时区的情况下传入的时间戳数据将以本地时间转换为具有微秒分辨率的UTC。当时间戳数据导出或在Spark中显示时，会话时区用于本地化时间戳值。会话时区使用配置“ spark.sql.session.timeZone”设置，如果未设置，则默认为JVM系统本地时区。Pandas使用`datetime64`具有纳秒分辨率的类型，`datetime64[ns]`每个列都具有可选的时区。

当时间戳数据从Spark传输到Pandas时，它将转换为纳秒，每列将转换为Spark会话时区，然后本地化到该时区，这会删除时区并将值显示为本地时间。这将在调用时`toPandas()`或`pandas_udf`使用时间戳列时发生。

当时间戳数据从Pandas传输到Spark时，它将转换为UTC微秒。在`createDataFrame`使用Pandas DataFrame进行调用或从返回时间戳时， 会发生这种情况`pandas_udf`。这些转换是自动完成的，以确保Spark将具有预期格式的数据，因此无需您自己进行任何这些转换。任何毫微秒的值都会被截断。

请注意，标准UDF（非Pandas）会将时间戳数据加载为Python日期时间对象，这与Pandas时间戳不同。建议在使用`pandas_udf`s中的时间戳时使用Pandas时间序列功能以获得最佳性能，有关详细信息，请参见 [此处](https://pandas.pydata.org/pandas-docs/stable/timeseries.html)。

### 推荐的Pandas和PyArrow版本

对于与pyspark.sql一起使用，支持的Pandas版本是0.24.2，而PyArrow是0.15.1。可以使用更高的版本，但是不能保证兼容性和数据正确性，并且应由用户验证。

### PyArrow> = 0.15.0和Spark 2.3.x，2.4.x的兼容性设置

从Arrow 0.15.0开始，二进制IPC格式的更改要求环境变量与Arrow <= 0.14.1的早期版本兼容。仅对于版本2.3.x和2.4.x且已将PyArrow手动升级到0.15.0的PySpark用户需要这样做。可以添加以下内容`conf/spark-env.sh`以使用旧版Arrow IPC格式：

```
ARROW_PRE_0_15_IPC_FORMAT=1
```

这将指示PyArrow> = 0.15.0将旧版IPC格式与Spark 2.3.x和2.4.x中的较旧Arrow Java一起使用。运行s或启用Arrow时， 未设置此环境变量将导致类似[SPARK-29367中](https://issues.apache.org/jira/browse/SPARK-29367)所述的错误。有关Arrow IPC更改的更多信息，请参见Arrow 0.15.0版本[博客](http://arrow.apache.org/blog/2019/10/06/0.15.0-release/#columnar-streaming-protocol-change-since-0140)。`pandas_udf``toPandas()`