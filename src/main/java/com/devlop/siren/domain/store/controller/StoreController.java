package com.devlop.siren.domain.store.controller;

import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.store.dto.request.StoreRegisterRequest;
import com.devlop.siren.domain.store.dto.request.StoreUpdateRequest;
import com.devlop.siren.domain.store.dto.response.StoreResponse;
import com.devlop.siren.domain.store.service.StoreService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.Permission;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
  private final StoreService storeService;

  @Permission(role = UserRole.ADMIN)
  @PostMapping
  public ApiResponse<Void> registerStore(
      @RequestBody @Valid StoreRegisterRequest storeRegisterRequest) {
    storeService.registerStore(storeRegisterRequest);
    return ApiResponse.ok(ResponseCode.Normal.CREATE, null);
  }

  @GetMapping("/details/{storeId}")
  public ApiResponse<StoreResponse> detailsStore(@PathVariable("storeId") Long storeId) {
    return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, storeService.detailsStore(storeId));
  }

  @GetMapping("/all")
  public ApiResponse<List<Store>> getAllStore() {
    return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, storeService.getAllStores());
  }

  @GetMapping("/nearby")
  public ApiResponse<List<Store>> findNearbyStores(
      @RequestParam Double latitude,
      @RequestParam Double longitude,
      @RequestParam Double radiusKm) {
    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE, storeService.getNearbyStores(latitude, longitude, radiusKm));
  }

  @Permission(role = UserRole.ADMIN)
  @PutMapping("/{storeId}")
  public ApiResponse<?> updateStore(
      @PathVariable("storeId") Long storeId, @RequestBody StoreUpdateRequest storeUpdateRequest) {
    storeService.updateStore(storeId, storeUpdateRequest);
    return ApiResponse.ok(ResponseCode.Normal.UPDATE, String.format("updateId > %d", storeId));
  }

  @Permission(role = UserRole.ADMIN)
  @DeleteMapping("/{storeId}")
  public ApiResponse<?> deleteStore(@PathVariable("storeId") Long storeId) {
    Long deleteId = storeService.deleteStore(storeId);
    return ApiResponse.ok(ResponseCode.Normal.DELETE, String.format("삭제 된 ID > %d", deleteId));
  }
}
