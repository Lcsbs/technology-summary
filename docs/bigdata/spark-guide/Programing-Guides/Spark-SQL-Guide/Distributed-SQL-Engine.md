# 分布式SQL引擎

- [运行Thrift JDBC / ODBC服务器](http://spark.apache.org/docs/latest/sql-distributed-sql-engine.html#running-the-thrift-jdbcodbc-server)
- [运行Spark SQL CLI](http://spark.apache.org/docs/latest/sql-distributed-sql-engine.html#running-the-spark-sql-cli)

Spark SQL还可以使用其JDBC / ODBC或命令行界面充当分布式查询引擎。在这种模式下，最终用户或应用程序可以直接与Spark SQL交互以运行SQL查询，而无需编写任何代码。

## 运行Thrift JDBC / ODBC服务器

此处实现的Thrift JDBC / ODBC服务器对应[`HiveServer2`](https://cwiki.apache.org/confluence/display/Hive/Setting+Up+HiveServer2) 于内置的Hive。您可以使用Spark或兼容的Hive随附的beeline脚本测试JDBC服务器。

要启动JDBC / ODBC服务器，请在Spark目录中运行以下命令：

```shell
./sbin/start-thriftserver.sh
```

该脚本接受所有`bin/spark-submit`命令行选项，以及`--hiveconf`用于指定Hive属性的选项。您可以运行`./sbin/start-thriftserver.sh --help`以获取所有可用选项的完整列表。默认情况下，服务器在localhost：10000上侦听。您可以通过任一环境变量来覆盖此行为，即：

```shell
export HIVE_SERVER2_THRIFT_PORT=<listening-port>
export HIVE_SERVER2_THRIFT_BIND_HOST=<listening-host>
./sbin/start-thriftserver.sh \
  --master <master-uri> \
  ...
```

或系统属性：

```shell
./sbin/start-thriftserver.sh \
  --hiveconf hive.server2.thrift.port=<listening-port> \
  --hiveconf hive.server2.thrift.bind.host=<listening-host> \
  --master <master-uri>
  ...
```

现在，您可以使用beeline测试Thrift JDBC / ODBC服务器：

```shell
./bin/beeline
```

通过以下方式直线连接到JDBC / ODBC服务器：

```shell
beeline> !connect jdbc:hive2://localhost:10000
```

Beeline会要求您提供用户名和密码。在非安全模式下，只需在计算机上输入用户名和空白密码即可。对于安全模式，请遵循[beeline文档中](https://cwiki.apache.org/confluence/display/Hive/HiveServer2+Clients)给出的 [说明](https://cwiki.apache.org/confluence/display/Hive/HiveServer2+Clients)。

Hive的结构是通过将您做`hive-site.xml`，`core-site.xml`和`hdfs-site.xml`文件`conf/`。

您还可以使用Hive随附的beeline脚本。

Thrift JDBC服务器还支持通过HTTP传输发送Thrift RPC消息。使用以下设置可以将HTTP模式作为系统属性或在`hive-site.xml`文件中启用`conf/`：

```shell
hive.server2.transport.mode - Set this to value: http
hive.server2.thrift.http.port - HTTP port number to listen on; default is 10001
hive.server2.http.endpoint - HTTP endpoint; default is cliservice
```

要进行测试，请使用beeline通过以下方式以http模式连接到JDBC / ODBC服务器：

```shell
beeline> !connect jdbc:hive2://<host>:<port>/<database>?hive.server2.transport.mode=http;hive.server2.thrift.http.path=<http_endpoint>
```

如果您关闭会话并执行CTAS，则必须在中将其设置`fs.%s.impl.disable.cache`为true `hive-site.xml`。在[[SPARK-21067\]中](https://issues.apache.org/jira/browse/SPARK-21067)查看更多详细信息。

## 运行Spark SQL CLI

Spark SQL CLI是一种方便的工具，可以在本地模式下运行Hive Metastore服务并执行从命令行输入的查询。请注意，Spark SQL CLI无法与Thrift JDBC服务器通信。

要启动Spark SQL CLI，请在Spark目录中运行以下命令：

```
./bin/spark-sql
```

Hive的结构是通过将您做`hive-site.xml`，`core-site.xml`和`hdfs-site.xml`文件`conf/`。您可以运行`./bin/spark-sql --help`以获取所有可用选项的完整列表。