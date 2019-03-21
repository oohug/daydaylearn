package com.hug.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.InlineShardingStrategyConfiguration;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DruidConfig {

    @Autowired
    private DruidProperties properties;

    @Bean(name = "shardingDataSource", destroyMethod = "close")
    @Qualifier("shardingDataSource")
    public DataSource getShardingDataSource() {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>(3);
        // 配置第一个数据源
        DruidDataSource dbs0 = createDefaultDruidDataSource();
        dbs0.setDriverClassName(properties.getDb0_driverClassName());
        dbs0.setUrl(properties.getDb0_url());
        dbs0.setUsername(properties.getDb0_username());
        dbs0.setPassword(properties.getDb0_password());
        dataSourceMap.put("db0", dbs0);
        // 配置第二个数据源
        DruidDataSource dbs1 = createDefaultDruidDataSource();
        dbs1.setDriverClassName(properties.getDb1_driverClassName());
        dbs1.setUrl(properties.getDb1_url());
        dbs1.setUsername(properties.getDb1_username());
        dbs1.setPassword(properties.getDb1_password());
        dataSourceMap.put("db1", dbs1);
        // 配置第3个数据源
        DruidDataSource dbs2 = createDefaultDruidDataSource();
        dbs2.setDriverClassName(properties.getDb2_driverClassName());
        dbs2.setUrl(properties.getDb2_url());
        dbs2.setUsername(properties.getDb2_username());
        dbs2.setPassword(properties.getDb2_password());
        dataSourceMap.put("db2", dbs2);

        // 配置Order表规则
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
        orderTableRuleConfig.setActualDataNodes("db${0..2}.t_order_${0..1}");

        // 配置分库策略（Groovy表达式配置db规则）
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "db${user_id % 2}"));
        // 配置分表策略（Groovy表达式配置表路由规则）
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order_${order_id % 2}"));

        // 配置分片规则
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        shardingRuleConfig.setDefaultDataSourceName("db0");

        // 获取数据源对象
        DataSource dataSource = null;
        try {
            dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new ConcurrentHashMap(), new Properties());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    public DruidDataSource createDefaultDruidDataSource() {
        DruidDataSource datasource = new DruidDataSource();

        // 获取连接等待超时的时间
        datasource.setMaxWait(properties.getMaxWait());
        // 初始化大小、最小、最大
        datasource.setInitialSize(properties.getInitialSize());
        datasource.setMinIdle(properties.getMinIdle());
        datasource.setMaxActive(properties.getMaxActive());

        datasource.setTestWhileIdle(properties.isTestWhileIdle());
        datasource.setValidationQuery(properties.getValidationQuery());
        datasource.setTestOnBorrow(false);
        datasource.setTestOnReturn(false);
        // 间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        datasource.setTimeBetweenEvictionRunsMillis(60000);
        // 一个连接在池中最小生存的时间，单位是毫秒
        datasource.setMinEvictableIdleTimeMillis(300000);

        // 自动回收超时连接
        datasource.setRemoveAbandoned(true);
        // 超时时间(单位 秒)
        // 自动回收超时连接时打印连接的超时错误
        datasource.setLogAbandoned(true);

        // 慢查询
        datasource.setConnectionProperties(properties.getConnectionProperties());

        try {
            datasource.setFilters("stat,wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    @Bean
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("shardingDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // http://localhost:8080/druid/weburi.html
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("admin", "admin");  // 用户名
        initParameters.put("123123", "123123");   // 密码
        initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
//        initParameters.put("allow", "127.0.0.1"); // IP白名单 (没有配置或者为空，则允许所有访问)
        // initParameters.put("deny", "192.168.20.38");// IP黑名单
        // (存在共同时，deny优先于allow)
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean(value = "druid-stat-interceptor")
    public DruidStatInterceptor DruidStatInterceptor() {
        DruidStatInterceptor druidStatInterceptor = new DruidStatInterceptor();
        return druidStatInterceptor;
    }

    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setProxyTargetClass(true);
        // 设置要监控的bean的id
        //beanNameAutoProxyCreator.setBeanNames("sysRoleMapper","loginController");
        beanNameAutoProxyCreator.setInterceptorNames("druid-stat-interceptor");
        return beanNameAutoProxyCreator;
    }

}