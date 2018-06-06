package com.structured.constant;

import java.text.SimpleDateFormat;

/**
 * 常量接口
 * @author rttxdu
 *
 */
public interface Constants {
	/**
	 * Spark作业相关的常量
	 */
	String HDFS_LODA_ADTA_PATH= "hdfs.LodaData";
	String STREAMING_CHECKPOINT_PATH = "streaming.checkpoint.path";
	String  SPARK_RUNMODE_MASTER = "spark.runmode.master";
	String SPARK_PROJECT_NAME = "spark.project.name";
	String SPARK_STREAMING_BATCH_TIME = "spark.string.batch.time";

	/**
	 * JDBC配置
	 */
	String JDBC_URL = "jdbc.url";
	String JDBC_USER = "jdbc.user";
	String JDBC_PASSWORD = "jdbc.password";
	String JDBC_USER_NAME = "user";
	String JDBC_PASSWORD_NAME = "password";

	/**
	 * KAFKA和ZK配置
	 */
	String KAFKA_METADATA_BROKER_LIST = "kafka.metadata.broker.list";
	String ZK_METADATA_BROKER_LIST = "zk.metadata.broker.list";
	String KAFKA_TOPICS_ID = "kafka.topics.id";
	String KAFKA_TOPICS_MYSQL_TABLENAME = "Kafka.topics.mysql.tablename";
	String KAFKA_TOPICS_MYSQL_TOPICNAME = "Kafka.topics.mysql.topic.name";
	String KAFKA_TOPICS_MYSQL_TOPICID = "Kafka.topics.mysql.topic.id";
	String KAFKA_TOPICS_DATA_CHANNEL_DB2HBASE = "Kafka.topics.data.Channel.DB2HBASE";
	String KAFKA_TOPICS_DATA_CHANNEL_FLUME2HBASE= "Kafka.topics.data.Channel.FLUME2HBASE";	
	String KAFKA_TOPICS_DATA_CHANNEL_TPTDP2HBASE= "Kafka.topics.data.Channel.TPTDP2HBASE";	
	String KAFKA_TOPICS_CHANNEL_DB2HDFS = "Kafka.topics.data.Channel.DB2HDFS";
	
	/**
	 * HBase相关
	 */
	String CF_DEFAULT = "info";
	String DEFAULT_ROW_KEY = "_pk";
	String HBASE_POOL_MAX_TOTAL = "hbase.pool.max-total";
	String HBASE_POOL_MAX_IDLE = "hbase.pool.max-idle";
	String HBASE_POOL_MAX_WAITMILLIS = "hbase.pool.max-waitmillis";
	String HBASE_POOL_TESTONBORROW = "hbase.pool.testonborrow";
	
	/**
	 * 数据管道json字段
	 */
	String CHANNEL_JSON_EVEENTCHANNEL = "eventChannel";
	String CHANNEL_JSON_EVEENTID = "eventId";
	String CHANNEL_JSON_EVEENTTIME = "eventTime";
	String CHANNEL_JSON_EVEENTTYPE = "eventType";
	String CHANNEL_JSON_EVEENTDATA = "eventData";
	String CHANNEL_JSON_EVEENTTARGET = "eventTarget";
	
	/**
	 * 分隔符和常量
	 */
	String SEPARATOR_001 = "\001";
	String SEPARATOR_002 = "\002";
	String ENCODE_UTF8 = "UTF-8";
	String DECODE_UTF8 = "UTF-8";
	
	/**
	 * 用于身份证解析与反解析
	 */
	String IDCard_BACK_x = "x";
	String IDCard_BACK_X = "X";
	String IDCard_BACK_11 = "11";
	String STR_NUMBER_0 = "0";
	
	/**
	 * 格式化时间
	 */
    SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat DATEKEY_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * jar产生的log日志
	 */
	String JAR_LOG_PATH = "jar.log.path";
}
