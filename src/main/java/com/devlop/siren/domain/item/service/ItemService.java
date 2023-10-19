package com.devlop.siren.domain.item.service;

import com.devlop.siren.domain.category.dto.response.CategoryItemsResponse;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.response.ItemDetailResponse;
import com.devlop.siren.domain.item.dto.response.ItemResponse;
import com.devlop.siren.domain.item.dto.response.NutritionDetailResponse;

public interface ItemService {

    ItemResponse create(ItemCreateRequest request);

    CategoryItemsResponse findAllByCategory(String categoryType, String categoryName);

    ItemDetailResponse findItemDetailById(Long itemId);

    NutritionDetailResponse findNutritionDetailById(Long itemId);


    Long deleteItemById(Long itemId);

    Long updateItemById(Long itemId, ItemCreateRequest itemCreateRequest);
}
