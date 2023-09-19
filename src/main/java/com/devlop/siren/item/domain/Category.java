package com.devlop.siren.item.domain;

import com.devlop.siren.item.dto.CategoryCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "category_name")
    @NotNull
    private String categoryName;

    @Column(name = "category_type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Builder
    public Category(String categoryName, CategoryType categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public void update(CategoryCreateRequest categoryCreateRequest) {
        this.categoryName = categoryCreateRequest.getCategoryName();
        this.categoryType = categoryCreateRequest.getCategoryType();
    }
}
