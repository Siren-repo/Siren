package com.devlop.siren.domain.category.service;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.dto.response.CategoriesResponse;
import com.devlop.siren.domain.category.dto.response.CategoryResponse;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.global.exception.DuplicateEntityException;
import com.devlop.siren.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 카테고리 등록
    @Transactional
    public CategoryResponse register(CategoryCreateRequest request) {

        validateDuplicateCategory(request.getCategoryType(), request.getCategoryName());

        Category category = Category.builder()
                .categoryName(request.getCategoryName())
                .categoryType(request.getCategoryType()).build();
        return CategoryResponse.from(categoryRepository.save(category));
    }

    protected void validateDuplicateCategory(CategoryType categoryType, String categoryName) {
        categoryRepository.findByCategoryTypeAndCategoryName(categoryType, categoryName)
                .ifPresent(o -> {throw new DuplicateEntityException();});
    }


    // 카테고리 타입별 모든 카테고리 이름 출력
    public CategoriesResponse findAllByType(CategoryType categoryType) {
        List<CategoryResponse> categoryResponses = categoryRepository.findByCategoryTypeOrderByCategoryId(categoryType)
                .orElseThrow(() -> new EntityNotFoundException())
                .stream()
                .map(category -> CategoryResponse.from(category))
                .collect(Collectors.toUnmodifiableList());

        return new CategoriesResponse(categoryResponses);
    }
}
