package com.devlop.siren.domain.order.service;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.service.ItemServiceImpl;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.option.BeverageOption;
import com.devlop.siren.domain.order.domain.option.CustomOption;
import com.devlop.siren.domain.order.domain.option.FoodOption;
import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.response.OrderDetailResponse;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.service.StoreService;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.service.UserService;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderUseCase {
  private final OrderService orderService;
  private final StoreService storeService;
  private final ItemServiceImpl itemService;
  private final UserService userService;

  @Transactional
  public OrderDetailResponse create(OrderCreateRequest request, UserDetailsDto userDto) {
    Store store = storeService.findStore(request.getStoreId());
    User user = userService.findUser(userDto.getEmail());
    List<OrderItem> orderItems = getOrderItems(request);

    return orderService.create(user, store, orderItems, LocalTime.now());
  }

  private List<OrderItem> getOrderItems(OrderCreateRequest request) {
    return request.getItems().stream()
        .map(
            orderItemRequest -> {
              Item item = itemService.findItem(orderItemRequest.getItemId());
              CustomOption customOption = null;
              switch (item.getCategory().getCategoryType().name()) {
                case "BEVERAGE":
                  customOption =
                      BeverageOption.fromDto(
                          orderItemRequest.getCustomOption(),
                          orderItemRequest.getTakeout(),
                          orderItemRequest.getWarm());
                  break;
                case "FOOD":
                  customOption =
                      FoodOption.fromDto(
                          orderItemRequest.getCustomOption(),
                          orderItemRequest.getTakeout(),
                          orderItemRequest.getWarm());
                  break;
              }
              return OrderItem.create(item, customOption, orderItemRequest.getQuantity());
            })
        .collect(Collectors.toList());
  }
}
