# 内置函数

### 聚合函数

| 功能                                                         | 描述                                                         |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| any（expr）                                                  | 如果`expr`的至少一个值为true，则返回true。                   |
| approx_count_distinct（expr [，relativeSD]）                 | 通过HyperLogLog ++返回估计的基数。“ relativeSD”定义了允许的最大相对标准偏差。 |
| approx_percentile(col, percentage [, accuracy])              | 以给定的percentage 返回数字列“ col”的近似百分位数。percentage 值必须在0.0到1.0之间。参数“ accuracy”（默认值：10000）是一个正数文字，它以存储为代价控制近似精度。较高的“准确性”值会产生更好的准确性，“ 1.0 /准确性”是近似值的相对误差。当“ percentage”是一个数组时，percentage数组的每个值都必须介于0.0和1.0之间。在这种情况下，以给定的percentage 数组返回“ col”列的近似percentage 数组。 |
| avg（expr）                                                  | 返回根据组值计算出的平均值。                                 |
| bit_or（expr）                                               | 返回所有非空输入值的按位或，如果没有，则返回null。           |
| bit_xor（expr）                                              | 返回所有非空输入值的按位XOR，如果没有，则返回null。          |
| bool_and（expr）                                             | 如果ʻexpr`的所有值均为true，则返回true。                     |
| bool_or（expr）                                              | 如果`expr`的至少一个值为true，则返回true。                   |
| collect_list（expr）                                         | 收集并返回非唯一元素的列表。                                 |
| collect_set（expr）                                          | 收集并返回一组唯一的元素。                                   |
| corr（expr1，expr2）                                         | 返回一组数字对之间的皮尔逊相关系数。                         |
| count（*）                                                   | 返回检索到的行总数，包括包含null的行。                       |
| count（expr [，expr ...]）                                   | 返回所提供的表达式都不为空的行数。                           |
| count（DISTINCT expr [，expr ...]）                          | 返回所提供的表达式唯一且不为空的行数。                       |
| count_if（expr）                                             | 返回表达式的“ TRUE”值的数量。                                |
| count_min_sketch（col，eps，置信度，种子）                   | 返回具有给定esp，置信度和种子的列的最小count草图。结果是一个字节数组，使用前可以将其反序列化为`CountMinSketch`。最小count草图是用于使用亚线性空间进行基数估计的概率数据结构。 |
| covar_pop（expr1，expr2）                                    | 返回一组数字对的总体协方差。                                 |
| covar_samp（expr1，expr2）                                   | 返回一组数字对的样本协方差。                                 |
| 每个（expr）                                                 | 如果ʻexpr`的所有值均为true，则返回true。                     |
| first（expr [，isIgnoreNull]）                               | 返回一组行的ʻexpr`的第一个值。如果ʻisIgnoreNull`为true，则仅返回非null值。 |
| first_value（expr [，isIgnoreNull]）                         | 返回一组行的ʻexpr`的第一个值。如果ʻisIgnoreNull`为true，则仅返回非null值。 |
| 峰度（expr）                                                 | 返回从组的值计算出的峰度值。                                 |
| last（expr [，isIgnoreNull]）                                | 返回一组行的ʻexpr`的最后一个值。如果ʻisIgnoreNull`为true，则仅返回非null值 |
| last_value（expr [，isIgnoreNull]）                          | 返回一组行的ʻexpr`的最后一个值。如果ʻisIgnoreNull`为true，则仅返回非null值 |
| max（expr）                                                  | 返回ʻexpr`的最大值。                                         |
| max_by（x，y）                                               | 返回与x最大值相关的x值。                                     |
| mean（expr）                                                 | 返回根据组值计算出的平均值。                                 |
| min（expr）                                                  | 返回ʻexpr`的最小值。                                         |
| min_by（x，y）                                               | 返回与x最小值相关的x值。                                     |
| percentile（col，percentage[，frequency]）                   | 以给定的percentage 返回数字列“ col”的确切百分位数值。percentage 值必须在0.0到1.0之间。frequency值应为正整数 |
| percentile（col，array（percentage1 [，percent2] ...）[，frequency]） | 以给定的percentage 返回数字列“ col”的确切百分位数值数组。percentage 数组的每个值都必须在0.0到1.0之间。frequency值应为正整数 |
| percentile_approx（col，percentage [，accuracy]）            | 以给定的percentage 返回数字列“ col”的近似百分位数。percentage 值必须在0.0到1.0之间。参数“ accuracy”（默认值：10000）是一个正数文字，它以存储为代价控制近似精度。较高的“准确性”值会产生更好的准确性，“ 1.0 /准确性”是近似值的相对误差。当“ percentage”是一个数组时，percentage数组的每个值都必须介于0.0和1.0之间。在这种情况下，以给定的percentage 数组返回“ col”列的近似percentage 数组。 |
| skewness（expr）                                             | 返回根据组值计算的偏度值。                                   |
| some（expr）                                                 | 如果`expr`的至少一个值为true，则返回true。                   |
| std（expr）                                                  | 返回根据组值计算的样本标准偏差。                             |
| stddev（expr）                                               | 返回根据组值计算的样本标准偏差。                             |
| stddev_pop（expr）                                           | 返回根据组值计算的总体标准差。                               |
| stddev_samp（expr）                                          | 返回根据组值计算的样本标准偏差。                             |
| sum（expr）                                                  | 返回根据组值计算得出的总和。                                 |
| var_pop（expr）                                              | 返回根据组的值计算的总体方差。                               |
| var_samp（expr）                                             | 返回根据组值计算的样本方差。                                 |
| variance（expr）                                             | 返回根据组值计算的样本方差。                                 |

#### 例子

