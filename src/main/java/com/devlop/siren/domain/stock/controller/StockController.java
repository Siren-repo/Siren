package com.devlop.siren.domain.stock.controller;

import com.devlop.siren.domain.stock.dto.request.StockCreateRequest;
import com.devlop.siren.domain.stock.dto.response.StockResponse;
import com.devlop.siren.domain.stock.service.StockService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.Permission;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@Validated
public class StockController {
  private final StockService stockService;

  // 재고를 생성
  @Permission(role = {UserRole.ADMIN, UserRole.STAFF})
  @PostMapping
  public ApiResponse<StockResponse> create(
      @RequestBody @Valid StockCreateRequest stockCreateRequest) {
    return ApiResponse.ok(ResponseCode.Normal.CREATE, stockService.create(stockCreateRequest));
  }

  // 매장별 모든 재고를 조회
  @Permission(role = {UserRole.ADMIN, UserRole.STAFF})
  @GetMapping(value = "/{storeId}")
  public ApiResponse<Page<StockResponse>> findAllByStore(
      @PathVariable @Min(1L) Long storeId,
      @PageableDefault(sort = "stockId", direction = Sort.Direction.DESC) Pageable pageable) {
    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE, stockService.findAllByStore(storeId, pageable));
  }

  // 매장별 특정 상품 재고를 조회
  @Permission(role = {UserRole.ADMIN, UserRole.STAFF})
  @GetMapping(value = "/{storeId}/{itemId}")
  public ApiResponse<StockResponse> findStockDetail(
      @PathVariable @Min(1L) Long storeId, @PathVariable @Min(1L) Long itemId) {
    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE, stockService.findByStoreAndItem(storeId, itemId));
  }

  // 재고를 수정
  @Permission(role = {UserRole.ADMIN, UserRole.STAFF})
  @PutMapping(value = "/{storeId}/{itemId}")
  public ApiResponse<StockResponse> update(
      @PathVariable @Min(1L) Long storeId,
      @PathVariable @Min(1L) Long itemId,
      @RequestParam @NotNull @Min(0) Integer stock) {
    return ApiResponse.ok(ResponseCode.Normal.UPDATE, stockService.update(storeId, itemId, stock));
  }

  // 재고를 삭제
  @Permission(role = {UserRole.ADMIN, UserRole.STAFF})
  @DeleteMapping(value = "/{storeId}/{itemId}")
  public ApiResponse<?> delete(
      @PathVariable @Min(1L) Long storeId, @PathVariable @Min(1L) Long itemId) {
    stockService.delete(storeId, itemId);
    return ApiResponse.ok(
        ResponseCode.Normal.DELETE, String.format("StoreId = %d ItemId = %d", storeId, itemId));
  }
}
