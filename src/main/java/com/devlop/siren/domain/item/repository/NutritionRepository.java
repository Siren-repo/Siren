package com.devlop.siren.domain.item.repository;

import com.devlop.siren.domain.item.entity.Nutrition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionRepository extends JpaRepository<Nutrition, Long> {
}
