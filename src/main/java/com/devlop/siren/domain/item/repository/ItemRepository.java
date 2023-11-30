package com.devlop.siren.domain.item.repository;

import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.item.entity.Item;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item, Long> {
  @Query("select i from Item i join fetch i.defaultOption do where i.itemId = :itemId")
  Optional<Item> findByIdWithOption(@Param("itemId") Long itemId);

  @Query("select i from Item i join fetch i.nutrition do where i.itemId = :itemId")
  Optional<Item> findByIdWithNutrition(@Param("itemId") Long itemId);

  @Query(
      "select i from Item i join fetch i.category c where c.categoryType = :categoryType and"
          + " c.categoryName = :categoryName ")
  Optional<List<Item>> findAllByItemTypeAndCategoryName(
      @Param("categoryType") CategoryType categoryType, @Param("categoryName") String categoryName);
}
