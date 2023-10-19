package com.devlop.siren.item.service;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.SizeType;
import com.devlop.siren.domain.item.repository.DefaultOptionRepository;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.item.repository.NutritionRepository;
import com.devlop.siren.domain.item.service.ItemServiceImpl;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private DefaultOptionRepository defaultOptionRepository;

    @Mock
    private NutritionRepository nutritionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private AllergyConverter allergyConverter;

    private static ItemCreateRequest validObject;
    private static ItemCreateRequest inValidObject;


    @BeforeAll
    private static void setUp() {
        validObject = new ItemCreateRequest(new CategoryCreateRequest(CategoryType.of("음료"), "에스프레소")
                , "아메리카노"
                , 5000, "아메리카노입니다", null, false, true,
                new DefaultOptionCreateRequest(2, 0, 0, 0, SizeType.of("Tall")), "우유, 대두", new NutritionCreateRequest(0, 2, 3, 0, 1, 2, 2, 0, 0, 0));
        inValidObject = new ItemCreateRequest(new CategoryCreateRequest(CategoryType.of("음료"), "dd")
                , "아메리카노"
                , -5, "아메리카노입니다", null, false, true,
                new DefaultOptionCreateRequest(2, 0, 0, 0, SizeType.of("Tall")), "우유, 대두", new NutritionCreateRequest(0, 2, 3, 0, 1, 2, 2, 0, 0, 0));

    }

    @Test
    @DisplayName("아이템 생성, 조회에 성공한다")
    public void create() {
        // Given
        Long itemId = 1L;
        Item item = Item.builder()
                .itemId(itemId)
                .itemName(validObject.getItemName())
                .price(validObject.getPrice())
                .image(null)
                .category(Category.builder()
                        .categoryName(validObject.getCategoryRequest().getCategoryName())
                        .categoryType(validObject.getCategoryRequest().getCategoryType()).build())
                .defaultOption(DefaultOptionCreateRequest.toEntity(validObject.getDefaultOptionRequest()))
                .description(validObject.getDescription())
                .isNew(validObject.getIsNew())
                .isBest(validObject.getIsBest())
                .allergies(allergyConverter.convertToEntityAttribute(validObject.getAllergy())).build();
        // When
        when(categoryRepository.findByCategoryTypeAndCategoryName(any(), any()))
                .thenReturn(Optional.ofNullable(item.getCategory()));
        when(allergyConverter.convertToEntityAttribute(validObject.getAllergy())).thenReturn(EnumSet.of(AllergyType.MILK, AllergyType.SOYBEAN));
        when(itemRepository.findByIdWithOption(itemId)).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);


        // Then
        assertThat(itemService.create(validObject).getItemId()).isEqualTo(itemId);
        assertThat(itemService.findItemDetailById(itemId).getItemId()).isEqualTo(itemId);
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
        Throwable throwable = catchThrowable(() -> itemService.findAllByCategory(
                inValidObject.getCategoryRequest().getCategoryType().getName()
                , inValidObject.getCategoryRequest().getCategoryName()));
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
        Throwable throwable = catchThrowable(() -> itemService.updateItemById(itemId, validObject));

        // Then
        assertThat(throwable)
                .isInstanceOf(GlobalException.class)
                .hasMessageContaining(ResponseCode.ErrorCode.NOT_FOUND_ITEM.getMESSAGE());
    }


}