package com.devlop.siren.domain.item.service;

import com.devlop.siren.domain.category.dto.response.CategoryItemsResponse;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.response.ItemDetailResponse;
import com.devlop.siren.domain.item.dto.response.ItemResponse;
import com.devlop.siren.domain.item.dto.response.NutritionDetailResponse;
import com.devlop.siren.domain.item.entity.Item;
import org.springframework.data.domain.Pageable;

public interface ItemService {

  ItemResponse create(ItemCreateRequest request);

  Item findItem(Long itemId);

  CategoryItemsResponse findAllByCategory(
      String categoryType, String categoryName, Pageable pageable);

  ItemDetailResponse findItemDetailById(Long itemId);

  NutritionDetailResponse findNutritionDetailById(Long itemId);

  Long deleteItemById(Long itemId);

  Long updateItemById(Long itemId, ItemCreateRequest itemCreateRequest);
}
