package com.devlop.siren.domain.category.entity;

import com.devlop.siren.domain.category.dto.request.CategoryCreateRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Setter
    @Column(name = "category_name", columnDefinition = "NVARCHAR(255) NOT NULL")
    private String categoryName;

    @Setter
    @Column(name = "category_type", columnDefinition = "NVARCHAR(255) NOT NULL")
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Builder
    public Category(Long categoryId, String categoryName, CategoryType categoryType) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public void update(CategoryCreateRequest categoryCreateRequest) {
        setCategoryName(categoryCreateRequest.getCategoryName());
        setCategoryType(categoryCreateRequest.getCategoryType());
    }


}
