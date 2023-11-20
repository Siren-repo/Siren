package com.devlop.siren.domain.category.dto.request;


import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {
    @NotNull(message = "아이템 타입이 선택되지 않았습니다.")
    private CategoryType categoryType;

    @NotBlank(message = "아이템 카테고리가 입력되지 않았습니다.")
    private String categoryName;

    public static Category toEntity(CategoryCreateRequest request){
        return Category.builder()
                .categoryName(request.getCategoryName())
                .categoryType(request.getCategoryType()).build();
    }

}
