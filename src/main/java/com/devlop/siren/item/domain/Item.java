package com.devlop.siren.item.domain;

import com.devlop.siren.item.dto.ItemCreateRequest;
import com.devlop.siren.item.utils.AllergyConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
@Getter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotNull
    @Column(name = "item_name", columnDefinition = "NVARCHAR(255)")
    private String itemName;

    @NotNull
    @Column(name = "price")
    private Integer price;

    @NotNull
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "image")
    private String image;


    @Column(name = "is_best", columnDefinition = "TINYINT(1)")
    private Boolean isBest;

    @Column(name = "is_new", columnDefinition = "TINYINT(1)")
    private Boolean isNew;

    @Column(name = "allergy")
    @Convert(converter = AllergyConverter.class)
    private EnumSet<Allergy> allergies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "default_option_id")
    private DefaultOption defaultOption;

    @PrePersist
    public void prePersist() {
        this.isBest = this.isBest != null && this.isBest;
        this.isNew = this.isNew != null && this.isNew;
        this.stock = this.stock == null ? 0 : this.stock;
    }

    @Builder
    public Item(String itemName, Integer price, String description, Integer stock, String image, Boolean isBest, Boolean isNew, EnumSet<Allergy> allergies, Category category, DefaultOption defaultOption) {
        this.itemName = itemName;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.image = image;
        this.isBest = isBest;
        this.isNew = isNew;
        this.allergies = allergies;
        this.category = category;
        this.defaultOption = defaultOption;
    }

    public void update(ItemCreateRequest itemCreateRequest) {
        this.itemName = itemCreateRequest.getItemName();
        this.price = itemCreateRequest.getPrice();
        this.description = itemCreateRequest.getDescription();
        this.stock = itemCreateRequest.getStock();
        this.image = itemCreateRequest.getImage();
        this.isBest = itemCreateRequest.getIsBest();
        this.isNew = itemCreateRequest.getIsNew();
        this.allergies = Allergy.convertToEnumSet(itemCreateRequest.getAllergy());
    }
}
