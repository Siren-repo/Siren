package com.devlop.siren.domain.category.repository;

import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryTypeAndCategoryName(@Param("categoryType") CategoryType categoryType,
                                                         @Param("categoryName") String categoryName);

    Page<Category> findByCategoryTypeOrderByCategoryId(@Param("categoryType") CategoryType categoryType, Pageable pageable);

}
