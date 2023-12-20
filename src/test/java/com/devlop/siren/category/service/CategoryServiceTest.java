package com.devlop.siren.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.domain.category.service.CategoryService;
import com.devlop.siren.global.exception.GlobalException;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private CategoryService categoryService;

  private CategoryCreateRequest validObject;

  @BeforeEach
  private void setUp() {
    validObject = new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소");
  }

  @Test
  @DisplayName("카테고리를 생성할 수 있다")
  public void register() {
    // given
    Category category = CategoryCreateRequest.toEntity(validObject);
    // when
    when(categoryRepository.findByCategoryTypeAndCategoryName(any(), any()))
        .thenReturn(Optional.empty());
    when(categoryRepository.save(any(Category.class))).thenReturn(category);
    // then
    assertThat(categoryService.register(validObject).getCategoryName())
        .isEqualTo(validObject.getCategoryName());
  }

  @Test
  @DisplayName("이미 생성된 카테고리를 중복생성할 수 없다")
  public void inValidRegister() throws NoSuchFieldException, IllegalAccessException {
    // given
    Long categoryId = 1L;
    Category category =
        Category.builder()
            .categoryType(validObject.getCategoryType())
            .categoryName(validObject.getCategoryName())
            .build();
    Field idField = Category.class.getDeclaredField("categoryId");
    idField.setAccessible(true);
    idField.set(category, categoryId);

    // when
    when(categoryRepository.findByCategoryTypeAndCategoryName(any(), any()))
        .thenReturn(Optional.ofNullable(category));
    // then
    assertThrows(GlobalException.class, () -> categoryService.register(validObject));
  }
}
