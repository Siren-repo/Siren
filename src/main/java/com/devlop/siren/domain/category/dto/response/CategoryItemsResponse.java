package com.devlop.siren.domain.category.dto.response;

import com.devlop.siren.domain.item.dto.response.ItemResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class CategoryItemsResponse {

  private String categoryName;

  private Page<ItemResponse> items;
}
