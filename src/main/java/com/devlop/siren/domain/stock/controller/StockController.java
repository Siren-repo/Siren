package com.devlop.siren.domain.stock.controller;

import com.devlop.siren.domain.stock.dto.request.StockCreateRequest;
import com.devlop.siren.domain.stock.dto.response.StockResponse;
import com.devlop.siren.domain.stock.service.StockService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Validated
public class StockController {
    private final StockService stockService;

    // 재고를 생성
    @PostMapping
    public ApiResponse<StockResponse> createStock(@RequestBody @Valid StockCreateRequest stockCreateRequest,
                                                  @AuthenticationPrincipal UserDetailsDto user) {
        return ApiResponse.ok(ResponseCode.Normal.CREATE, stockService.create(stockCreateRequest, user));
    }

    // 매장별 모든 재고를 조회
    @GetMapping(value = "/{storeId}")
    public ApiResponse<Page<StockResponse>> findAllByStore(@PathVariable @Min(1L) Long storeId,
                                                           @AuthenticationPrincipal UserDetailsDto user,
                                                           @PageableDefault(size = 5, sort = "stockId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, stockService.findAllByStore(storeId, user, pageable));
    }

    // 매장별 특정 상품 재고를 조회
    @GetMapping(value = "/{storeId}/{itemId}")
    public ApiResponse<StockResponse> findStockDetail(@PathVariable @Min(1L) Long storeId,
                                                      @PathVariable @Min(1L) Long itemId,
                                                      @AuthenticationPrincipal UserDetailsDto user) {
        return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, stockService.findByStoreAndItem(storeId, itemId, user));
    }

    // 재고를 수정
    @PutMapping(value = "/{storeId}/{itemId}")
    public ApiResponse<StockResponse> updateStock(@PathVariable @Min(1L) Long storeId,
                                                  @PathVariable @Min(1L) Long itemId,
                                                  @RequestParam @NotNull @Min(0) Integer stock,
                                                  @AuthenticationPrincipal UserDetailsDto user) {
        return ApiResponse.ok(ResponseCode.Normal.UPDATE, stockService.updateStock(storeId, itemId, stock, user));
    }

    // 재고를 삭제
    @DeleteMapping(value = "/{storeId}/{itemId}")
    public ApiResponse<?> deleteStock(@PathVariable @Min(1L) Long storeId,
                                      @PathVariable @Min(1L) Long itemId,
                                      @AuthenticationPrincipal UserDetailsDto user) {
        stockService.deleteStock(storeId, itemId, user);
        return ApiResponse.ok(ResponseCode.Normal.DELETE, String.format("StoreId = %d ItemId = %d", storeId, itemId));
    }
}
