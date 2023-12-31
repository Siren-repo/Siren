package com.devlop.siren.domain.item.entity;

import com.devlop.siren.domain.category.entity.Category;
import com.devlop.siren.domain.item.dto.request.ItemCreateRequest;
import com.devlop.siren.domain.item.entity.option.DefaultOption;
import com.devlop.siren.domain.item.utils.AllergyConverter;
import java.util.EnumSet;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "items")
@Getter
public class Item {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long itemId;

  @Column(name = "item_name", columnDefinition = "NVARCHAR(255) NOT NULL")
  private String itemName;

  @Column(name = "price", columnDefinition = "INT NOT NULL")
  private Integer price;

  @Column(name = "description", columnDefinition = "TEXT NOT NULL")
  private String description;

  @Column(name = "is_best", columnDefinition = "TINYINT(1)")
  private Boolean isBest;

  @Column(name = "is_new", columnDefinition = "TINYINT(1)")
  private Boolean isNew;

  @Column(name = "image")
  private String image;

  @Column(name = "allergy")
  @Convert(converter = AllergyConverter.class)
  private EnumSet<AllergyType> allergies;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "default_option_id")
  private DefaultOption defaultOption;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "nutrition_id")
  private Nutrition nutrition;

  @PrePersist
  public void prePersist() {
    this.isBest = this.isBest != null && this.isBest;
    this.isNew = this.isNew != null && this.isNew;
  }

  @Builder
  public Item(
      String itemName,
      Integer price,
      String image,
      String description,
      Boolean isBest,
      Boolean isNew,
      EnumSet<AllergyType> allergies,
      Category category,
      DefaultOption defaultOption,
      Nutrition nutrition) {
    this.itemName = itemName;
    this.image = image;
    this.price = price;
    this.description = description;
    this.isBest = isBest;
    this.isNew = isNew;
    this.allergies = allergies;
    this.category = category;
    this.defaultOption = defaultOption;
    this.nutrition = nutrition;
  }

  public void update(ItemCreateRequest itemCreateRequest, EnumSet<AllergyType> allergies) {
    setItemName(itemCreateRequest.getItemName());
    setPrice(itemCreateRequest.getPrice());
    setImage(itemCreateRequest.getImage());
    setDescription(itemCreateRequest.getDescription());
    setBest(itemCreateRequest.getIsBest());
    setNew(itemCreateRequest.getIsNew());
    setAllergies(allergies);
  }

  private void setItemName(String itemName) {
    this.itemName = itemName;
  }

  private void setPrice(Integer price) {
    this.price = price;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  private void setBest(Boolean best) {
    this.isBest = best == null ? this.isBest : best;
  }

  private void setNew(Boolean aNew) {
    this.isNew = aNew == null ? this.isNew : aNew;
  }

  private void setAllergies(EnumSet<AllergyType> allergies) {
    this.allergies = allergies == null ? this.allergies : allergies;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