```sql
-- any
SELECT any(col) FROM VALUES (true), (false), (false) AS tab(col);
+--------+
|any(col)|
+--------+
|    true|
+--------+

SELECT any(col) FROM VALUES (NULL), (true), (false) AS tab(col);
+--------+
|any(col)|
+--------+
|    true|
+--------+

SELECT any(col) FROM VALUES (false), (false), (NULL) AS tab(col);
+--------+
|any(col)|
+--------+
|   false|
+--------+

-- approx_count_distinct
SELECT approx_count_distinct(col1) FROM VALUES (1), (1), (2), (2), (3) tab(col1);
+---------------------------+
|approx_count_distinct(col1)|
+---------------------------+
|                          3|
+---------------------------+

-- approx_percentile
SELECT approx_percentile(10.0, array(0.5, 0.4, 0.1), 100);
+--------------------------------------------------+
|approx_percentile(10.0, array(0.5, 0.4, 0.1), 100)|
+--------------------------------------------------+
|                                [10.0, 10.0, 10.0]|
+--------------------------------------------------+

SELECT approx_percentile(10.0, 0.5, 100);
+-------------------------------------------------+
|approx_percentile(10.0, CAST(0.5 AS DOUBLE), 100)|
+-------------------------------------------------+
|                                             10.0|
+-------------------------------------------------+

-- avg
SELECT avg(col) FROM VALUES (1), (2), (3) AS tab(col);
+--------+
|avg(col)|
+--------+
|     2.0|
+--------+

SELECT avg(col) FROM VALUES (1), (2), (NULL) AS tab(col);
+--------+
|avg(col)|
+--------+
|     1.5|
+--------+

-- bit_or
SELECT bit_or(col) FROM VALUES (3), (5) AS tab(col);
+-----------+
|bit_or(col)|
+-----------+
|          7|
+-----------+

-- bit_xor
SELECT bit_xor(col) FROM VALUES (3), (5) AS tab(col);
+------------+
|bit_xor(col)|
+------------+
|           6|
+------------+

-- bool_and
SELECT bool_and(col) FROM VALUES (true), (true), (true) AS tab(col);
+-------------+
|bool_and(col)|
+-------------+
|         true|
+-------------+

SELECT bool_and(col) FROM VALUES (NULL), (true), (true) AS tab(col);
+-------------+
|bool_and(col)|
+-------------+
|         true|
+-------------+

SELECT bool_and(col) FROM VALUES (true), (false), (true) AS tab(col);
+-------------+
|bool_and(col)|
+-------------+
|        false|
+-------------+

-- bool_or
SELECT bool_or(col) FROM VALUES (true), (false), (false) AS tab(col);
+------------+
|bool_or(col)|
+------------+
|        true|
+------------+

SELECT bool_or(col) FROM VALUES (NULL), (true), (false) AS tab(col);
+------------+
|bool_or(col)|
+------------+
|        true|
+------------+

SELECT bool_or(col) FROM VALUES (false), (false), (NULL) AS tab(col);
+------------+
|bool_or(col)|
+------------+
|       false|
+------------+

-- collect_list
SELECT collect_list(col) FROM VALUES (1), (2), (1) AS tab(col);
+-----------------+
|collect_list(col)|
+-----------------+
|        [1, 2, 1]|
+-----------------+

-- collect_set
SELECT collect_set(col) FROM VALUES (1), (2), (1) AS tab(col);
+----------------+
|collect_set(col)|
+----------------+
|          [1, 2]|
+----------------+

-- corr
SELECT corr(c1, c2) FROM VALUES (3, 2), (3, 3), (6, 4) as tab(c1, c2);
+--------------------------------------------+
|corr(CAST(c1 AS DOUBLE), CAST(c2 AS DOUBLE))|
+--------------------------------------------+
|                          0.8660254037844387|
+--------------------------------------------+

-- count
SELECT count(*) FROM VALUES (NULL), (5), (5), (20) AS tab(col);
+--------+
|count(1)|
+--------+
|       4|
+--------+

SELECT count(col) FROM VALUES (NULL), (5), (5), (20) AS tab(col);
+----------+
|count(col)|
+----------+
|         3|
+----------+

SELECT count(DISTINCT col) FROM VALUES (NULL), (5), (5), (10) AS tab(col);
+-------------------+
|count(DISTINCT col)|
+-------------------+
|                  2|
+-------------------+

-- count_if
SELECT count_if(col % 2 = 0) FROM VALUES (NULL), (0), (1), (2), (3) AS tab(col);
+-------------------------+
|count_if(((col % 2) = 0))|
+-------------------------+
|                        2|
+-------------------------+

SELECT count_if(col IS NULL) FROM VALUES (NULL), (0), (1), (2), (3) AS tab(col);
+-----------------------+
|count_if((col IS NULL))|
+-----------------------+
|                      1|
+-----------------------+

-- covar_pop
SELECT covar_pop(c1, c2) FROM VALUES (1,1), (2,2), (3,3) AS tab(c1, c2);
+-------------------------------------------------+
|covar_pop(CAST(c1 AS DOUBLE), CAST(c2 AS DOUBLE))|
+-------------------------------------------------+
|                               0.6666666666666666|
+-------------------------------------------------+

-- covar_samp
SELECT covar_samp(c1, c2) FROM VALUES (1,1), (2,2), (3,3) AS tab(c1, c2);
+--------------------------------------------------+
|covar_samp(CAST(c1 AS DOUBLE), CAST(c2 AS DOUBLE))|
+--------------------------------------------------+
|                                               1.0|
+--------------------------------------------------+

-- every
SELECT every(col) FROM VALUES (true), (true), (true) AS tab(col);
+----------+
|every(col)|
+----------+
|      true|
+----------+

SELECT every(col) FROM VALUES (NULL), (true), (true) AS tab(col);
+----------+
|every(col)|
+----------+
|      true|
+----------+

SELECT every(col) FROM VALUES (true), (false), (true) AS tab(col);
+----------+
|every(col)|
+----------+
|     false|
+----------+

-- first
SELECT first(col) FROM VALUES (10), (5), (20) AS tab(col);
+----------+
|first(col)|
+----------+
|        10|
+----------+

SELECT first(col) FROM VALUES (NULL), (5), (20) AS tab(col);
+----------+
|first(col)|
+----------+
|      null|
+----------+

SELECT first(col, true) FROM VALUES (NULL), (5), (20) AS tab(col);
+----------+
|first(col)|
+----------+
|         5|
+----------+

-- first_value
SELECT first_value(col) FROM VALUES (10), (5), (20) AS tab(col);
+----------------+
|first_value(col)|
+----------------+
|              10|
+----------------+

SELECT first_value(col) FROM VALUES (NULL), (5), (20) AS tab(col);
+----------------+
|first_value(col)|
+----------------+
|            null|
+----------------+

SELECT first_value(col, true) FROM VALUES (NULL), (5), (20) AS tab(col);
+----------------+
|first_value(col)|
+----------------+
|               5|
+----------------+

-- kurtosis
SELECT kurtosis(col) FROM VALUES (-10), (-20), (100), (1000) AS tab(col);
+-----------------------------+
|kurtosis(CAST(col AS DOUBLE))|
+-----------------------------+
|          -0.7014368047529618|
+-----------------------------+

SELECT kurtosis(col) FROM VALUES (1), (10), (100), (10), (1) as tab(col);
+-----------------------------+
|kurtosis(CAST(col AS DOUBLE))|
+-----------------------------+
|          0.19432323191698986|
+-----------------------------+

-- last
SELECT last(col) FROM VALUES (10), (5), (20) AS tab(col);
+---------+
|last(col)|
+---------+
|       20|
+---------+

SELECT last(col) FROM VALUES (10), (5), (NULL) AS tab(col);
+---------+
|last(col)|
+---------+
|     null|
+---------+

SELECT last(col, true) FROM VALUES (10), (5), (NULL) AS tab(col);
+---------+
|last(col)|
+---------+
|        5|
+---------+

-- last_value
SELECT last_value(col) FROM VALUES (10), (5), (20) AS tab(col);
+---------------+
|last_value(col)|
+---------------+
|             20|
+---------------+

SELECT last_value(col) FROM VALUES (10), (5), (NULL) AS tab(col);
+---------------+
|last_value(col)|
+---------------+
|           null|
+---------------+

SELECT last_value(col, true) FROM VALUES (10), (5), (NULL) AS tab(col);
+---------------+
|last_value(col)|
+---------------+
|              5|
+---------------+

-- max
SELECT max(col) FROM VALUES (10), (50), (20) AS tab(col);
+--------+
|max(col)|
+--------+
|      50|
+--------+

-- max_by
SELECT max_by(x, y) FROM VALUES (('a', 10)), (('b', 50)), (('c', 20)) AS tab(x, y);
+------------+
|max_by(x, y)|
+------------+
|           b|
+------------+

-- mean
SELECT mean(col) FROM VALUES (1), (2), (3) AS tab(col);
+---------+
|mean(col)|
+---------+
|      2.0|
+---------+

SELECT mean(col) FROM VALUES (1), (2), (NULL) AS tab(col);
+---------+
|mean(col)|
+---------+
|      1.5|
+---------+

-- min
SELECT min(col) FROM VALUES (10), (-1), (20) AS tab(col);
+--------+
|min(col)|
+--------+
|      -1|
+--------+

-- min_by
SELECT min_by(x, y) FROM VALUES (('a', 10)), (('b', 50)), (('c', 20)) AS tab(x, y);
+------------+
|min_by(x, y)|
+------------+
|           a|
+------------+

-- percentile
SELECT percentile(col, 0.3) FROM VALUES (0), (10) AS tab(col);
+---------------------------------------+
|percentile(col, CAST(0.3 AS DOUBLE), 1)|
+---------------------------------------+
|                                    3.0|
+---------------------------------------+

SELECT percentile(col, array(0.25, 0.75)) FROM VALUES (0), (10) AS tab(col);
+-------------------------------------+
|percentile(col, array(0.25, 0.75), 1)|
+-------------------------------------+
|                           [2.5, 7.5]|
+-------------------------------------+

-- percentile_approx
SELECT percentile_approx(10.0, array(0.5, 0.4, 0.1), 100);
+--------------------------------------------------+
|percentile_approx(10.0, array(0.5, 0.4, 0.1), 100)|
+--------------------------------------------------+
|                                [10.0, 10.0, 10.0]|
+--------------------------------------------------+

SELECT percentile_approx(10.0, 0.5, 100);
+-------------------------------------------------+
|percentile_approx(10.0, CAST(0.5 AS DOUBLE), 100)|
+-------------------------------------------------+
|                                             10.0|
+-------------------------------------------------+

-- skewness
SELECT skewness(col) FROM VALUES (-10), (-20), (100), (1000) AS tab(col);
+-----------------------------+
|skewness(CAST(col AS DOUBLE))|
+-----------------------------+
|           1.1135657469022013|
+-----------------------------+

SELECT skewness(col) FROM VALUES (-1000), (-100), (10), (20) AS tab(col);
+-----------------------------+
|skewness(CAST(col AS DOUBLE))|
+-----------------------------+
|          -1.1135657469022011|
+-----------------------------+

-- some
SELECT some(col) FROM VALUES (true), (false), (false) AS tab(col);
+---------+
|some(col)|
+---------+
|     true|
+---------+

SELECT some(col) FROM VALUES (NULL), (true), (false) AS tab(col);
+---------+
|some(col)|
+---------+
|     true|
+---------+

SELECT some(col) FROM VALUES (false), (false), (NULL) AS tab(col);
+---------+
|some(col)|
+---------+
|    false|
+---------+

-- std
SELECT std(col) FROM VALUES (1), (2), (3) AS tab(col);
+------------------------+
|std(CAST(col AS DOUBLE))|
+------------------------+
|                     1.0|
+------------------------+

-- stddev
SELECT stddev(col) FROM VALUES (1), (2), (3) AS tab(col);
+---------------------------+
|stddev(CAST(col AS DOUBLE))|
+---------------------------+
|                        1.0|
+---------------------------+

-- stddev_pop
SELECT stddev_pop(col) FROM VALUES (1), (2), (3) AS tab(col);
+-------------------------------+
|stddev_pop(CAST(col AS DOUBLE))|
+-------------------------------+
|              0.816496580927726|
+-------------------------------+

-- stddev_samp
SELECT stddev_samp(col) FROM VALUES (1), (2), (3) AS tab(col);
+--------------------------------+
|stddev_samp(CAST(col AS DOUBLE))|
+--------------------------------+
|                             1.0|
+--------------------------------+

-- sum
SELECT sum(col) FROM VALUES (5), (10), (15) AS tab(col);
+--------+
|sum(col)|
+--------+
|      30|
+--------+

SELECT sum(col) FROM VALUES (NULL), (10), (15) AS tab(col);
+--------+
|sum(col)|
+--------+
|      25|
+--------+

SELECT sum(col) FROM VALUES (NULL), (NULL) AS tab(col);
+--------+
|sum(col)|
+--------+
|    null|
+--------+

-- var_pop
SELECT var_pop(col) FROM VALUES (1), (2), (3) AS tab(col);
+----------------------------+
|var_pop(CAST(col AS DOUBLE))|
+----------------------------+
|          0.6666666666666666|
+----------------------------+

-- var_samp
SELECT var_samp(col) FROM VALUES (1), (2), (3) AS tab(col);
+-----------------------------+
|var_samp(CAST(col AS DOUBLE))|
+-----------------------------+
|                          1.0|
+-----------------------------+

-- variance
SELECT variance(col) FROM VALUES (1), (2), (3) AS tab(col);
+-----------------------------+
|variance(CAST(col AS DOUBLE))|
+-----------------------------+
|                          1.0|
+-----------------------------+
```

