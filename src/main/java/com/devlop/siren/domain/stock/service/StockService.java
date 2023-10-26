package com.devlop.siren.domain.stock.service;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.stock.dto.request.StockCreateRequest;
import com.devlop.siren.domain.stock.dto.response.StockResponse;
import com.devlop.siren.domain.stock.dto.response.StocksResponse;
import com.devlop.siren.domain.stock.entity.Stock;
import com.devlop.siren.domain.stock.repository.StockRepository;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.repository.StoreRepository;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode.ErrorCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.UserInformation;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {
    private final ItemRepository itemRepository;
    private final StockRepository stockRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public StockResponse create(StockCreateRequest request, UserDetailsDto user) {
        UserInformation.validStaffOrAdmin(user);
        Store store = storeRepository.findByStoreId(request.getStoreId()).orElseThrow(() -> new GlobalException(
                ErrorCode.NOT_FOUND_STORE));
        Item item = itemRepository.findById(request.getItemId()).orElseThrow(() -> new GlobalException(
                ErrorCode.NOT_FOUND_ITEM));
        Stock stock = StockCreateRequest.toEntity(request, item, store);
        return StockResponse.from(stockRepository.save(stock));
    }

    public StocksResponse findAllByStore(Long storeId, UserDetailsDto user) {
        UserInformation.validStaffOrAdmin(user);
        return new StocksResponse(stockRepository.findAllByStore(storeId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_STOCKS_IN_STORE))
                .stream()
                .map(stock -> StockResponse.from(stock))
                .collect(Collectors.toUnmodifiableList()));
    }

    public StockResponse findByStoreAndItem(Long storeId, Long itemId, UserDetailsDto user) {
        UserInformation.validStaffOrAdmin(user);
        return StockResponse.from(stockRepository.findByStoreAndItem(storeId, itemId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_STOCK_IN_STORE)));
    }

    @Transactional
    public StockResponse updateStock(Long storeId, Long itemId, Integer newStock, UserDetailsDto user) {
        UserInformation.validStaffOrAdmin(user);
        Stock stock = stockRepository.findByStoreAndItem(storeId, itemId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_STOCK_IN_STORE));
        stock.update(newStock);
        return StockResponse.from(stock);
    }

    @Transactional
    public void deleteStock(Long storeId, Long itemId, UserDetailsDto user) {
        UserInformation.validStaffOrAdmin(user);
        Stock stock = stockRepository.findByStoreAndItem(storeId, itemId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_FOUND_STOCK_IN_STORE));
        stockRepository.delete(stock);
    }
}
