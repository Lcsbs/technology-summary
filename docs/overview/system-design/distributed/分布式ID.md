在复杂分布式系统中，往往需要对大量的数据和消息进行唯一标识。比如数据量太大之后，往往需要对进行对数据进行分库分表，分库分表后需要有一个唯一ID来标识一条数据或消息，数据库的自增ID显然不能满足需求。

相关阅读：[为什么要分布式 id ？分布式 id 生成方案有哪些？](docs/system-design/micro-service/分布式id生成方案总结.md)