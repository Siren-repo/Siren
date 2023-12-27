package com.devlop.siren.global.configuration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@SpringBootTest
class DynamicRoutingDataSourceTest {
  @Autowired private Environment environment;

  @Test
  @DisplayName("Master_DataSource_테스트")
  void masterDataSourceTest(@Qualifier("masterDataSource") final DataSource masterDataSource) {
    // given
    String url = environment.getProperty("spring.datasource.master.jdbc-url");
    String username = environment.getProperty("spring.datasource.master.username");
    String driverClassName = environment.getProperty("spring.datasource.master.driver-class-name");
    Boolean readOnly =
        Boolean.valueOf(environment.getProperty("spring.datasource.master.read-only"));

    // when
    HikariDataSource hikariDataSource = (HikariDataSource) masterDataSource;

    // then
    verifyOf(readOnly, url, username, driverClassName, hikariDataSource);
  }

  @Test
  @DisplayName("Slave_DataSource_테스트")
  void slaveDataSourceTest(@Qualifier("slaveDataSource") final DataSource slaveDataSource) {
    // given
    String url = environment.getProperty("spring.datasource.slave.jdbc-url");
    String username = environment.getProperty("spring.datasource.slave.username");
    String driverClassName = environment.getProperty("spring.datasource.slave.driver-class-name");
    Boolean readOnly =
        Boolean.valueOf(environment.getProperty("spring.datasource.slave.read-only"));

    // when
    HikariDataSource hikariDataSource = (HikariDataSource) slaveDataSource;

    // then
    verifyOf(readOnly, url, username, driverClassName, hikariDataSource);
  }

  private void verifyOf(
      Boolean readOnly,
      String url,
      String username,
      String driverClassName,
      HikariDataSource hikariDataSource) {
    assertThat(hikariDataSource.isReadOnly()).isEqualTo(readOnly);
    assertThat(hikariDataSource.getJdbcUrl()).isEqualTo(url);
    assertThat(hikariDataSource.getUsername()).isEqualTo(username);
    assertThat(hikariDataSource.getDriverClassName()).isEqualTo(driverClassName);
  }
}
