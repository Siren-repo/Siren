package com.devlop.siren.domain.item.service;

import com.devlop.siren.domain.category.dto.response.CategoryItemsResponse;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import com.devlop.siren.domain.item.dto.response.ItemDetailResponse;
import com.devlop.siren.domain.item.dto.response.ItemResponse;
import com.devlop.siren.domain.item.dto.response.NutritionDetailResponse;
import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.Nutrition;
import com.devlop.siren.domain.item.entity.option.DefaultOption;
import com.devlop.siren.domain.item.repository.DefaultOptionRepository;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.item.repository.NutritionRepository;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.domain.user.dto.UserDetailsDto;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import com.devlop.siren.global.util.UserInformation;
import java.util.EnumSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;
  private final CategoryRepository categoryRepository;
  private final NutritionRepository nutritionRepository;
  private final DefaultOptionRepository defaultOptionRepository;
  private final AllergyConverter allergyConverter;

  @Override
  @Transactional
  public ItemResponse create(ItemCreateRequest request) {
    Category itemCategory =
        categoryRepository
            .findByCategoryTypeAndCategoryName(
                request.getCategoryRequest().getCategoryType(),
                request.getCategoryRequest().getCategoryName())
            .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_CATEGORY));
    if (itemCategory.getCategoryType() == CategoryType.FOOD) {
      return createFood(request, itemCategory);
    }
    return createBeverage(request, itemCategory);
  }

  private ItemResponse createFood(ItemCreateRequest request, Category itemCategory) {
    EnumSet<AllergyType> allergies =
        allergyConverter.convertToEntityAttribute(request.getAllergy());
    Item item = ItemCreateRequest.toEntity(request, itemCategory, null, allergies, null);
    return ItemResponse.from(itemRepository.save(item));
  }

  private ItemResponse createBeverage(ItemCreateRequest request, Category itemCategory) {
    DefaultOption defaultOption =
        DefaultOptionCreateRequest.toEntity(request.getDefaultOptionRequest());
    defaultOptionRepository.save(defaultOption);

    Nutrition nutrition = NutritionCreateRequest.toEntity(request.getNutritionCreateRequest());
    nutritionRepository.save(nutrition);

    EnumSet<AllergyType> allergies =
        allergyConverter.convertToEntityAttribute(request.getAllergy());
    Item item =
        ItemCreateRequest.toEntity(request, itemCategory, defaultOption, allergies, nutrition);

    return ItemResponse.from(itemRepository.save(item));
  }

  @Override
  public Item findItem(Long itemId) {
    return itemRepository
        .findByIdWithOption(itemId)
        .orElseThrow(() -> new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM));
  }

  @Override
  public CategoryItemsResponse findAllByCategory(
      String categoryType, String categoryName, Pageable pageable) {
    PageRequest pageRequest =
        PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), Sort.by("itemId").descending());
    Page<Item> items =
        Optional.ofNullable(
                itemRepository.findAllByItemTypeAndCategoryName(
                    CategoryType.of(categoryType), categoryName, pageRequest))
            .orElseThrow(
                () -> {
                  throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_CATEGORY);
                });
    return new CategoryItemsResponse(categoryName, items.map(ItemResponse::from));
  }

  @Override
  public ItemDetailResponse findItemDetailById(Long itemId) {
    Item item =
        itemRepository
            .findByIdWithOption(itemId)
            .orElseThrow(
                () -> {
                  throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM);
                });
    String allergy = allergyConverter.convertToDatabaseColumn(item.getAllergies());
    return ItemDetailResponse.from(item, allergy);
  }

  @Override
  public NutritionDetailResponse findNutritionDetailById(Long itemId) {
    Item item =
        itemRepository
            .findByIdWithNutrition(itemId)
            .orElseThrow(
                () -> {
                  throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM);
                });

    return NutritionDetailResponse.from(item);
  }

  @Override
  @Transactional
  public Long deleteItemById(Long itemId) {
    try {
      itemRepository.deleteById(itemId);
    } catch (Exception e) {
      throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM);
    }
    return itemId;
  }

  @Override
  @Transactional
  public Long updateItemById(
      Long itemId, ItemCreateRequest itemCreateRequest) {
    Item item =
        itemRepository
            .findByIdWithOption(itemId)
            .orElseThrow(
                () -> {
                  throw new GlobalException(ResponseCode.ErrorCode.NOT_FOUND_ITEM);
                });
    Category category = item.getCategory();
    DefaultOption defaultOption = item.getDefaultOption();
    Nutrition nutrition = item.getNutrition();
    EnumSet<AllergyType> allergies =
        allergyConverter.convertToEntityAttribute(itemCreateRequest.getAllergy());
    item.update(itemCreateRequest, allergies);
    category.update(itemCreateRequest.getCategoryRequest());
    defaultOption.update(itemCreateRequest.getDefaultOptionRequest());
    nutrition.update(itemCreateRequest.getNutritionCreateRequest());
    return item.getItemId();
  }
}
