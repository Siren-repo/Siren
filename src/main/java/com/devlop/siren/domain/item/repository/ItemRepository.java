package com.devlop.siren.domain.item.repository;

import com.devlop.siren.domain.category.entity.CategoryType;
import com.devlop.siren.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i join fetch i.defaultOption do where i.itemId = :itemId")
    Optional<Item> findByIdWithOption(@Param("itemId") Long itemId);

    @Query("select i from Item i join fetch i.nutrition do where i.itemId = :itemId")
    Optional<Item> findByIdWithNutrition(@Param("itemId") Long itemId);

    @Query(value = "SELECT i FROM Item i JOIN FETCH i.category c WHERE c.categoryType = :categoryType AND c.categoryName = :categoryName",
            countQuery = "SELECT COUNT(i) FROM Item i JOIN i.category c WHERE c.categoryType = :categoryType AND c.categoryName = :categoryName")
    Page<Item> findAllByItemTypeAndCategoryName(@Param("categoryType") CategoryType categoryType,
                                                @Param("categoryName") String categoryName,
                                                Pageable pageable);

}
