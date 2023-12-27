package com.devlop.siren.global.configuration.db;

import static com.devlop.siren.global.configuration.db.DataSourceConstants.MASTER;
import static com.devlop.siren.global.configuration.db.DataSourceConstants.SLAVE;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Configuration
public class RoutingDataSourceConfig {

  @DependsOn({"masterDataSource", "slaveDataSource"})
  @Bean(name = "routingDataSource")
  public DataSource routingDataSource(
      @Qualifier("masterDataSource") DataSource master,
      @Qualifier("slaveDataSource") DataSource slave) {
    DynamicRoutingDataSource routingDataSource = new DynamicRoutingDataSource();

    Map<Object, Object> dataSourceMap = new HashMap<>();

    dataSourceMap.put(MASTER, master);
    dataSourceMap.put(SLAVE, slave);

    routingDataSource.setTargetDataSources(dataSourceMap);
    routingDataSource.setDefaultTargetDataSource(master);

    return routingDataSource;
  }

  @DependsOn({"routingDataSource"})
  @Bean(name = "dataSource")
  public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }
}
