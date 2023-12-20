package com.devlop.siren.stock.service;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.stock.dto.request.StockCreateRequest;
import com.devlop.siren.domain.stock.entity.Stock;
import com.devlop.siren.domain.stock.repository.StockRepository;
import com.devlop.siren.domain.stock.service.StockService;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.fixture.ItemFixture;
import com.devlop.siren.fixture.StoreFixture;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.common.response.ResponseCode.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

  @Mock private ItemRepository itemRepository;
  @Mock private StoreRepository storeRepository;
  @Mock private StockRepository stockRepository;
  @InjectMocks private StockService stockService;
  private static final Long STORE_ID = 1L;
  private static final Long ITEM_ID = 1L;
  private StockCreateRequest validStockDto;
  private StockCreateRequest inValidStoreInStockDto;
  private StockCreateRequest inValidItemInStockDto;
  private Store store;
  private Item item;

  @BeforeEach
  private void setUp() throws NoSuchFieldException, IllegalAccessException {
    validStockDto = new StockCreateRequest(STORE_ID, ITEM_ID, 1);
    inValidItemInStockDto = new StockCreateRequest(STORE_ID, 0L, -1);
    inValidStoreInStockDto = new StockCreateRequest(0L, ITEM_ID, -1);
    store = StoreFixture.get(STORE_ID);
    item = ItemFixture.get(ITEM_ID);
  }

  @Test
  @DisplayName("재고 생성에 성공한다")
  void createStock() {
    Stock stock = new Stock(item, store, 1);
    when(storeRepository.findByStoreId(STORE_ID)).thenReturn(Optional.ofNullable(store));
    when(itemRepository.findById(ITEM_ID)).thenReturn(Optional.ofNullable(item));
    when(stockRepository.save(any(Stock.class))).thenReturn(stock);

    assertThat(stockService.create(validStockDto).getStock()).isEqualTo(validStockDto.getStock());
  }

  @Test
  @DisplayName("해당하는 아이템이 없는 경우 재고 생성을 실패한다")
  void inValidItemToCreateStock() {
    // Given
    // When
    when(storeRepository.findByStoreId(STORE_ID)).thenReturn(Optional.ofNullable(store));
    Throwable throwable = catchThrowable(() -> stockService.create(inValidItemInStockDto));
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
    Throwable throwable = catchThrowable(() -> stockService.create(inValidStoreInStockDto));
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
    Throwable throwable =
        catchThrowable(
            () ->
                stockService.findAllByStore(
                    inValidStoreInStockDto.getStoreId(), PageRequest.of(1, 1)));
    // Then
    assertThat(throwable)
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ErrorCode.NOT_FOUND_STORE.getMESSAGE());
  }

  @Test
  @DisplayName("존재하지 않는 아이템 id가 입력되는 경우 매장 재고 조회에 실패한다")
  void inValidFindByStoreAndItem() {
    // Given
    // When
    Throwable throwable =
        catchThrowable(
            () -> stockService.findByStoreAndItem(STORE_ID, inValidItemInStockDto.getItemId()));
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
    Throwable throwable =
        catchThrowable(
            () ->
                stockService.update(
                    inValidStoreInStockDto.getStoreId(), ITEM_ID, validStockDto.getStock()));
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
    Throwable throwable =
        catchThrowable(
            () ->
                stockService.update(
                    STORE_ID, inValidItemInStockDto.getItemId(), validStockDto.getStock()));
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
    Throwable throwable =
        catchThrowable(() -> stockService.delete(inValidStoreInStockDto.getStoreId(), ITEM_ID));
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
    Throwable throwable =
        catchThrowable(() -> stockService.delete(STORE_ID, inValidItemInStockDto.getItemId()));
    // Then
    assertThat(throwable)
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_STOCK_IN_STORE.getMESSAGE());
  }
}
