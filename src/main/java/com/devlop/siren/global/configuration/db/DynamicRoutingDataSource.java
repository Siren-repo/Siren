package com.devlop.siren.global.configuration.db;

import static com.devlop.siren.global.configuration.db.DataSourceConstants.MASTER;
import static com.devlop.siren.global.configuration.db.DataSourceConstants.SLAVE;
import static org.springframework.transaction.support.TransactionSynchronizationManager.isCurrentTransactionReadOnly;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {
    return isCurrentTransactionReadOnly() ? SLAVE : MASTER;
  }
}
