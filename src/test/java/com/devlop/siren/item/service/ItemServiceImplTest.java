package com.devlop.siren.item.service;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.repository.DefaultOptionRepository;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.item.repository.NutritionRepository;
import com.devlop.siren.domain.item.service.ItemServiceImpl;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.fixture.ItemFixture;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import java.util.EnumSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
  @Mock private ItemRepository itemRepository;

  @Mock private DefaultOptionRepository defaultOptionRepository;

  @Mock private NutritionRepository nutritionRepository;

  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private ItemServiceImpl itemService;

  @Mock private AllergyConverter allergyConverter;

  private ItemCreateRequest validObject;
  private ItemCreateRequest inValidObject;

  @BeforeEach
  private void setUp() {
    validObject = ItemFixture.get(new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소"), 5000);
    inValidObject = ItemFixture.get(new CategoryCreateRequest(CategoryType.of("음료"), "dd"), -5);
  }

  @Test
  @DisplayName("아이템 생성, 조회에 성공한다")
  public void create() throws NoSuchFieldException, IllegalAccessException {
    // Given
    Long itemId = 1L;
    Item item = ItemFixture.get(itemId);

    // When
    when(categoryRepository.findByCategoryTypeAndCategoryName(any(), any()))
        .thenReturn(Optional.ofNullable(item.getCategory()));
    when(allergyConverter.convertToEntityAttribute(validObject.getAllergy()))
        .thenReturn(EnumSet.of(AllergyType.MILK, AllergyType.SOYBEAN));
    when(itemRepository.findByIdWithOption(itemId)).thenReturn(Optional.of(item));
    when(itemRepository.save(any(Item.class))).thenReturn(item);

    // Then
    assertThat(itemService.create(validObject).getItemName()).isEqualTo(item.getItemName());
    assertThat(itemService.findItemDetailById(itemId).getItemName()).isEqualTo(item.getItemName());
  }

  @Test
  @DisplayName("해당하는 카테고리 네임, 카테고리 타입이 없는 경우 아이템 생성을 실패한다")
  public void inValidCreate() {
    // Given
    // When
    Throwable throwable = catchThrowable(() -> itemService.create(inValidObject));
    // Then
    assertThat(throwable)
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_CATEGORY.getMESSAGE());
  }


  @Test
  @DisplayName("해당하는 카테고리 네임, 카테고리 타입이 없는 경우 카테고리별 아이템 조회를 실패한다")
  public void inValidFindAllByCategory() {
    // Given
    // When
    Throwable throwable =
        catchThrowable(
            () ->
                itemService.findAllByCategory(
                    inValidObject.getCategoryRequest().getCategoryType().getName(),
                    inValidObject.getCategoryRequest().getCategoryName(),
                    PageRequest.of(1, 1)));
    // Then
    assertThat(throwable)
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_CATEGORY.getMESSAGE());
  }

  @Test
  @DisplayName("존재하지 않는 id가 입력되는 경우 아이템 조회에 실패한다")
  public void inValidFindItemDetailById() {
    // Given
    Long itemId = 1L;
    // When
    Throwable throwable = catchThrowable(() -> itemService.findItemDetailById(itemId));
    // Then
    assertThat(throwable)
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getMESSAGE());
  }

  @Test
  @DisplayName("존재하지 않는 id가 입력되는 경우 아이템 수정에 실패한다")
  public void inValidUpdateItemById() {
    // Given
    Long itemId = 1L;

    // When
    Throwable throwable =
        catchThrowable(() -> itemService.updateItemById(itemId, validObject));

    // Then
    assertThat(throwable)
        .isInstanceOf(GlobalException.class)
        .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getMESSAGE());
  }

}
