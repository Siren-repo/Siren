package com.devlop.siren.domain.category.dto.response;

import com.devlop.siren.domain.item.dto.response.ItemResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoryItemsResponse {

    private String categoryName;

    private List<ItemResponse> items;

}