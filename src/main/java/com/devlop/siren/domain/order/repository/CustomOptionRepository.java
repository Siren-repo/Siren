package com.devlop.siren.domain.order.repository;

import com.devlop.siren.domain.order.domain.option.CustomOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomOptionRepository extends JpaRepository<CustomOption, Long> {}
