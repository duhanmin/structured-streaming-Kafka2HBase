## structured-streaming-Kafka2HBase项目介绍  
Spark structured-streaming 消费kafka数据写入hbase<br />
该项目修改基础配置就可以跑<br />

### 数据格式

```
数据写入格式如下(中间分割符为\001)
dbName tableName tableRowkey tableData
tets test 1111 {"a1":"a1","a2":"a3"}
```

### 数据如下
``` 
"A"+ "\001"
+ "test2"+ "\001"
+ "1111"+ "\001"
+ "{\"a1\":\"a1\",\"a2\":\"a3\"}"
```
