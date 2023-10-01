package com.devlop.siren.domain.item.controller;

import com.devlop.siren.domain.category.dto.response.CategoryItemsResponse;
import com.devlop.siren.global.common.response.ApiResponse;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.response.ItemDetailResponse;
import com.devlop.siren.domain.item.dto.response.ItemResponse;
import com.devlop.siren.domain.item.dto.response.NutritionDetailResponse;
import com.devlop.siren.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    // 아이템 셍성
    @PostMapping
    public ApiResponse<ItemResponse> createItem(@RequestBody @Valid ItemCreateRequest itemCreateRequest) {

        return ApiResponse.createSuccess(ResponseCode.Normal.CREATE, itemService.create(itemCreateRequest));
    }

    // 카테고리(에스프레소)에 따른 아이템 리스트 조회
    @GetMapping
    public ApiResponse<CategoryItemsResponse> findAllByCategory(
            @RequestParam("categoryType") @NotBlank String categoryType,
            @RequestParam("categoryName") @NotBlank String categoryName) {

        return ApiResponse.createSuccess(ResponseCode.Normal.RETRIEVE, itemService.findAllByCategory(categoryType, categoryName));
    }

    // 아이템 상세 조회
    @GetMapping(value = "/{itemId}")
    public ApiResponse<ItemDetailResponse> findItemDetail(@PathVariable @Min(1L) Long itemId) {
        return ApiResponse.createSuccess(ResponseCode.Normal.RETRIEVE, itemService.findItemDetailById(itemId));
    }

    // 아이템에 따른 영양성분 조회
    @GetMapping(value = "/{itemId}/nutrition")
    public ApiResponse<NutritionDetailResponse> findNutritionDetail(@PathVariable @Min(1L) Long itemId) {
        return ApiResponse.createSuccess(ResponseCode.Normal.RETRIEVE, itemService.findNutritionDetailById(itemId));
    }

    // 아이템 삭제
    @DeleteMapping(value = "/{itemId}")
    public ApiResponse<?> deleteItem(@PathVariable @Min(1L) Long itemId) {
        Long id = itemService.deleteItemById(itemId);
        return ApiResponse.createSuccess(ResponseCode.Normal.DELETE, String.format("ItemId = %d", id));
    }

    // 아이템 수정
    @PutMapping(value = "/{itemId}")
    public ApiResponse<?> updateItem(@PathVariable @Min(1L) Long itemId, @RequestBody @Valid ItemCreateRequest itemCreateRequest) {
        Long id = itemService.updateItemById(itemId, itemCreateRequest);
        return ApiResponse.createSuccess(ResponseCode.Normal.UPDATE, String.format("ItemId = %d", id));
    }


}
