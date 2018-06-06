package com.structured.APP;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.sql.ForeachWriter;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.structured.conf.ConfigurationManager;
import com.structured.constant.Constants;
import com.structured.pool.hbase.HbaseConnectionPool;
import com.structured.pool.tool.ConnectionPoolConfig;

import scala.Serializable;

public class ForeachWriterHBase extends ForeachWriter<Row> implements
		Serializable {
	public static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(ForeachWriterHBase.class);
	public static HbaseConnectionPool pool = null;
	public  Connection conn = null;

	static {
		ConnectionPoolConfig config = new ConnectionPoolConfig();
		// 配置连接池参数
		config.setMaxTotal(ConfigurationManager
				.getInteger(Constants.HBASE_POOL_MAX_TOTAL));
		config.setMaxIdle(ConfigurationManager
				.getInteger(Constants.HBASE_POOL_MAX_IDLE));
		config.setMaxWaitMillis(ConfigurationManager
				.getInteger(Constants.HBASE_POOL_MAX_WAITMILLIS));
		config.setTestOnBorrow(ConfigurationManager
				.getBoolean(Constants.HBASE_POOL_TESTONBORROW));
		Configuration hbaseConfig = getHBaseConfiguration();
		pool =new HbaseConnectionPool(config, hbaseConfig);
	}
	
	public static synchronized Connection getConn() {
		return pool.getConnection();
	}
	
	@Override
	public boolean open(long partitionId, long version) {
		try {
			conn = getConn();
			return true;
		} catch (Exception e) {
			pool.returnConnection(conn);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(Row value) {
		GenericRowWithSchema genericRowWithSchema = (GenericRowWithSchema) value;
		// 数据写入格式如下(中间分割符为\001)
		// dbName tableName tableRowkey tableData
		// tets test 1111 {"a1":"a1","a2":"a3"}
		String[] tableInformation= genericRowWithSchema.get(0).toString().split(Constants.SEPARATOR_001);
		
		String dbName = tableInformation[0];
		String tableName = tableInformation[1];
		Object tableRowkey = tableInformation[2];
		
	    Gson gson = new Gson();
	    Map<String, Object> tableData = new HashMap<String, Object>();
	    tableData = gson.fromJson(tableInformation[3], tableData.getClass());

		// 调用数据解析器
		tableName = dbName + "." + tableName;

		HTableDescriptor table = new HTableDescriptor(
				TableName.valueOf(tableName));
		table.addFamily(new HColumnDescriptor(Constants.CF_DEFAULT)
				.setCompressionType(Algorithm.NONE));

		Table tablePut = null;
		Admin admin = null;
		try {
			tablePut = conn.getTable(TableName.valueOf(tableName));
			admin = conn.getAdmin();
			TableName tName = table.getTableName();
			if (!admin.tableExists(tName)) {
				try {
					admin.createTable(table);
					tablePut = conn.getTable(TableName.valueOf(tableName));
					// admin.flush(tName);
				} catch (Exception e) {
					logger.error("建表失败： ->" + tableName);
				}
			}
		} catch (IOException e1) {
			logger.error("获取tablePut或Admin失败： ->" + tableName);
		}

		try {
			Put put = setDataPut(tableRowkey, tableData);
			tablePut.put(put);
		} catch (Exception e) {
			logger.error("写入数据失败： ->" + tableName + "-" + tableData);
		}

		try {
			admin.close();
			tablePut.close();
		} catch (IOException e) {
			logger.error("关闭tablePut或Admin失败： ->" + tableName);
		}
//		pool.returnConnection(conn);

	}

	@Override
	public void close(Throwable errorOrNull) {
		pool.returnConnection(conn);
	}
	
	
	public static Configuration getHBaseConfiguration() {
		Configuration conf = null;
		try {
			conf = HBaseConfiguration.create();
			conf.set("hbase.zookeeper.quorum", ConfigurationManager
					.getProperty(Constants.ZK_METADATA_BROKER_LIST));
			conf.set("hbase.defaults.for.version.skip", "true");
			
		} catch (Exception e) {
			logger.error("获取HBaseConfiguration出错，请检查是否有配置文件和ZK是否正常。ZK链接： ->"
					+ ConfigurationManager
							.getProperty(Constants.ZK_METADATA_BROKER_LIST));
		}
		return conf;
	}
	
	public static Put setDataPut(Object tableRowkey,
			Map<String, Object> tableData) {
		Put put = new Put(Bytes.toBytes(tableRowkey.toString()));
		for (Entry<String, Object> entry : tableData.entrySet()) {
			put.addColumn(Bytes.toBytes(Constants.CF_DEFAULT),
					Bytes.toBytes(entry.getKey()),
					Bytes.toBytes(entry.getValue().toString()));
		}
		return put;
	}
}