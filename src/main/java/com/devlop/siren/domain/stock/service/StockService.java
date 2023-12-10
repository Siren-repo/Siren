package com.devlop.siren.domain.stock.service;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.stock.dto.request.StockCreateRequest;
import com.devlop.siren.domain.stock.dto.response.StockResponse;
import com.devlop.siren.domain.stock.entity.Stock;
import com.devlop.siren.domain.stock.repository.StockRepository;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.global.common.response.ResponseCode.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {
  private final ItemRepository itemRepository;
  private final StockRepository stockRepository;
  private final StoreRepository storeRepository;

  private Item findByItemId(Long itemId) {
    return itemRepository
        .findById(itemId)
        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_ITEM));
  }

  private Store findByStoreId(Long storeId) {
    return storeRepository
        .findByStoreId(storeId)
        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_STORE));
  }

  private Stock findStoreAndItem(Long storeId, Long itemId) {
    return stockRepository
        .findByStoreAndItem(storeId, itemId)
        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_STOCK_IN_STORE));
  }

  @Transactional
  public StockResponse create(StockCreateRequest request) {
    Store store = findByStoreId(request.getStoreId());
    Item item = findByItemId(request.getItemId());
    Stock stock = StockCreateRequest.toEntity(request, item, store);
    return StockResponse.from(stockRepository.save(stock));
  }

  public Page<StockResponse> findAllByStore(Long storeId, Pageable pageable) {
    Store store = findByStoreId(storeId);
    PageRequest pageRequest =
        PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), Sort.by("stockId").descending());
    return Optional.of(stockRepository.findAllByStore(store, pageRequest).map(StockResponse::from))
        .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_STOCKS_IN_STORE));
  }

  public StockResponse findByStoreAndItem(Long storeId, Long itemId) {
    return StockResponse.from(findStoreAndItem(storeId, itemId));
  }

  @Transactional
  public StockResponse update(Long storeId, Long itemId, Integer newStock) {
    Stock stock = findStoreAndItem(storeId, itemId);
    stock.update(newStock);
    return StockResponse.from(stock);
  }

  @Transactional
  public void delete(Long storeId, Long itemId) {
    Stock stock = findStoreAndItem(storeId, itemId);
    stockRepository.delete(stock);
  }
}