### Windows函数

| 功能                             | 描述                                                         |
| :------------------------------- | :----------------------------------------------------------- |
| cume_dist（）                    | 计算一个值相对于分区中所有值的位置。                         |
| density_rank（）                 | 计算一组值中一个值的等级。结果是一个加上先前分配的等级值。与函数等级不同，density_rank不会在等级序列中产生间隙。 |
| lag（input [，offset [，默认]]） | 返回窗口中当前行之前的第“ offset”行的“ input”的值。“ offset”的默认值为1，而“ default”的默认值为null。如果第“ offset”行的“ input”的值为null，则返回null。如果没有这样的偏移行（例如，当偏移为1时，窗口的第一行没有任何前一行），则返回“默认”。 |
| 线索（输入[，偏移[，默认]]）     | 在窗口中当前行之后的第“ offset”行返回“ input”的值。“ offset”的默认值为1，而“ default”的默认值为null。如果第“ offset”行的“ input”的值为null，则返回null。如果没有这样的偏移行（例如，当偏移为1时，窗口的最后一行没有任何后续行），则返回“默认”。 |
| ntile（n）                       | 将每个窗口分区的行划分为“ n”个存储桶，范围从1到最大“ n”。    |
| percent_rank（）                 | 计算一组值中某个值的percentage 排名。                        |
| 秩（）                           | 计算一组值中一个值的等级。结果是分区顺序中当前行之前或等于当前行的行数加一。值将在序列中产生间隙。 |
| row_number（）                   | 根据窗口分区中各行的顺序，为每一行分配一个唯一的顺序号（从1开始）。 |

### Array函数

