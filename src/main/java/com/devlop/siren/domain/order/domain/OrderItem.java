package com.devlop.siren.domain.order.domain;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.order.domain.option.CustomOption;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "order_items",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"order_id", "item_id"})})
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_item_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "custom_option_id")
  private CustomOption customOption;

  private Integer quantity;
  private Integer amount;

  public static OrderItem create(Item item, CustomOption customOption, Integer quantity) {
    OrderItem orderItem = new OrderItem();
    orderItem.setItem(item);
    orderItem.setCustomOption(customOption);
    orderItem.setQuantity(quantity);
    orderItem.setAmount((item.getPrice() + customOption.getAdditionalAmount()) * quantity);
    return orderItem;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  private void setItem(Item item) {
    this.item = item;
  }

  private void setCustomOption(CustomOption option) {
    this.customOption = option;
  }

  private void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  private void setAmount(Integer amount) {
    this.amount = amount;
  }
}
