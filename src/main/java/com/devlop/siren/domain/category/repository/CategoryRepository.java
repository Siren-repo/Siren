package com.devlop.siren.domain.category.repository;

import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.category.entity.CategoryType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryTypeAndCategoryName(@Param("categoryType") CategoryType categoryType,
                                                         @Param("categoryName") String categoryName);

    Optional<List<Category>> findByCategoryTypeOrderByCategoryId(@Param("categoryType") CategoryType categoryType);

}