| 功能                                               | 描述                                                         |
| :------------------------------------------------- | :----------------------------------------------------------- |
| array_contains（数组，值）                         | 如果数组包含该值，则返回true。                               |
| array_distinct（array）                            | 从数组中删除重复的值。                                       |
| array_except（array1，array2）                     | 返回array1中的元素数组，但不返回array2中的元素数组，没有重复项。 |
| array_intersect（array1，array2）                  | 返回array1和array2相交处的元素数组，没有重复。               |
| array_join（array，delimiter [，nullReplacement]） | 使用定界符和可选字符串替换空值来连接给定数组的元素。如果没有为nullReplacement设置任何值，则将过滤任何空值。 |
| array_max（array）                                 | 返回数组中的最大值。跳过NULL元素。                           |
| array_min（array）                                 | 返回数组中的最小值。跳过NULL元素。                           |
| array_position（数组，元素）                       | 返回数组的第一个元素的（从1开始）索引。                      |
| array_remove（array，element）                     | 从数组中删除所有等于element的元素。                          |
| array_repeat（element，count）                     | 返回包含元素count时间的数组。                                |
| array_union（array1，array2）                      | 返回array1和array2的并集中的元素数组，没有重复。             |
| arrays_overlap（a1，a2）                           | 如果a1至少包含a2中也存在一个非null元素，则返回true。如果数组没有公共元素，并且它们都是非空的，并且它们两个都包含null元素，则返回null，否则返回false。 |
| arrays_zip（a1，a2，...）                          | 返回结构的合并数组，其中第N个结构包含输入数组的所有第N个值。 |
| concat（col1，col2，...，colN）                    | 返回col1，col2，...，colN的串联。                            |
| 展平（arrayOfArrays）                              | 将数组的数组转换为单个数组。                                 |
| 反向（数组）                                       | 返回一个反向字符串或具有反向元素顺序的数组。                 |
| 顺序（开始，停止，步进）                           | 从开始到停止（包括结束）生成元素数组，并逐步增加。返回的元素的类型与参数表达式的类型相同。支持的类型为：字节，短整数，长整数，日期，时间戳。开始和停止表达式必须解析为相同的类型。如果起始和终止表达式解析为“日期”或“时间戳”类型，则步骤表达式必须解析为“间隔”类型，否则解析为与起始和终止表达式相同的类型。 |
| 随机播放（数组）                                   | 返回给定数组的随机排列。                                     |
| 切片（x，开始，长度）                              | 以指定的长度从索引开头（数组索引从1开始或如果结尾为负，则从结尾开始）开始，对数组x进行子集设置。 |
| sort_array（array [，ascendingOrder]）             | 根据数组元素的自然顺序对输入数组进行升序或降序排序。空元素将以升序放置在返回数组的开头，或者以降序放置在返回数组的结尾。 |

#### 例子

```
-- array_contains
SELECT array_contains(array(1, 2, 3), 2);
+---------------------------------+
|array_contains(array(1, 2, 3), 2)|
+---------------------------------+
|                             true|
+---------------------------------+

-- array_distinct
SELECT array_distinct(array(1, 2, 3, null, 3));
+----------------------------------------------------+
|array_distinct(array(1, 2, 3, CAST(NULL AS INT), 3))|
+----------------------------------------------------+
|                                          [1, 2, 3,]|
+----------------------------------------------------+

-- array_except
SELECT array_except(array(1, 2, 3), array(1, 3, 5));
+--------------------------------------------+
|array_except(array(1, 2, 3), array(1, 3, 5))|
+--------------------------------------------+
|                                         [2]|
+--------------------------------------------+

-- array_intersect
SELECT array_intersect(array(1, 2, 3), array(1, 3, 5));
+-----------------------------------------------+
|array_intersect(array(1, 2, 3), array(1, 3, 5))|
+-----------------------------------------------+
|                                         [1, 3]|
+-----------------------------------------------+

-- array_join
SELECT array_join(array('hello', 'world'), ' ');
+----------------------------------+
|array_join(array(hello, world),  )|
+----------------------------------+
|                       hello world|
+----------------------------------+

SELECT array_join(array('hello', null ,'world'), ' ');
+--------------------------------------------------------+
|array_join(array(hello, CAST(NULL AS STRING), world),  )|
+--------------------------------------------------------+
|                                             hello world|
+--------------------------------------------------------+

SELECT array_join(array('hello', null ,'world'), ' ', ',');
+-----------------------------------------------------------+
|array_join(array(hello, CAST(NULL AS STRING), world),  , ,)|
+-----------------------------------------------------------+
|                                              hello , world|
+-----------------------------------------------------------+

-- array_max
SELECT array_max(array(1, 20, null, 3));
+---------------------------------------------+
|array_max(array(1, 20, CAST(NULL AS INT), 3))|
+---------------------------------------------+
|                                           20|
+---------------------------------------------+

-- array_min
SELECT array_min(array(1, 20, null, 3));
+---------------------------------------------+
|array_min(array(1, 20, CAST(NULL AS INT), 3))|
+---------------------------------------------+
|                                            1|
+---------------------------------------------+

-- array_position
SELECT array_position(array(3, 2, 1), 1);
+---------------------------------+
|array_position(array(3, 2, 1), 1)|
+---------------------------------+
|                                3|
+---------------------------------+

-- array_remove
SELECT array_remove(array(1, 2, 3, null, 3), 3);
+-----------------------------------------------------+
|array_remove(array(1, 2, 3, CAST(NULL AS INT), 3), 3)|
+-----------------------------------------------------+
|                                              [1, 2,]|
+-----------------------------------------------------+

-- array_repeat
SELECT array_repeat('123', 2);
+--------------------+
|array_repeat(123, 2)|
+--------------------+
|          [123, 123]|
+--------------------+

-- array_union
SELECT array_union(array(1, 2, 3), array(1, 3, 5));
+-------------------------------------------+
|array_union(array(1, 2, 3), array(1, 3, 5))|
+-------------------------------------------+
|                               [1, 2, 3, 5]|
+-------------------------------------------+

-- arrays_overlap
SELECT arrays_overlap(array(1, 2, 3), array(3, 4, 5));
+----------------------------------------------+
|arrays_overlap(array(1, 2, 3), array(3, 4, 5))|
+----------------------------------------------+
|                                          true|
+----------------------------------------------+

-- arrays_zip
SELECT arrays_zip(array(1, 2, 3), array(2, 3, 4));
+------------------------------------------+
|arrays_zip(array(1, 2, 3), array(2, 3, 4))|
+------------------------------------------+
|                      [[1, 2], [2, 3], ...|
+------------------------------------------+

SELECT arrays_zip(array(1, 2), array(2, 3), array(3, 4));
+-------------------------------------------------+
|arrays_zip(array(1, 2), array(2, 3), array(3, 4))|
+-------------------------------------------------+
|                             [[1, 2, 3], [2, 3...|
+-------------------------------------------------+

-- concat
SELECT concat('Spark', 'SQL');
+------------------+
|concat(Spark, SQL)|
+------------------+
|          SparkSQL|
+------------------+

SELECT concat(array(1, 2, 3), array(4, 5), array(6));
+---------------------------------------------+
|concat(array(1, 2, 3), array(4, 5), array(6))|
+---------------------------------------------+
|                           [1, 2, 3, 4, 5, 6]|
+---------------------------------------------+

-- flatten
SELECT flatten(array(array(1, 2), array(3, 4)));
+----------------------------------------+
|flatten(array(array(1, 2), array(3, 4)))|
+----------------------------------------+
|                            [1, 2, 3, 4]|
+----------------------------------------+

-- reverse
SELECT reverse('Spark SQL');
+------------------+
|reverse(Spark SQL)|
+------------------+
|         LQS krapS|
+------------------+

SELECT reverse(array(2, 1, 4, 3));
+--------------------------+
|reverse(array(2, 1, 4, 3))|
+--------------------------+
|              [3, 4, 1, 2]|
+--------------------------+

-- sequence
SELECT sequence(1, 5);
+---------------+
| sequence(1, 5)|
+---------------+
|[1, 2, 3, 4, 5]|
+---------------+

SELECT sequence(5, 1);
+---------------+
| sequence(5, 1)|
+---------------+
|[5, 4, 3, 2, 1]|
+---------------+

SELECT sequence(to_date('2018-01-01'), to_date('2018-03-01'), interval 1 month);
+---------------------------------------------------------------------------+
|sequence(to_date('2018-01-01'), to_date('2018-03-01'), INTERVAL '1 months')|
+---------------------------------------------------------------------------+
|                                                       [2018-01-01, 2018...|
+---------------------------------------------------------------------------+

-- shuffle
SELECT shuffle(array(1, 20, 3, 5));
+---------------------------+
|shuffle(array(1, 20, 3, 5))|
+---------------------------+
|              [5, 3, 20, 1]|
+---------------------------+

SELECT shuffle(array(1, 20, null, 3));
+-------------------------------------------+
|shuffle(array(1, 20, CAST(NULL AS INT), 3))|
+-------------------------------------------+
|                                [20, 1,, 3]|
+-------------------------------------------+

-- slice
SELECT slice(array(1, 2, 3, 4), 2, 2);
+------------------------------+
|slice(array(1, 2, 3, 4), 2, 2)|
+------------------------------+
|                        [2, 3]|
+------------------------------+

SELECT slice(array(1, 2, 3, 4), -2, 2);
+-------------------------------+
|slice(array(1, 2, 3, 4), -2, 2)|
+-------------------------------+
|                         [3, 4]|
+-------------------------------+

-- sort_array
SELECT sort_array(array('b', 'd', null, 'c', 'a'), true);
+---------------------------------------------------------+
|sort_array(array(b, d, CAST(NULL AS STRING), c, a), true)|
+---------------------------------------------------------+
|                                           [, a, b, c, d]|
+---------------------------------------------------------+
```

