**分库分表是为了解决由于库、表数据量过大，而导致数据库性能持续下降的问题。** 常见的分库分表工具有：`sharding-jdbc`（当当）、`TSharding`（蘑菇街）、`MyCAT`（基于Cobar）、`Cobar`（阿里巴巴）...。

**推荐使用 `sharding-jdbc`** 。 因为，`sharding-jdbc` 是一款轻量级 `Java` 框架，以 `jar` 包形式提供服务，不要我们做额外的运维工作，并且兼容性也很好。