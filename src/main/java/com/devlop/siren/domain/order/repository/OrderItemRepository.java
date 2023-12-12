package com.devlop.siren.domain.order.repository;

import com.devlop.siren.domain.order.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