### map函数

| 功能                               | 描述                               |
| :--------------------------------- | :--------------------------------- |
| map_concat（地图，...）            | 返回所有给定地图的并集             |
| map_entries（地图）                | 返回给定映射中所有条目的无序数组。 |
| map_from_entries（arrayOfEntries） | 返回从给定的条目数组创建的映射。   |
| map_keys（地图）                   | 返回包含地图键的无序数组。         |
| map_values（地图）                 | 返回包含映射值的无序数组。         |

#### 例子

```
-- map_concat
SELECT map_concat(map(1, 'a', 2, 'b'), map(3, 'c'));
+--------------------------------------+
|map_concat(map(1, a, 2, b), map(3, c))|
+--------------------------------------+
|                  [1 -> a, 2 -> b, ...|
+--------------------------------------+

-- map_entries
SELECT map_entries(map(1, 'a', 2, 'b'));
+----------------------------+
|map_entries(map(1, a, 2, b))|
+----------------------------+
|            [[1, a], [2, b]]|
+----------------------------+

-- map_from_entries
SELECT map_from_entries(array(struct(1, 'a'), struct(2, 'b')));
+---------------------------------------------------+
|map_from_entries(array(struct(1, a), struct(2, b)))|
+---------------------------------------------------+
|                                   [1 -> a, 2 -> b]|
+---------------------------------------------------+

-- map_keys
SELECT map_keys(map(1, 'a', 2, 'b'));
+-------------------------+
|map_keys(map(1, a, 2, b))|
+-------------------------+
|                   [1, 2]|
+-------------------------+

-- map_values
SELECT map_values(map(1, 'a', 2, 'b'));
+---------------------------+
|map_values(map(1, a, 2, b))|
+---------------------------+
|                     [a, b]|
+---------------------------+
```

### 日期和时间戳函数

| 功能                                                  | 描述                                                         |
| :---------------------------------------------------- | :----------------------------------------------------------- |
| add_months（开始日期，num_months）                    | 返回“开始日期”之后为“ num_months”的日期。                    |
| 当前日期（）                                          | 返回查询评估开始时的当前日期。                               |
| 当前日期                                              | 返回查询评估开始时的当前日期。                               |
| current_timestamp（）                                 | 返回查询评估开始时的当前时间戳。                             |
| current_timestamp                                     | 返回查询评估开始时的当前时间戳。                             |
| date_add（开始日期，num_days）                        | 返回“开始日期”之后为“ num_days”的日期。                      |
| date_format（时间戳，fmt）                            | 将“时间戳”转换为日期格式“ fmt”所指定格式的字符串值。         |
| date_part（字段，源）                                 | 提取日期/时间戳记或间隔源的一部分。                          |
| date_sub（开始日期，num_days）                        | 返回起始日期之前的“ num_days”天。                            |
| date_trunc（fmt，ts）                                 | 返回时间戳“ ts”，该时间戳被截断为格式模型“ fmt”指定的单位。  |
| datediff（endDate，startDate）                        | 返回从startDate到endDate的天数。                             |
| 星期几（日期）                                        | 返回日期/时间戳的星期几（1 =星期日，2 =星期一，...，7 =星期六）。 |
| 每年的日期（日期）                                    | 返回日期/时间戳的年份。                                      |
| from_unixtime（unix_time，格式）                      | 以指定的格式返回“ unix_time”。                               |
| from_utc_timestamp（时间戳，时区）                    | 给定一个类似于'2017-07-14 02：40：00.0'的时间戳，将其解释为UTC时间，并将该时间呈现为给定时区中的时间戳。例如，“ GMT + 1”将产生“ 2017-07-14 03：40：00.0”。 |
| 小时（时间戳）                                        | 返回字符串/时间戳的小时部分。                                |
| last_day（日期）                                      | 返回日期所属的月份的最后一天。                               |
| make_date（年，月，日）                               | 根据年，月和日字段创建日期。                                 |
| make_timestamp（年，月，日，小时，分钟，秒[，时区]）  | 根据年，月，日，小时，分钟，秒和时区字段创建时间戳。         |
| 分钟（时间戳）                                        | 返回字符串/时间戳的分钟部分。                                |
| 月（日期）                                            | 返回日期/时间戳的月份部分。                                  |
| months_between（timestamp1，timestamp2 [，roundOff]） | 如果“ timestamp1”晚于“ timestamp2”，则结果为肯定。如果`timestamp1`和`timestamp2`分别在一个月的同一天，或两者都在一个月的最后一天，则一天中的时间将被忽略。否则，差异将基于每月31天计算，并四舍五入为8位数字，除非roundOff = false。 |
| next_day（开始日期，day_of_week）                     | 返回第一个日期，该日期晚于`start_date`并按指示命名。         |
| 现在（）                                              | 返回查询评估开始时的当前时间戳。                             |
| 季度（日期）                                          | 返回日期的一年的四分之一，范围为1到4。                       |
| 秒（时间戳）                                          | 返回字符串/时间戳的第二部分。                                |
| to_date（date_str [，fmt]）                           | 将`date_str`表达式和`fmt`表达式解析为一个日期。返回无效输入的null。默认情况下，如果省略`fmt`，它将遵循转换规则到日期。 |
| to_timestamp（timestamp_str [，fmt]）                 | 将timestamp_str表达式和fmt表达式解析为时间戳。返回无效输入的null。默认情况下，如果省略了`fmt`，它将遵循转换规则到时间戳。 |
| to_unix_timestamp（timeExp [，format]）               | 返回给定时间的UNIX时间戳。                                   |
| to_utc_timestamp（时间戳，时区）                      | 给定类似“ 2017-07-14 02：40：00.0”的时间戳，将其解释为给定时区中的时间，并将该时间呈现为UTC中的时间戳。例如，“ GMT + 1”将产生“ 2017-07-14 01：40：00.0”。 |
| 截断（日期，FMT）                                     | 返回“日期”，并将一天中的时间部分截断为格式模型“ fmt”指定的单位。 |
| unix_timestamp（[timeExp [，format]]）                | 返回当前或指定时间的UNIX时间戳。                             |
| 工作日（日期）                                        | 返回日期/时间戳的星期几（0 =星期一，1 =星期二，...，6 =星期日）。 |
| 每年的星期几（日期）                                  | 返回给定日期的一年中的星期。一周被认为是从星期一开始，而一周1是具有3天以上的第一周。 |
| 年（日期）                                            | 返回日期/时间戳的年份部分。                                  |

