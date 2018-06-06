package com.structured.APP;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;

import com.structured.conf.ConfigurationManager;
import com.structured.constant.Constants;

public class structuredJava {

	public static void main(String[] args) {

		SparkSession spark = SparkSession.builder()
				.appName("structured-streaming-Kafka2HBase")
				.master("local[4]")
				.getOrCreate();

		Dataset<Row> line = spark
				.readStream()
				.format("kafka")
				.option("kafka.bootstrap.servers",ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST))
				.option("subscribe", "Kafka2HBase").load();

		Dataset<Row> dataset = line.selectExpr("CAST(value AS STRING)");

		StreamingQuery query = dataset.writeStream()
				.foreach(new ForeachWriterHBase())
				.outputMode("update").start();

		try {
			query.awaitTermination();
		} catch (StreamingQueryException e) {
			e.printStackTrace();
		}
	}
}
