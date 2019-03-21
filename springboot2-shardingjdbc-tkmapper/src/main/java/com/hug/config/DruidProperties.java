package com.hug.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource({"classpath:jdbc.properties"})
public class DruidProperties {


	@Value("${db0.url}")
	private String db0_url;
	@Value("${db0.username}")
	private String db0_username;
	@Value("${db0.password}")
	private String db0_password;
	@Value("${db0.driverClassName}")
	private String db0_driverClassName;
	@Value("${db0.dbName}")
	private String db0_dbName;

	@Value("${db1.url}")
	private String db1_url;
	@Value("${db1.username}")
	private String db1_username;
	@Value("${db1.password}")
	private String db1_password;
	@Value("${db1.driverClassName}")
	private String db1_driverClassName;
	@Value("${db1.dbName}")
	private String db1_dbName;

	@Value("${db2.url}")
	private String db2_url;
	@Value("${db2.username}")
	private String db2_username;
	@Value("${db2.password}")
	private String db2_password;
	@Value("${db2.driverClassName}")
	private String db2_driverClassName;
	@Value("${db2.dbName}")
	private String db2_dbName;

	@Value("${jdbc.max_active}")
	private int maxActive;
	@Value("${jdbc.initial_size}")
	private int initialSize;
	@Value("${jdbc.max_wait}")
	private long maxWait;
	@Value("${jdbc.min_idle}")
	private int minIdle;
	@Value("${jdbc.test-while-idle}")
	private boolean testWhileIdle;
	@Value("${jdbc.validation-query}")
	private String validationQuery;
	@Value("${jdbc.connectionProperties}")
	private String connectionProperties;

	public String getDb0_url() {
		return db0_url;
	}

	public String getDb0_username() {
		return db0_username;
	}

	public String getDb0_password() {
		return db0_password;
	}

	public String getDb0_driverClassName() {
		return db0_driverClassName;
	}

	public String getDb0_dbName() {
		return db0_dbName;
	}

	public String getDb1_url() {
		return db1_url;
	}

	public String getDb1_username() {
		return db1_username;
	}

	public String getDb1_password() {
		return db1_password;
	}

	public String getDb1_driverClassName() {
		return db1_driverClassName;
	}

	public String getDb1_dbName() {
		return db1_dbName;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public String getConnectionProperties() {
		return connectionProperties;
	}

	public String getDb2_url() {
		return db2_url;
	}

	public String getDb2_username() {
		return db2_username;
	}

	public String getDb2_password() {
		return db2_password;
	}

	public String getDb2_driverClassName() {
		return db2_driverClassName;
	}

	public String getDb2_dbName() {
		return db2_dbName;
	}
}
