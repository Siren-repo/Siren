package com.devlop.siren.domain.order.repository;

import com.devlop.siren.domain.order.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {}
