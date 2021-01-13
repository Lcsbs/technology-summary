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