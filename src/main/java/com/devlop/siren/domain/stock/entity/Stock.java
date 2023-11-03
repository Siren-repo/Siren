package com.devlop.siren.domain.stock.entity;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.store.domain.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "stocks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"item_id", "store_id"})
})
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
    public Stock(Long stockId, Item item, Store store, int stock) {
        this.stockId = stockId;
        this.item = item;
        this.store = store;
        this.stock = stock;
    }

    public void update(Integer stock) {
        this.stock = stock;
    }
}
