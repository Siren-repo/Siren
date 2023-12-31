package com.devlop.siren.global.configuration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class RoutingDataSourceConfigTest {
  private static final String Test_Method_Name = "determineCurrentLookupKey";

  @Test
  @DisplayName("쓰기_전용_트랜잭션_테스트")
  @Transactional(readOnly = false)
  void writeOnlyTransactionTest()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    // given
    DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

    // when
    Method determineCurrentLookupKey =
        DynamicRoutingDataSource.class.getDeclaredMethod(Test_Method_Name);
    determineCurrentLookupKey.setAccessible(true);

    Object dataSourceType = determineCurrentLookupKey.invoke(dynamicRoutingDataSource);

    // then
    assertThat(dataSourceType).isEqualTo(DataSourceConstants.MASTER);
  }

  @Test
  @DisplayName("읽기_전용_트랜잭션_테스트")
  @Transactional(readOnly = true)
  void readOnlyTransactionTest()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    // given
    DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();

    // when
    Method determineCurrentLookupKey =
        DynamicRoutingDataSource.class.getDeclaredMethod(Test_Method_Name);
    determineCurrentLookupKey.setAccessible(true);

    Object dataSourceType = determineCurrentLookupKey.invoke(dynamicRoutingDataSource);

    // then
    assertThat(dataSourceType).isEqualTo(DataSourceConstants.SLAVE);
  }
}
