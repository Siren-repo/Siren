package com.devlop.siren.domain.order.repository;

import com.devlop.siren.domain.order.domain.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query(
      "SELECT o FROM Order o "
          + "JOIN FETCH o.orderItems "
          + "JOIN FETCH o.store "
          + "JOIN FETCH o.user "
          + "WHERE o.id = :orderId")
  Optional<Order> findByIdWithDetails(@Param("orderId") Long orderId);
}
