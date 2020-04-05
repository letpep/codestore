package com.letpep.osweb.IdGenerater.config;

import com.zaxxer.hikari.HikariConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date 2020
 */
@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment environment;
    private static final String SEP = ",";

    private static final String DEFAULT_DATASOURCE_TYPE = "com.zaxxer.hikari.HikariDataSource";
    @Autowired
    DataSourceChecker dataSourceChecker;


    @Bean
    public DataSource getDynamicDataSource() {
        DynamicDataSource routingDataSource = new DynamicDataSource();
        List<String> dataSourceKeys = new ArrayList<>();
        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
        Binder binder = new Binder(sources);
        BindResult<Properties> bindResult = binder.bind("datasource.letpepid", Properties.class);
        Properties properties= bindResult.get();
        String names = (String) properties.get("names");
        String dataSourceType = (String) properties.get("type");

        Map<Object, Object> targetDataSources = new HashMap<>(4);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDataSourceKeys(dataSourceKeys);
        // 多个数据源
        for (String name : names.split(SEP)) {
            BindResult<Properties> dataSourceNameResult = binder.bind("datasource.letpepid."+name, Properties.class);
            Properties dataSourceNameProperties= dataSourceNameResult.get();

            DataSource dataSource = buildDataSource(dataSourceType, dataSourceNameProperties);
            targetDataSources.put(name, dataSource);
            dataSourceKeys.add(name);
        }
            dataSourceChecker.getSourcekeys().addAll(dataSourceKeys);

        return routingDataSource;
    }



    private HikariDataSource buildDataSource(String dataSourceType, Properties dsMap) {
        try {
            String className = DEFAULT_DATASOURCE_TYPE;
            if (dataSourceType != null && !"".equals(dataSourceType.trim())) {
                className = dataSourceType;
            }
            String driverClassName = dsMap.get("driver-class-name").toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            HikariConfig jdbcConfig = new HikariConfig();
            jdbcConfig.setPoolName(getClass().getName());
            jdbcConfig.setDriverClassName(driverClassName);
            jdbcConfig.setJdbcUrl(url);
            jdbcConfig.setUsername(username);
            jdbcConfig.setPassword(password);
            jdbcConfig.setMaximumPoolSize(5);
            jdbcConfig.setMaxLifetime(1800000);
            jdbcConfig.setConnectionTimeout(2000);
            jdbcConfig.setIdleTimeout(30000);

            return new HikariDataSource(jdbcConfig);
        } catch (Exception e) {
            logger.error("buildDataSource error", e);
            throw new IllegalStateException(e);
        }
    }


}
