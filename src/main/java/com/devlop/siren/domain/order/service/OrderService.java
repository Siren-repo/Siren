package com.devlop.siren.domain.order.service;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.OrderStatus;
import com.devlop.siren.domain.order.dto.request.OrderStatusRequest;
import com.devlop.siren.domain.order.dto.response.OrderDetailResponse;
import com.devlop.siren.domain.order.repository.CustomOptionRepository;
import com.devlop.siren.domain.order.repository.OrderItemRepository;
import com.devlop.siren.domain.order.repository.OrderLockRepository;
import com.devlop.siren.domain.order.repository.OrderRepository;
import com.devlop.siren.domain.order.repository.StockLockRepository;
import com.devlop.siren.domain.stock.repository.StockRepository;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.common.response.ResponseCode.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.UserInformation;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final CustomOptionRepository customOptionRepository;
  private final StockRepository stockRepository;
  private final StockLockRepository stockLockRepository;
  private final OrderLockRepository orderLockRepository;

  static final String LOCK_TIME_OUT = "3000";

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public OrderDetailResponse create(
      User user, Store store, List<OrderItem> orderItems, LocalTime orderTime) {
    isStoreOperating(store, orderTime);
    Order newOrder = Order.of(user, store, orderItems);

    orderItems.forEach(
        orderItem -> {
          customOptionRepository.save(orderItem.getCustomOption());
          stockLockRepository.executeWithLock(
              getNamedLockNameWithStock(orderItem.getItem(), store),
              LOCK_TIME_OUT,
              () -> {
                consumeStock(
                    orderItem.getQuantity(), store.getStoreId(), orderItem.getItem().getItemId());
              });
          orderItem.setOrder(newOrder);
          orderItemRepository.save(orderItem);
        });
    return OrderDetailResponse.of(orderRepository.save(newOrder));
  }

  @Transactional
  public OrderDetailResponse cancel(Long orderId, UserDetailsDto userDto) {
    UserInformation.validStaffOrAdmin(userDto);
    Order order = findByOrderId(orderId);

    if (!OrderStatus.INIT.equals(order.getStatus())) {
      throw new GlobalException(ResponseCode.ErrorCode.ALREADY_ORDERED);
    }

    Store orderedStore = order.getStore();

    order.getOrderItems().stream()
        .forEach(
            orderItem -> {
              Item item = orderItem.getItem();
              stockLockRepository.executeWithLock(
                  getNamedLockNameWithStock(item, orderedStore),
                  LOCK_TIME_OUT,
                  () -> {
                    revertStock(
                        orderItem.getQuantity(),
                        orderedStore.getStoreId(),
                        orderItem.getItem().getItemId());
                  });
            });

    orderLockRepository.executeWithLock(
        getNamedLockNameWithOrder(order),
        LOCK_TIME_OUT,
        () -> {
          order.cancel();
        });

    return OrderDetailResponse.of(order);
  }

  @Transactional
  public OrderDetailResponse updateStatus(OrderStatusRequest request, UserDetailsDto userDto) {
    UserInformation.validStaffOrAdmin(userDto);
    Order order = findByOrderId(request.getOrderId());

    if (OrderStatus.COMPLETED.equals(order.getStatus())) {
      throw new GlobalException(ErrorCode.ALREADY_COMPLETED_ORDER);
    }

    orderLockRepository.executeWithLock(
        getNamedLockNameWithOrder(order),
        LOCK_TIME_OUT,
        () -> order.setStatus(request.getStatus()));

    return OrderDetailResponse.of(order);
  }

  private String getNamedLockNameWithStock(Item item, Store store) {
    return String.format("order-%s-%s", store.getStoreId().toString(), item.getItemId().toString());
  }

  private String getNamedLockNameWithOrder(Order order) {
    return String.format("order-%s", order.getId().toString());
  }

  private void isStoreOperating(Store store, LocalTime now) {
    if (now.isBefore(store.getOpenTime()) || now.isAfter(store.getCloseTime())) {
      throw new GlobalException(ResponseCode.ErrorCode.NOT_OPERATING_TIME);
    }
  }

  private void consumeStock(Integer quantity, Long storeId, Long itemId) {
    stockRepository
        .findByStoreAndItem(storeId, itemId)
        .ifPresent(stock -> stock.consumed(quantity));
  }

  private void revertStock(Integer quantity, Long storeId, Long itemId) {
    stockRepository.findByStoreAndItem(storeId, itemId).ifPresent(stock -> stock.revert(quantity));
  }

  private Order findByOrderId(Long orderId) {
    return orderRepository
        .findById(orderId)
        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ORDER));
  }
}
