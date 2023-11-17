package com.devlop.siren.domain.category.dto.response;

import com.devlop.siren.domain.item.dto.response.ItemResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryItemsResponse {

    private String categoryName;

    private List<ItemResponse> items;

}