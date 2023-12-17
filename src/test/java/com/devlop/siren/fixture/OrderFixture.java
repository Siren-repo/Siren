package com.devlop.siren.fixture;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.option.OptionDetails.EspressoDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.PotionDetail;
import com.devlop.siren.domain.item.entity.option.OptionDetails.PotionType;
import com.devlop.siren.domain.item.entity.option.OptionDetails.SyrupDetail;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.EspressoType;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.MilkType;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup.SyrupType;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.order.domain.Order;
import com.devlop.siren.domain.order.domain.OrderItem;
import com.devlop.siren.domain.order.domain.option.BeverageOption;
import com.devlop.siren.domain.order.dto.request.CustomOptionRequest;
import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderFixture {
  public static OrderCreateRequest get(Long storeId, List<OrderItemRequest> list) {
    return new OrderCreateRequest(storeId, list);
  }

  public static Order getOrder(User user, Store store, List<OrderItem> items) {
    return Order.of(user, store, items);
  }

  public static List<OrderItemRequest> getOrderItemRequest() {
    return List.of(
        OrderItemRequest.builder()
            .itemId(1L)
            .takeout(true)
            .warm(false)
            .customOption(
                CustomOptionRequest.builder()
                    .cupSize(SizeType.TALL)
                    .milk(MilkType.LOW_FAT)
                    .espresso(new EspressoDetail(EspressoType.ORIGINAL, 1))
                    .syrups(Set.of(new SyrupDetail(SyrupType.VANILLA, 2)))
                    .build())
            .quantity(2)
            .build());
  }

  public static List<OrderItemRequest> invalidOrderItemRequest() {
    return List.of(
        OrderItemRequest.builder()
            .itemId(null) // 아이템 없음
            .takeout(false)
            .warm(true)
            .customOption(
                CustomOptionRequest.builder()
                    .cupSize(SizeType.TALL)
                    .milk(MilkType.LOW_FAT)
                    .espresso(new EspressoDetail(EspressoType.ORIGINAL, 1))
                    .syrups(Set.of(new SyrupDetail(SyrupType.VANILLA, 2)))
                    .build())
            .quantity(2)
            .build());
  }

  public static List<OrderItem> getOrderItem(Item item) {
    return List.of(
        OrderItem.create(
            item,
            BeverageOption.builder()
                .isTakeout(true)
                .isWarmed(false)
                .cupSize(SizeType.TALL)
                .milk(MilkType.LOW_FAT)
                .espresso(new EspressoDetail(EspressoType.ORIGINAL, 1))
                .drizzle(new HashSet<>())
                .syrup(Set.of(new SyrupDetail(SyrupType.VANILLA, 2)))
                .build(),
            3));
  }

  public static Store get(LocalTime open, LocalTime close) {
    return Store.builder()
        .storeId(1L)
        .city("Seoul")
        .storeId(1L)
        .storeName("강남대로신사")
        .street("서울특별시 서초구 강남대로 595")
        .zipCode("12345")
        .latitude(37.5148446)
        .storePhone("1522-3232")
        .longitude(127.0194574)
        .openTime(open)
        .closeTime(close)
        .build();
  }

  public static List<OrderItemRequest> getOrderItemOfBeverage() {
    OrderItemRequest icedCoffeeLatte = OrderItemRequest.builder().build();

    return List.of(icedCoffeeLatte);
  }

  public static List<OrderItemRequest> getOrderItemOfFood() {
    OrderItemRequest bread =
        OrderItemRequest.builder()
            .warm(true)
            .takeout(true)
            .itemId(2L)
            .customOption(
                CustomOptionRequest.builder()
                    .potions(Set.of(new PotionDetail(PotionType.BUTTER, 2)))
                    .build())
            .build();
    return List.of(bread);
  }
}
