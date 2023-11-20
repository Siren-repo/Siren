package com.devlop.siren.category.service;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.domain.category.service.CategoryService;
import com.devlop.siren.domain.user.domain.UserRole;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private CategoryCreateRequest validObject;
    private UserDetailsDto staff;
    private UserDetailsDto customer;

    @BeforeEach
    private void setUp() {
        validObject = new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소");
        staff = new UserDetailsDto(1L, "test@test.com", "12345678", UserRole.ADMIN, false);
        customer = new UserDetailsDto(2L, "test1@test.com", "12345678", UserRole.CUSTOMER, false);
    }

    @Test
    @DisplayName("권한이 있는 사용자는 카테고리를 생성할 수 있다")
    public void register() {
        //given
        Category category = CategoryCreateRequest.toEntity(validObject);
        //when
        when(categoryRepository.findByCategoryTypeAndCategoryName(any(), any())).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        //then
        assertThat(categoryService.register(validObject, staff).getCategoryName()).isEqualTo(validObject.getCategoryName());
    }

    @Test
    @DisplayName("권한이 없는 사용자는 카테고리를 생성할 수 없다")
    public void failRegister() {
        //given
        Throwable throwable = catchThrowable(() -> categoryService.register(validObject, customer));
        // Then
        AssertionsForClassTypes.assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_AUTHORITY_USER.getMESSAGE());
    }



    @Test
    @DisplayName("이미 생성된 카테고리를 중복생성할 수 없다")
    public void inValidRegister() {
        //given
        Long categoryId = 1L;
        Category category = Category.builder().categoryId(categoryId).categoryType(validObject.getCategoryType()).categoryName(validObject.getCategoryName()).build();
        //when
        when(categoryRepository.findByCategoryTypeAndCategoryName(any(), any())).thenReturn(Optional.ofNullable(category));
        //then
        assertThrows(GlobalException.class, () -> categoryService.register(validObject, staff));
    }

}