package com.devlop.siren.domain.item.controller;

import com.devlop.siren.domain.category.dto.response.CategoryItemsResponse;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.response.ItemDetailResponse;
import com.devlop.siren.domain.item.dto.response.ItemResponse;
import com.devlop.siren.domain.item.dto.response.NutritionDetailResponse;
import com.devlop.siren.domain.item.service.ItemService;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.util.UserInformation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
  private final ItemService itemService;

  @PostMapping
  public ApiResponse<ItemResponse> createItem(
      @RequestBody @Valid ItemCreateRequest itemCreateRequest,
      @AuthenticationPrincipal UserDetailsDto user) {
    UserInformation.validAdmin(user);
    return ApiResponse.ok(ResponseCode.Normal.CREATE, itemService.create(itemCreateRequest));
  }

  @GetMapping
  public ApiResponse<CategoryItemsResponse> findAllByCategory(
      @RequestParam("categoryType") @NotBlank String categoryType,
      @RequestParam("categoryName") @NotBlank String categoryName) {

    return ApiResponse.ok(
        ResponseCode.Normal.RETRIEVE, itemService.findAllByCategory(categoryType, categoryName));
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

  @DeleteMapping(value = "/{itemId}")
  public ApiResponse<?> deleteItem(
      @PathVariable @Min(1L) Long itemId, @AuthenticationPrincipal UserDetailsDto user) {
    UserInformation.validAdmin(user);
    Long id = itemService.deleteItemById(itemId);
    return ApiResponse.ok(ResponseCode.Normal.DELETE, String.format("ItemId = %d", id));
  }

  @PutMapping(value = "/{itemId}")
  public ApiResponse<?> updateItem(
      @PathVariable @Min(1L) Long itemId,
      @RequestBody @Valid ItemCreateRequest itemCreateRequest,
      @AuthenticationPrincipal UserDetailsDto user) {
    UserInformation.validAdmin(user);
    Long id = itemService.updateItemById(itemId, itemCreateRequest);
    return ApiResponse.ok(ResponseCode.Normal.UPDATE, String.format("ItemId = %d", id));
  }
}
