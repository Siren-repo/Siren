package com.devlop.siren.domain.item.service;

import com.devlop.siren.domain.category.dto.response.CategoryItemsResponse;
import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.category.repository.CategoryRepository;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.dto.response.ItemDetailResponse;
import com.devlop.siren.domain.item.dto.response.ItemResponse;
import com.devlop.siren.domain.item.dto.response.NutritionDetailResponse;
import com.devlop.siren.domain.item.entity.AllergyType;
import com.devlop.siren.domain.item.repository.DefaultOptionRepository;
import com.devlop.siren.domain.item.repository.ItemRepository;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import com.devlop.siren.global.exception.EntityNotFoundException;
import com.devlop.siren.domain.item.dto.request.DefaultOptionCreateRequest;
import com.devlop.siren.domain.item.dto.request.NutritionCreateRequest;
import com.devlop.siren.domain.item.entity.DefaultOption;
import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.item.entity.Nutrition;
import com.devlop.siren.domain.item.repository.NutritionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final NutritionRepository nutritionRepository;
    private final DefaultOptionRepository defaultOptionRepository;
    private final AllergyConverter allergyConverter;


    // 아이템 생성
    @Override
    @Transactional
    public ItemResponse create(ItemCreateRequest request) {
        Category itemCategory = categoryRepository.findByCategoryTypeAndCategoryName(request.getCategoryRequest().getCategoryType(),
                        request.getCategoryRequest().getCategoryName())
                .orElseThrow(() -> new EntityNotFoundException());

        DefaultOption defaultOption = DefaultOptionCreateRequest.toEntity(request.getDefaultOptionRequest());
        defaultOptionRepository.save(defaultOption);

        Nutrition nutrition = NutritionCreateRequest.toEntity(request.getNutritionCreateRequest());
        nutritionRepository.save(nutrition);

        EnumSet<AllergyType> allergies = allergyConverter.convertToEntityAttribute(request.getAllergy());
        Item item = ItemCreateRequest.toEntity(request, itemCategory, defaultOption, allergies, nutrition);

        return ItemResponse.from(itemRepository.save(item));
    }

    // 카테고리 이름별 아이템 출력
    @Override
    public CategoryItemsResponse findAllByCategory(String categoryType, String categoryName) {
        return new CategoryItemsResponse(categoryName,
                itemRepository.findAllByItemTypeAndCategoryName(CategoryType.of(categoryType), categoryName)
                        .orElseThrow(() -> new EntityNotFoundException())
                        .stream().map(item -> ItemResponse.from(item))
                        .collect(Collectors.toUnmodifiableList()));
    }

    // 아이템 상세 조회
    @Override
    public ItemDetailResponse findItemDetailById(Long itemId) {
        Item item = itemRepository.findByIdWithOption(itemId).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        String allergy = allergyConverter.convertToDatabaseColumn(item.getAllergies());
        return ItemDetailResponse.from(item, allergy);
    }

    @Override
    public NutritionDetailResponse findNutritionDetailById(Long itemId) {
        Item item = itemRepository.findByIdWithNutrition(itemId).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });

        return NutritionDetailResponse.from(item);
    }

    // 아이템 삭제
    @Override
    @Transactional
    public Long deleteItemById(Long itemId) {
        itemRepository.deleteById(itemId);
        return itemId;
    }

    // 아이템 수정
    @Override
    @Transactional
    public Long updateItemById(Long itemId, ItemCreateRequest itemCreateRequest) {
        Item item = itemRepository.findByIdWithOption(itemId).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        Category category = item.getCategory();
        DefaultOption defaultOption = item.getDefaultOption();
        Nutrition nutrition = item.getNutrition();
        EnumSet<AllergyType> allergies = allergyConverter.convertToEntityAttribute(itemCreateRequest.getAllergy());
        item.update(itemCreateRequest, allergies);
        category.update(itemCreateRequest.getCategoryRequest());
        defaultOption.update(itemCreateRequest.getDefaultOptionRequest());
        nutrition.update(itemCreateRequest.getNutritionCreateRequest());
        return item.getItemId();
    }


}