#### 例子

```
-- add_months
SELECT add_months('2016-08-31', 1);
+---------------------------------------+
|add_months(CAST(2016-08-31 AS DATE), 1)|
+---------------------------------------+
|                             2016-09-30|
+---------------------------------------+

-- current_date
SELECT current_date();
+--------------+
|current_date()|
+--------------+
|    2020-08-28|
+--------------+

SELECT current_date;
+--------------+
|current_date()|
+--------------+
|    2020-08-28|
+--------------+

-- current_timestamp
SELECT current_timestamp();
+--------------------+
| current_timestamp()|
+--------------------+
|2020-08-28 10:16:...|
+--------------------+

SELECT current_timestamp;
+--------------------+
| current_timestamp()|
+--------------------+
|2020-08-28 10:16:...|
+--------------------+

-- date_add
SELECT date_add('2016-07-30', 1);
+-------------------------------------+
|date_add(CAST(2016-07-30 AS DATE), 1)|
+-------------------------------------+
|                           2016-07-31|
+-------------------------------------+

-- date_format
SELECT date_format('2016-04-08', 'y');
+---------------------------------------------+
|date_format(CAST(2016-04-08 AS TIMESTAMP), y)|
+---------------------------------------------+
|                                         2016|
+---------------------------------------------+

-- date_part
SELECT date_part('YEAR', TIMESTAMP '2019-08-12 01:00:00.123456');
+---------------------------------------------------------+
|date_part('YEAR', TIMESTAMP '2019-08-12 01:00:00.123456')|
+---------------------------------------------------------+
|                                                     2019|
+---------------------------------------------------------+

SELECT date_part('week', timestamp'2019-08-12 01:00:00.123456');
+---------------------------------------------------------+
|date_part('week', TIMESTAMP '2019-08-12 01:00:00.123456')|
+---------------------------------------------------------+
|                                                       33|
+---------------------------------------------------------+

SELECT date_part('doy', DATE'2019-08-12');
+-----------------------------------+
|date_part('doy', DATE '2019-08-12')|
+-----------------------------------+
|                                224|
+-----------------------------------+

SELECT date_part('SECONDS', timestamp'2019-10-01 00:00:01.000001');
+------------------------------------------------------------+
|date_part('SECONDS', TIMESTAMP '2019-10-01 00:00:01.000001')|
+------------------------------------------------------------+
|                                                    1.000001|
+------------------------------------------------------------+

SELECT date_part('days', interval 1 year 10 months 5 days);
+------------------------------------------------------+
|date_part('days', INTERVAL '1 years 10 months 5 days')|
+------------------------------------------------------+
|                                                     5|
+------------------------------------------------------+

SELECT date_part('seconds', interval 5 hours 30 seconds 1 milliseconds 1 microseconds);
+----------------------------------------------------------+
|date_part('seconds', INTERVAL '5 hours 30.001001 seconds')|
+----------------------------------------------------------+
|                                                 30.001001|
+----------------------------------------------------------+

-- date_sub
SELECT date_sub('2016-07-30', 1);
+-------------------------------------+
|date_sub(CAST(2016-07-30 AS DATE), 1)|
+-------------------------------------+
|                           2016-07-29|
+-------------------------------------+

-- date_trunc
SELECT date_trunc('YEAR', '2015-03-05T09:32:05.359');
+------------------------------------------------------------+
|date_trunc(YEAR, CAST(2015-03-05T09:32:05.359 AS TIMESTAMP))|
+------------------------------------------------------------+
|                                         2015-01-01 00:00:00|
+------------------------------------------------------------+

SELECT date_trunc('MM', '2015-03-05T09:32:05.359');
+----------------------------------------------------------+
|date_trunc(MM, CAST(2015-03-05T09:32:05.359 AS TIMESTAMP))|
+----------------------------------------------------------+
|                                       2015-03-01 00:00:00|
+----------------------------------------------------------+

SELECT date_trunc('DD', '2015-03-05T09:32:05.359');
+----------------------------------------------------------+
|date_trunc(DD, CAST(2015-03-05T09:32:05.359 AS TIMESTAMP))|
+----------------------------------------------------------+
|                                       2015-03-05 00:00:00|
+----------------------------------------------------------+

SELECT date_trunc('HOUR', '2015-03-05T09:32:05.359');
+------------------------------------------------------------+
|date_trunc(HOUR, CAST(2015-03-05T09:32:05.359 AS TIMESTAMP))|
+------------------------------------------------------------+
|                                         2015-03-05 09:00:00|
+------------------------------------------------------------+

SELECT date_trunc('MILLISECOND', '2015-03-05T09:32:05.123456');
+----------------------------------------------------------------------+
|date_trunc(MILLISECOND, CAST(2015-03-05T09:32:05.123456 AS TIMESTAMP))|
+----------------------------------------------------------------------+
|                                                  2015-03-05 09:32:...|
+----------------------------------------------------------------------+

-- datediff
SELECT datediff('2009-07-31', '2009-07-30');
+------------------------------------------------------------+
|datediff(CAST(2009-07-31 AS DATE), CAST(2009-07-30 AS DATE))|
+------------------------------------------------------------+
|                                                           1|
+------------------------------------------------------------+

SELECT datediff('2009-07-30', '2009-07-31');
+------------------------------------------------------------+
|datediff(CAST(2009-07-30 AS DATE), CAST(2009-07-31 AS DATE))|
+------------------------------------------------------------+
|                                                          -1|
+------------------------------------------------------------+

-- dayofweek
SELECT dayofweek('2009-07-30');
+-----------------------------------+
|dayofweek(CAST(2009-07-30 AS DATE))|
+-----------------------------------+
|                                  5|
+-----------------------------------+

-- dayofyear
SELECT dayofyear('2016-04-09');
+-----------------------------------+
|dayofyear(CAST(2016-04-09 AS DATE))|
+-----------------------------------+
|                                100|
+-----------------------------------+

-- from_unixtime
SELECT from_unixtime(0, 'yyyy-MM-dd HH:mm:ss');
+-----------------------------------------------------+
|from_unixtime(CAST(0 AS BIGINT), yyyy-MM-dd HH:mm:ss)|
+-----------------------------------------------------+
|                                  1970-01-01 00:00:00|
+-----------------------------------------------------+

-- from_utc_timestamp
SELECT from_utc_timestamp('2016-08-31', 'Asia/Seoul');
+-------------------------------------------------------------+
|from_utc_timestamp(CAST(2016-08-31 AS TIMESTAMP), Asia/Seoul)|
+-------------------------------------------------------------+
|                                          2016-08-31 09:00:00|
+-------------------------------------------------------------+

-- hour
SELECT hour('2009-07-30 12:58:59');
+--------------------------------------------+
|hour(CAST(2009-07-30 12:58:59 AS TIMESTAMP))|
+--------------------------------------------+
|                                          12|
+--------------------------------------------+

-- last_day
SELECT last_day('2009-01-12');
+----------------------------------+
|last_day(CAST(2009-01-12 AS DATE))|
+----------------------------------+
|                        2009-01-31|
+----------------------------------+

-- make_date
SELECT make_date(2013, 7, 15);
+----------------------+
|make_date(2013, 7, 15)|
+----------------------+
|            2013-07-15|
+----------------------+

SELECT make_date(2019, 13, 1);
+----------------------+
|make_date(2019, 13, 1)|
+----------------------+
|                  null|
+----------------------+

SELECT make_date(2019, 7, NULL);
+-------------------------------------+
|make_date(2019, 7, CAST(NULL AS INT))|
+-------------------------------------+
|                                 null|
+-------------------------------------+

SELECT make_date(2019, 2, 30);
+----------------------+
|make_date(2019, 2, 30)|
+----------------------+
|                  null|
+----------------------+

-- make_timestamp
SELECT make_timestamp(2014, 12, 28, 6, 30, 45.887);
+-----------------------------------------------------------------+
|make_timestamp(2014, 12, 28, 6, 30, CAST(45.887 AS DECIMAL(8,6)))|
+-----------------------------------------------------------------+
|                                             2014-12-28 06:30:...|
+-----------------------------------------------------------------+

SELECT make_timestamp(2014, 12, 28, 6, 30, 45.887, 'CET');
+----------------------------------------------------------------------+
|make_timestamp(2014, 12, 28, 6, 30, CAST(45.887 AS DECIMAL(8,6)), CET)|
+----------------------------------------------------------------------+
|                                                  2014-12-28 05:30:...|
+----------------------------------------------------------------------+

SELECT make_timestamp(2019, 6, 30, 23, 59, 60);
+-------------------------------------------------------------+
|make_timestamp(2019, 6, 30, 23, 59, CAST(60 AS DECIMAL(8,6)))|
+-------------------------------------------------------------+
|                                          2019-07-01 00:00:00|
+-------------------------------------------------------------+

SELECT make_timestamp(2019, 13, 1, 10, 11, 12, 'PST');
+------------------------------------------------------------------+
|make_timestamp(2019, 13, 1, 10, 11, CAST(12 AS DECIMAL(8,6)), PST)|
+------------------------------------------------------------------+
|                                                              null|
+------------------------------------------------------------------+

SELECT make_timestamp(null, 7, 22, 15, 30, 0);
+-------------------------------------------------------------------------+
|make_timestamp(CAST(NULL AS INT), 7, 22, 15, 30, CAST(0 AS DECIMAL(8,6)))|
+-------------------------------------------------------------------------+
|                                                                     null|
+-------------------------------------------------------------------------+

-- minute
SELECT minute('2009-07-30 12:58:59');
+----------------------------------------------+
|minute(CAST(2009-07-30 12:58:59 AS TIMESTAMP))|
+----------------------------------------------+
|                                            58|
+----------------------------------------------+

-- month
SELECT month('2016-07-30');
+-------------------------------+
|month(CAST(2016-07-30 AS DATE))|
+-------------------------------+
|                              7|
+-------------------------------+

-- months_between
SELECT months_between('1997-02-28 10:30:00', '1996-10-30');
+-------------------------------------------------------------------------------------------+
|months_between(CAST(1997-02-28 10:30:00 AS TIMESTAMP), CAST(1996-10-30 AS TIMESTAMP), true)|
+-------------------------------------------------------------------------------------------+
|                                                                                 3.94959677|
+-------------------------------------------------------------------------------------------+

SELECT months_between('1997-02-28 10:30:00', '1996-10-30', false);
+--------------------------------------------------------------------------------------------+
|months_between(CAST(1997-02-28 10:30:00 AS TIMESTAMP), CAST(1996-10-30 AS TIMESTAMP), false)|
+--------------------------------------------------------------------------------------------+
|                                                                          3.9495967741935485|
+--------------------------------------------------------------------------------------------+

-- next_day
SELECT next_day('2015-01-14', 'TU');
+--------------------------------------+
|next_day(CAST(2015-01-14 AS DATE), TU)|
+--------------------------------------+
|                            2015-01-20|
+--------------------------------------+

-- now
SELECT now();
+--------------------+
|               now()|
+--------------------+
|2020-08-28 10:16:...|
+--------------------+

-- quarter
SELECT quarter('2016-08-31');
+---------------------------------+
|quarter(CAST(2016-08-31 AS DATE))|
+---------------------------------+
|                                3|
+---------------------------------+

-- second
SELECT second('2009-07-30 12:58:59');
+----------------------------------------------+
|second(CAST(2009-07-30 12:58:59 AS TIMESTAMP))|
+----------------------------------------------+
|                                            59|
+----------------------------------------------+

-- to_date
SELECT to_date('2009-07-30 04:17:52');
+------------------------------+
|to_date('2009-07-30 04:17:52')|
+------------------------------+
|                    2009-07-30|
+------------------------------+

SELECT to_date('2016-12-31', 'yyyy-MM-dd');
+-----------------------------------+
|to_date('2016-12-31', 'yyyy-MM-dd')|
+-----------------------------------+
|                         2016-12-31|
+-----------------------------------+

-- to_timestamp
SELECT to_timestamp('2016-12-31 00:12:00');
+-----------------------------------+
|to_timestamp('2016-12-31 00:12:00')|
+-----------------------------------+
|                2016-12-31 00:12:00|
+-----------------------------------+

SELECT to_timestamp('2016-12-31', 'yyyy-MM-dd');
+----------------------------------------+
|to_timestamp('2016-12-31', 'yyyy-MM-dd')|
+----------------------------------------+
|                     2016-12-31 00:00:00|
+----------------------------------------+

-- to_unix_timestamp
SELECT to_unix_timestamp('2016-04-08', 'yyyy-MM-dd');
+-----------------------------------------+
|to_unix_timestamp(2016-04-08, yyyy-MM-dd)|
+-----------------------------------------+
|                               1460073600|
+-----------------------------------------+

-- to_utc_timestamp
SELECT to_utc_timestamp('2016-08-31', 'Asia/Seoul');
+-----------------------------------------------------------+
|to_utc_timestamp(CAST(2016-08-31 AS TIMESTAMP), Asia/Seoul)|
+-----------------------------------------------------------+
|                                        2016-08-30 15:00:00|
+-----------------------------------------------------------+

-- trunc
SELECT trunc('2019-08-04', 'week');
+-------------------------------------+
|trunc(CAST(2019-08-04 AS DATE), week)|
+-------------------------------------+
|                           2019-07-29|
+-------------------------------------+

SELECT trunc('2019-08-04', 'quarter');
+----------------------------------------+
|trunc(CAST(2019-08-04 AS DATE), quarter)|
+----------------------------------------+
|                              2019-07-01|
+----------------------------------------+

SELECT trunc('2009-02-12', 'MM');
+-----------------------------------+
|trunc(CAST(2009-02-12 AS DATE), MM)|
+-----------------------------------+
|                         2009-02-01|
+-----------------------------------+

SELECT trunc('2015-10-27', 'YEAR');
+-------------------------------------+
|trunc(CAST(2015-10-27 AS DATE), YEAR)|
+-------------------------------------+
|                           2015-01-01|
+-------------------------------------+

-- unix_timestamp
SELECT unix_timestamp();
+--------------------------------------------------------+
|unix_timestamp(current_timestamp(), yyyy-MM-dd HH:mm:ss)|
+--------------------------------------------------------+
|                                              1598609785|
+--------------------------------------------------------+

SELECT unix_timestamp('2016-04-08', 'yyyy-MM-dd');
+--------------------------------------+
|unix_timestamp(2016-04-08, yyyy-MM-dd)|
+--------------------------------------+
|                            1460073600|
+--------------------------------------+

-- weekday
SELECT weekday('2009-07-30');
+---------------------------------+
|weekday(CAST(2009-07-30 AS DATE))|
+---------------------------------+
|                                3|
+---------------------------------+

-- weekofyear
SELECT weekofyear('2008-02-20');
+------------------------------------+
|weekofyear(CAST(2008-02-20 AS DATE))|
+------------------------------------+
|                                   8|
+------------------------------------+

-- year
SELECT year('2016-07-30');
+------------------------------+
|year(CAST(2016-07-30 AS DATE))|
+------------------------------+
|                          2016|
+------------------------------+
```

