package com.devlop.siren.stock.service;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.SizeType;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.domain.stock.dto.request.StockCreateRequest;
import com.devlop.siren.domain.stock.entity.Stock;
import com.devlop.siren.domain.stock.repository.StockRepository;
import com.devlop.siren.domain.stock.service.StockService;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.common.response.ResponseCode.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.UserInformation;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StockRepository stockRepository;
    @InjectMocks
    private StockService stockService;
    @Mock
    private AllergyConverter allergyConverter;
    private static final Long STORE_ID = 1L;
    private static final Long ITEM_ID = 1L;
    private StockCreateRequest validStockDto;
    private StockCreateRequest inValidStoreInStockDto;
    private StockCreateRequest inValidItemInStockDto;
    private MockedStatic<UserInformation> userInformationMock;
    private Store store;
    private Item item;
    private ItemCreateRequest validItemDto;
    private UserDetailsDto staff;


    @BeforeEach
    private void setUp() {
        staff = new UserDetailsDto(1L, "test@test.com", "testtest", UserRole.ADMIN, false);
        validStockDto = new StockCreateRequest(STORE_ID, ITEM_ID, 1);
        inValidItemInStockDto = new StockCreateRequest(STORE_ID, 0L, -1);
        inValidStoreInStockDto = new StockCreateRequest(0L, ITEM_ID, -1);
        userInformationMock = mockStatic(UserInformation.class);
        validItemDto = new ItemCreateRequest(new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소")
                , "아메리카노"
                , 5000, "아메리카노입니다", null, false, true,
                new DefaultOptionCreateRequest(2, 0, 0, 0, SizeType.of("Tall")), "우유, 대두",
                new NutritionCreateRequest(0, 2, 3, 0, 1, 2, 2, 0, 0, 0));
        store = Store.builder()
                .storeId(STORE_ID)
                .storeName("First Store Name")
                .storePhone("First Store Phone")
                .city("Seoul")
                .street("대전 서구 둔산중로32번길 29 1층 103호")
                .zipCode(54321)
                .openTime(LocalDateTime.of(2023, 9, 25, 18, 0))
                .closeTime(LocalDateTime.of(2023, 9, 25, 9, 0))
                .build();
        item = Item.builder()
                .itemId(ITEM_ID)
                .itemName(validItemDto.getItemName())
                .price(validItemDto.getPrice())
                .image(null)
                .category(Category.builder()
                        .categoryName(validItemDto.getCategoryRequest().getCategoryName())
                        .categoryType(validItemDto.getCategoryRequest().getCategoryType()).build())
                .defaultOption(DefaultOptionCreateRequest.toEntity(validItemDto.getDefaultOptionRequest()))
                .description(validItemDto.getDescription())
                .isNew(validItemDto.getIsNew())
                .isBest(validItemDto.getIsBest())
                .allergies(allergyConverter.convertToEntityAttribute(validItemDto.getAllergy())).build();
    }

    @AfterEach
    private void cleanUp() {
        userInformationMock.close();
    }

    @Test
    @DisplayName("재고 생성에 성공한다")
    void createStock() {
        Long stockId = 1L;
        Stock stock = new Stock(stockId, item, store, 1);
        when(storeRepository.findByStoreId(STORE_ID)).thenReturn(Optional.ofNullable(store));
        when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.ofNullable(item));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        assertThat(stockService.create(validStockDto, staff).getStockId()).isEqualTo(stockId);
    }

    @Test
    @DisplayName("해당하는 아이템이 없는 경우 재고 생성을 실패한다")
    void inValidItemToCreateStock() {
        // Given
        // When
        when(storeRepository.findByStoreId(STORE_ID)).thenReturn(Optional.ofNullable(store));
        Throwable throwable = catchThrowable(() -> stockService.create(inValidItemInStockDto, staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_ITEM.getMESSAGE());

    }

    @Test
    @DisplayName("해당하는 매장이 없는 경우 재고 생성을 실패한다")
    void inValidStoreToCreateStock() {
        // Given
        // When
        Throwable throwable = catchThrowable(() -> stockService.create(inValidStoreInStockDto, staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_STORE.getMESSAGE());

    }

    @Test
    @DisplayName("존재하지 않는 매장 id가 입력되는 경우 매장 재고 조회에 실패한다")
    void inValidFindAllByStore() {
        // Given
        // When
        Throwable throwable = catchThrowable(
                () -> stockService.findAllByStore(inValidStoreInStockDto.getStoreId(), staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_STOCKS_IN_STORE.getMESSAGE());
    }

    @Test
    @DisplayName("존재하지 않는 아이템 id가 입력되는 경우 매장 재고 조회에 실패한다")
    void inValidFindByStoreAndItem() {
        // Given
        // When
        Throwable throwable = catchThrowable(() -> stockService.findByStoreAndItem(STORE_ID,
                inValidItemInStockDto.getItemId(), staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_STOCK_IN_STORE.getMESSAGE());
    }

    @Test
    @DisplayName("존재하지 않는 매장 id가 입력되는 경우 매장 재고 수정에 실패한다")
    void inValidStoreUpdateStock() {
        // Given
        // When
        Throwable throwable = catchThrowable(
                () -> stockService.updateStock(inValidStoreInStockDto.getStoreId(), ITEM_ID, validStockDto.getStock(),
                        staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_STOCK_IN_STORE.getMESSAGE());
    }

    @Test
    @DisplayName("존재하지 않는 아이템 id가 입력되는 경우 매장 재고 수정에 실패한다")
    void inValidItemUpdateStock() {
        // Given
        // When
        Throwable throwable = catchThrowable(
                () -> stockService.updateStock(STORE_ID, inValidItemInStockDto.getItemId(), validStockDto.getStock(),
                        staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_STOCK_IN_STORE.getMESSAGE());
    }

    @Test
    @DisplayName("존재하지 않는 매장 id가 입력되는 경우 매장 재고 삭제에 실패한다")
    void inValidStoreDeleteStock() {
        // Given
        // When
        Throwable throwable = catchThrowable(
                () -> stockService.deleteStock(inValidStoreInStockDto.getStoreId(), ITEM_ID, staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_STOCK_IN_STORE.getMESSAGE());
    }

    @Test
    @DisplayName("존재하지 않는 아이템 id가 입력되는 경우 매장 재고 삭제에 실패한다")
    void inValidItemDeleteStock() {
        // Given
        // When
        Throwable throwable = catchThrowable(
                () -> stockService.deleteStock(STORE_ID, inValidItemInStockDto.getItemId(), staff));
        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_STOCK_IN_STORE.getMESSAGE());
    }

}