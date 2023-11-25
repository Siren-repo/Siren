package com.devlop.siren.domain.stock.entity;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.global.common.response.ResponseCode;
import com.devlop.siren.global.exception.GlobalException;
import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
    name = "stocks",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"item_id", "store_id"})})
public class Stock {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long stockId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id")
  private Store store;

  @Column(name = "stock", columnDefinition = "INT NOT NULL")
  private Integer stock;

  @Builder
  public Stock(Item item, Store store, int stock) {
    this.item = item;
    this.store = store;
    this.stock = stock;
  }

  public void update(Integer stock) {
    this.stock = stock;
  }

  public void consumed(int quantity) {
    if (stock < quantity) {
      throw new GlobalException(ResponseCode.ErrorCode.ORDER_QUANTITY_IN_STOCK);
    }
    this.stock -= quantity;
  }

  public void revert(int quantity) {
    this.stock += quantity;
  }
}
