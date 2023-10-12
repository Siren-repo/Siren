package com.devlop.siren.domain.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CategoriesResponse {
    private List<CategoryResponse> categories;
}
