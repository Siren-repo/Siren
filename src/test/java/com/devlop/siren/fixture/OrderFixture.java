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
import com.devlop.siren.domain.order.dto.request.CustomOptionRequest;
import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.store.domain.Store;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class OrderFixture {
  public static OrderCreateRequest get(Long storeId, List<OrderItemRequest> list) {
    return new OrderCreateRequest(storeId, list);
  }

  public static List<OrderItemRequest> getOrderItem(Item item) {
    return List.of(
        OrderItemRequest.builder()
            .itemId(item.getItemId())
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

  public static Store get(LocalTime open, LocalTime close) {
    return Store.builder()
        .city("Seoul")
        .storeId(1L)
        .storeName("강남대로신사")
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
