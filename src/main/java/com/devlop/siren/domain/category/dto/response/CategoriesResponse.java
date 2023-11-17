package com.devlop.siren.domain.category.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoriesResponse {
    private List<CategoryResponse> categories;
}
