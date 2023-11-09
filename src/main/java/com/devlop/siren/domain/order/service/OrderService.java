package com.devlop.siren.domain.order.service;

import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.OrderStatus;
import com.devlop.siren.domain.order.repository.OrderRepository;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.UserInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    @Transactional
    public Order create(User user, Store store, List<OrderItem> orderItems){
        isStoreOperating(store, LocalDateTime.now());
        Order newOrder = Order.of(user, store, orderItems);
        return orderRepository.save(newOrder);
    }
    private void isStoreOperating(Store store, LocalDateTime now){
        if(store.getOpenTime().isBefore(now) || store.getCloseTime().isAfter(now)){
            throw new GlobalException(ResponseCode.ErrorCode.NOT_OPERATING_TIME);
        }
    }
    @Transactional
    public List<OrderItem> cancel(Long orderId, UserDetailsDto userDto){
        UserInformation.validStaff(userDto);

        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ORDER));

        if(!OrderStatus.INIT.equals(order.getStatus())){
            throw new GlobalException(ResponseCode.ErrorCode.NOT_CANCEL_ORDER);
        }

        order.cancel();
        return order.getOrderItems();
    }

}
