package com.devlop.siren.order.service;

import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.order.repository.OrderRepository;
import com.devlop.siren.domain.order.service.OrderService;
import com.devlop.siren.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
  @InjectMocks private OrderService orderService;
  @Mock private OrderRepository orderRepository;
  @Mock private StockRepository stockRepository;
  @Mock private ItemRepository itemRepository;

  @Test
  @DisplayName("주문 생성 성공")
  void createOrder() {}

  @Test
  @DisplayName("재고 부족으로 주문 생성 실패")
  void createOrderWithNotConsumeStock() {}
}
