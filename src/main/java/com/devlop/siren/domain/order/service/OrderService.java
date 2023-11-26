package com.devlop.siren.domain.order.service;

import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.OrderStatus;
import com.devlop.siren.domain.order.domain.option.CustomOption;
import com.devlop.siren.domain.order.dto.response.OrderCreateResponse;
import com.devlop.siren.domain.order.repository.CustomOptionRepository;
import com.devlop.siren.domain.order.repository.OrderItemRepository;
import com.devlop.siren.domain.order.repository.OrderRepository;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.UserInformation;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final CustomOptionRepository customOptionRepository;

  @Transactional
  public void saveCustomOption(CustomOption option) {
    customOptionRepository.save(option);
  }

  @Transactional
  public OrderCreateResponse create(User user, Store store, List<OrderItem> orderItems) {
    isStoreOperating(store, LocalTime.now());
    Order newOrder = Order.of(user, store, orderItems);
    orderItems.forEach(
        orderItem -> {
          orderItem.setOrder(newOrder);
          orderItemRepository.save(orderItem);
        });
    return OrderCreateResponse.of(orderRepository.save(newOrder));
  }

  public void isStoreOperating(Store store, LocalTime now) {
    if (now.isBefore(store.getOpenTime()) || now.isAfter(store.getCloseTime())) {
      throw new GlobalException(ResponseCode.ErrorCode.NOT_OPERATING_TIME);
    }
  }

  @Transactional
  public List<OrderItem> cancel(Long orderId, UserDetailsDto userDto) {
    UserInformation.validStaff(userDto);

    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ORDER));

    if (!OrderStatus.INIT.equals(order.getStatus())) {
      throw new GlobalException(ResponseCode.ErrorCode.NOT_CANCEL_ORDER);
    }

    order.cancel();
    return order.getOrderItems();
  }
}
