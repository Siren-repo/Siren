package com.devlop.siren.domain.item.controller;

import com.devlop.siren.domain.category.dto.response.CategoryItemsResponse;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.response.ItemDetailResponse;
import com.devlop.siren.domain.item.dto.response.ItemResponse;
import com.devlop.siren.domain.item.dto.response.NutritionDetailResponse;
import com.devlop.siren.domain.item.service.ItemService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.Permission;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
  private final ItemService itemService;

  @Permission(role = {UserRole.ADMIN})
  @PostMapping
  public ApiResponse<ItemResponse> createItem(
      @RequestBody @Valid ItemCreateRequest itemCreateRequest) {
    return ApiResponse.ok(ResponseCode.Normal.CREATE, itemService.create(itemCreateRequest));
  }

  @GetMapping
  public ApiResponse<CategoryItemsResponse> findAllByCategory(
      @RequestParam("categoryType") @NotBlank String categoryType,
      @RequestParam("categoryName") @NotBlank String categoryName,
      @PageableDefault(sort = "itemId", direction = Sort.Direction.DESC) Pageable pageable) {

    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE,
        itemService.findAllByCategory(categoryType, categoryName, pageable));
  }

  @GetMapping(value = "/{itemId}")
  public ApiResponse<ItemDetailResponse> findItemDetail(@PathVariable @Min(1L) Long itemId) {
    return ApiResponse.ok(ResponseCode.Normal.RETRIEVE, itemService.findItemDetailById(itemId));
  }

  @GetMapping(value = "/{itemId}/nutrition")
  public ApiResponse<NutritionDetailResponse> findNutritionDetail(
      @PathVariable @Min(1L) Long itemId) {
    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE, itemService.findNutritionDetailById(itemId));
  }

  @Permission(role = {UserRole.ADMIN})
  @DeleteMapping(value = "/{itemId}")
  public ApiResponse<?> deleteItem(@PathVariable @Min(1L) Long itemId) {
    return ApiResponse.ok(ResponseCode.Normal.DELETE, String.format("ItemId = %d", itemId));
  }

  @Permission(role = {UserRole.ADMIN})
  @PutMapping(value = "/{itemId}")
  public ApiResponse<?> updateItem(
      @PathVariable @Min(1L) Long itemId, @RequestBody @Valid ItemCreateRequest itemCreateRequest) {
    return ApiResponse.ok(
        ResponseCode.Normal.UPDATE,
        String.format("ItemId = %d", itemService.updateItemById(itemId, itemCreateRequest)));
  }
}
