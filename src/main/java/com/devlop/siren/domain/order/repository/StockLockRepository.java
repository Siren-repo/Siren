package com.devlop.siren.domain.order.repository;

import com.devlop.siren.domain.stock.entity.Stock;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StockLockRepository extends JpaRepository<Stock, Long> {
  @Query(value = "select get_lock(:name, :time)", nativeQuery = true)
  void getLock(@Param("name") String name, @Param("time") String time);

  @Query(value = "select release_lock(:name)", nativeQuery = true)
  void releaseLock(@Param("name") String name);

  default void executeWithLock(String lockName, String timeout, LockAction action) {
    try {
      getLock(lockName, timeout);
      action.perform();
    } finally {
      releaseLock(lockName);
    }
  }
}
