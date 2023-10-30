package com.devlop.siren.domain.order.domain;

import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @Enumerated(EnumType.STRING)
    private OrderState status;

    private Integer totalPrice = 0;

    public static Order of(User user, Store store, List<OrderItem> items){
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStore(store);
        newOrder.setStatus(OrderState.INIT);
        newOrder.setOrderItem(items);
        newOrder.setTotalPrice(getTotalPrice(items));
        return newOrder;
    }
    private static int getTotalPrice(List<OrderItem> items){
        return items.stream()
                .mapToInt(item -> item.getItem().getPrice() * item.getQuantity()
                        + item.getCustomOption().getPrice())
                .sum();
    }
    public void cancel(){
        status = OrderState.CANCELLED;
    }
    private void setUser(User user){
        this.user = user;
        user.getOrders().add(this);
    }
    private void setStore(Store store) {
        this.store = store;
        store.getOrders().add(this);
    }
    private void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
    private void setOrderItem(List<OrderItem> items){
        items.stream()
                .map(orderItem -> {
                    orderItems.add(orderItem);
                    orderItem.setOrder(this);
                    return orderItem;
                });
    }
    private void setStatus(OrderState status){
        this.status = status;
    }
}
