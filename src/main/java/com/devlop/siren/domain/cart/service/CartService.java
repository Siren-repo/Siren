package com.devlop.siren.domain.cart.service;

import com.devlop.siren.domain.cart.dto.CartDto;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.repository.UserRepository;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
  private static final String CART_KEY = "Cart - UserId";
  private final ItemRepository itemRepository;
  private final UserRepository userRepository;
  private final RedisTemplate<String, OrderItemRequest> cartRedisTemplate;
  private ListOperations<String, OrderItemRequest> listOperations;

  @PostConstruct
  private void init() {
    listOperations = cartRedisTemplate.opsForList();
  }

  private String cartKeyGenerate(Long userId) {
    return CART_KEY + ":" + userId;
  }

  public CartDto add(OrderItemRequest orderItemRequest, UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    matchAllergies(
        findItemById(orderItemRequest.getItemId()),
        userRepository.findByEmail(user.getEmail()).get());
    OrderItemRequest founded = getOrderItemRequestByKeyAndValue(key, orderItemRequest);
    if (founded != null) {
      // 상품이 이미 장바구니에 존재하면 수량 증가
      int index = findIndex(key, founded);
      founded.setQuantity(founded.getQuantity() + orderItemRequest.getQuantity());
      listOperations.set(key, index, founded);
      return new CartDto(key, List.of(founded));
    }
    // 상품이 장바구니에 없는 경우
    listOperations.rightPush(key, orderItemRequest);
    return new CartDto(key, List.of(orderItemRequest));
  }

  public CartDto retrieve(UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    return new CartDto(key, listOperations.range(key, 0, listOperations.size(key) - 1));
  }

  public void removeAll(UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    listOperations.trim(key, 1, 0);
  }

  public CartDto remove(OrderItemRequest orderItemRequest, UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    OrderItemRequest founded = getOrderItemRequestByKeyAndValue(key, orderItemRequest);
    if (founded == null) {
      throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM_IN_CART);
    }
    listOperations.remove(key, 1, founded);
    return new CartDto(key, listOperations.range(key, 0, listOperations.size(key) - 1));
  }

  public CartDto update(OrderItemRequest orderItemRequest, UserDetailsDto user) {
    String key = cartKeyGenerate(user.getId());
    OrderItemRequest founded = getOrderItemRequestByKeyAndValue(key, orderItemRequest);
    if (founded == null) {
      throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM_IN_CART);
    }
    int index = findIndex(key, founded);
    founded.setQuantity(orderItemRequest.getQuantity());
    listOperations.set(key, index, founded);
    return new CartDto(key, listOperations.range(key, 0, listOperations.size(key) - 1));
  }

  private int findIndex(String key, OrderItemRequest orderItemRequest) {
    return listOperations.range(key, 0, listOperations.size(key) - 1).indexOf(orderItemRequest);
  }

  private Item findItemById(Long itemId) {
    return itemRepository
        .findById(itemId)
        .orElseThrow(
            () -> {
              throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM);
            });
  }

  private OrderItemRequest getOrderItemRequestByKeyAndValue(
      String key, OrderItemRequest orderItemRequest) {
    List<OrderItemRequest> orderItemRequestList =
        listOperations.range(key, 0, listOperations.size(key) - 1);
    // 주어진 값과 일치하는 OrderItemRequest 찾기
    for (OrderItemRequest founded : orderItemRequestList) {
      if (orderItemRequest.equals(founded)) {
        return founded;
      }
    }
    // 일치하는 값이 없는 경우 null 반환
    return null;
  }

  private void matchAllergies(Item item, User user) {
    boolean match =
        user.getAllergies().stream()
            .anyMatch(allergyType -> item.getAllergies().contains(allergyType));
    if (match) {
      throw new GlobalException(ResponseCode.ErrorCode.CAUSE_ALLERGY_IN_CART);
    }
  }
}
