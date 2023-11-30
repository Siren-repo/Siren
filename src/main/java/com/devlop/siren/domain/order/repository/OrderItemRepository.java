package com.devlop.siren.domain.order.repository;

import com.devlop.siren.domain.order.domain.OrderItem;
import org.springframework.data.repository.CrudRepository;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {}
