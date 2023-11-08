package com.devlop.siren.domain.order.domain;

import com.devlop.siren.domain.item.entity.Item;
import com.devlop.siren.domain.order.domain.option.CustomOption;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_items", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"order_id", "item_id"})
})
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_option_id")
    private CustomOption customOption;

    private Integer quantity;

    public static OrderItem create(Item item, CustomOption customOption, Integer quantity){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCustomOption(customOption);
        orderItem.setQuantity(quantity);
        //TODO :: item.deduct(quantity)
        return orderItem;
    }
    public void setOrder(Order order){
        this.order = order;
    }
    private void setItem(Item item){
        this.item = item;
    }
    private void setCustomOption(CustomOption option){
        this.customOption = option;
    }
    private void setQuantity(Integer quantity){
        this.quantity = quantity;
    }
}
