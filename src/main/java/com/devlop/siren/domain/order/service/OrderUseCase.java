package com.devlop.siren.domain.order.service;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.service.ItemService;
import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.option.BeverageOption;
import com.devlop.siren.domain.order.domain.option.CustomOption;
import com.devlop.siren.domain.order.domain.option.FoodOption;
import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.response.OrderCreateResponse;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.service.StoreService;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderUseCase {
    private final OrderService orderService;
    private final StoreService storeService;
    private final ItemService itemService;
    // private final StockService stock

    public OrderCreateResponse create(OrderCreateRequest request, UserDetailsDto userDto){
        Store store = storeService.findStore(request.getStoreId());
        User user = UserDetailsDto.toEntity(userDto);
        List<OrderItem> orderItems = getOrderItems(request);

        Order createdOrder = orderService.create(user, store, orderItems); // 주문 레파지토리에 저장
        orderItems.forEach(orderItem -> orderItem.setOrder(createdOrder));

        //TODO :: 카드, 결제 로직

        //TODO :: 주문처리 메시지 큐에 in

        //createdOrder.setQueueInfo(큐타입명, 들어간큐에서몇번째순서인지);
        return OrderCreateResponse.of(createdOrder);// 새로 만들어진 주문 상태
    }
    private List<OrderItem> getOrderItems(OrderCreateRequest request){
        return request.getItems().stream().map(orderItemRequest -> {
                    Item item = itemService.findItem(orderItemRequest.getItemId());
                    //stockService.checkQuantity(item, orderItemRequest.getQuantity()); // 내부에서 수량 부족으로 예외 던지도록

                    CustomOption customOption = null;
                    switch(item.getCategory().getCategoryType()){
                        case BEVERAGE:
                            customOption = BeverageOption.fromDto(orderItemRequest.getCustomOption(), orderItemRequest.getTakeout(), orderItemRequest.getWarm());
                            break;
                        case FOOD:
                            customOption = FoodOption.fromDto(orderItemRequest.getCustomOption(), orderItemRequest.getTakeout(), orderItemRequest.getWarm());
                    }

                    return OrderItem.create(item, customOption, orderItemRequest.getQuantity());
                }).collect(Collectors.toList());
    }
    public void cancel(Long orderId, UserDetailsDto userDto){
        List<OrderItem> items = orderService.cancel(orderId, userDto);
//        items.stream().map( orderItem -> {
//
//            //stockService.차감(quantity);
//        })
    }

}