### JSON函数

| 功能                                     | 描述                                                         |
| :--------------------------------------- | :----------------------------------------------------------- |
| from_json（jsonStr，schema [，options]） | 返回具有给定的jsonStr和schema的结构值。                      |
| get_json_object（json_txt，路径）        | 从`path`中提取一个json对象。                                 |
| json_tuple（jsonStr，p1，p2，...，pn）   | 返回一个类似于函数get_json_object的元组，但是它具有多个名称。所有输入参数和输出列类型都是字符串。 |
| schema_of_json（json [，options]）       | 以JSON字符串的DDL格式返回架构。                              |
| to_json（expr [，options]）              | 返回具有给定结构值的JSON字符串                               |

#### 例子

```sql
-- from_json
SELECT from_json('{"a":1, "b":0.8}', 'a INT, b DOUBLE');
+---------------------------+
|from_json({"a":1, "b":0.8})|
+---------------------------+
|                   [1, 0.8]|
+---------------------------+

SELECT from_json('{"time":"26/08/2015"}', 'time Timestamp', map('timestampFormat', 'dd/MM/yyyy'));
+--------------------------------+
|from_json({"time":"26/08/2015"})|
+--------------------------------+
|            [2015-08-26 00:00...|
+--------------------------------+

-- get_json_object
SELECT get_json_object('{"a":"b"}', '$.a');
+-------------------------------+
|get_json_object({"a":"b"}, $.a)|
+-------------------------------+
|                              b|
+-------------------------------+

-- json_tuple
SELECT json_tuple('{"a":1, "b":2}', 'a', 'b');
+---+---+
| c0| c1|
+---+---+
|  1|  2|
+---+---+

-- schema_of_json
SELECT schema_of_json('[{"col":0}]');
+---------------------------+
|schema_of_json([{"col":0}])|
+---------------------------+
|       array<struct<col:...|
+---------------------------+

SELECT schema_of_json('[{"col":01}]', map('allowNumericLeadingZeros', 'true'));
+----------------------------+
|schema_of_json([{"col":01}])|
+----------------------------+
|        array<struct<col:...|
+----------------------------+

-- to_json
SELECT to_json(named_struct('a', 1, 'b', 2));
+---------------------------------+
|to_json(named_struct(a, 1, b, 2))|
+---------------------------------+
|                    {"a":1,"b":2}|
+---------------------------------+

SELECT to_json(named_struct('time', to_timestamp('2015-08-26', 'yyyy-MM-dd')), map('timestampFormat', 'dd/MM/yyyy'));
+---------------------------------------------------------------------+
|to_json(named_struct(time, to_timestamp('2015-08-26', 'yyyy-MM-dd')))|
+---------------------------------------------------------------------+
|                                                 {"time":"26/08/20...|
+---------------------------------------------------------------------+

SELECT to_json(array(named_struct('a', 1, 'b', 2)));
+----------------------------------------+
|to_json(array(named_struct(a, 1, b, 2)))|
+----------------------------------------+
|                         [{"a":1,"b":2}]|
+----------------------------------------+

SELECT to_json(map('a', named_struct('b', 1)));
+-----------------------------------+
|to_json(map(a, named_struct(b, 1)))|
+-----------------------------------+
|                      {"a":{"b":1}}|
+-----------------------------------+

SELECT to_json(map(named_struct('a', 1),named_struct('b', 2)));
+----------------------------------------------------+
|to_json(map(named_struct(a, 1), named_struct(b, 2)))|
+----------------------------------------------------+
|                                     {"[1]":{"b":2}}|
+----------------------------------------------------+

SELECT to_json(map('a', 1));
+------------------+
|to_json(map(a, 1))|
+------------------+
|           {"a":1}|
+------------------+

SELECT to_json(array((map('a', 1))));
+-------------------------+
|to_json(array(map(a, 1)))|
+-------------------------+
|                [{"a":1}]|
+-------------------------+
```

