package com.devlop.siren.order.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.option.DefaultOption;
import com.devlop.siren.domain.item.entity.option.OptionDetails;
import com.devlop.siren.domain.item.entity.option.OptionTypeGroup;
import com.devlop.siren.domain.item.entity.option.SizeType;
import com.devlop.siren.domain.item.service.ItemServiceImpl;
import com.devlop.siren.domain.order.dto.request.OrderCreateRequest;
import com.devlop.siren.domain.order.dto.request.OrderItemRequest;
import com.devlop.siren.domain.order.dto.response.OrderDetailResponse;
import com.devlop.siren.domain.order.service.OrderService;
import com.devlop.siren.domain.order.service.OrderUseCase;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.service.StoreService;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.domain.user.service.UserService;
import com.devlop.siren.fixture.ItemFixture;
import com.devlop.siren.fixture.OrderFixture;
import com.devlop.siren.fixture.UserFixture;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderUseCaseTest {
  @Mock private OrderService orderService;
  @InjectMocks private OrderUseCase orderUseCase;
  @Mock private StoreService storeService;
  @Mock private ItemServiceImpl itemService;
  @Mock private UserService userService;

  private static Store store;
  private static ItemCreateRequest itemCreateRequest;
  private static UserDetailsDto userDto;

  @BeforeEach
  void init() {
    store = OrderFixture.get(LocalTime.of(9, 0), LocalTime.of(20, 0));
    itemCreateRequest =
        ItemFixture.get(new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소"), 5000);
    userDto = UserFixture.get(UserRole.CUSTOMER);
  }

  @Test
  @DisplayName("요청한 request대로 주문을 생성한다 - 음료")
  void createOrder() {
    // given
    NutritionCreateRequest nutrition = new NutritionCreateRequest(0, 2, 3, 0, 1, 2, 2, 0, 0, 0);
    Item item =
        ItemCreateRequest.toEntity(
            itemCreateRequest,
            Category.builder().categoryName("에스프레소").categoryType(CategoryType.BEVERAGE).build(),
            new DefaultOption(
                new OptionDetails.EspressoDetail(OptionTypeGroup.EspressoType.ORIGINAL, 2),
                Set.of(new OptionDetails.SyrupDetail(OptionTypeGroup.SyrupType.VANILLA, 2)),
                OptionTypeGroup.MilkType.ORIGINAL,
                OptionTypeGroup.FoamType.MILK,
                OptionTypeGroup.DrizzleType.CHOCOLATE,
                SizeType.TALL),
            EnumSet.of(AllergyType.PEANUT, AllergyType.MILK),
            NutritionCreateRequest.toEntity(nutrition));

    List<OrderItemRequest> items = OrderFixture.getOrderItem(item);
    OrderCreateRequest request = OrderFixture.get(store.getStoreId(), items);

    when(storeService.findStore(anyLong())).thenReturn(store);
    when(userService.findUser(anyString())).thenReturn(mock(User.class));
    when(itemService.findItem(item.getItemId())).thenReturn(item);
    when(orderService.create(any(), any(), any())).thenReturn(mock(OrderDetailResponse.class));

    OrderDetailResponse response = orderUseCase.create(request, userDto);
    assertThat(response.getOrderId()).isEqualTo(0L);
  }
}