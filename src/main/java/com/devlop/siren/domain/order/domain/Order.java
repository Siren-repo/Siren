package com.devlop.siren.domain.order.domain;

import com.devlop.siren.domain.store.domain.Store;
import com.devlop.siren.domain.user.domain.User;
import com.devlop.siren.global.common.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
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

  private Integer totalAmount = 0;

  public static Order of(User user, Store store, List<OrderItem> items) {
    Order newOrder = new Order();
    newOrder.setUser(user);
    newOrder.setStore(store);
    newOrder.setStatus(OrderState.INIT);
    newOrder.setOrderItem(items);
    newOrder.setTotalAmount(getTotalAmount(items));
    return newOrder;
  }

  private static int getTotalAmount(List<OrderItem> items) {
    return items.stream()
        .mapToInt(
            item ->
                item.getItem().getPrice() * item.getQuantity()
                    + item.getCustomOption().getAdditionalAmount())
        .sum();
  }

  public void cancel() {
    status = OrderState.CANCELLED;
  }

  private void setUser(User user) {
    this.user = user;
    user.getOrders().add(this);
  }

  private void setStore(Store store) {
    this.store = store;
    store.getOrders().add(this);
  }

  private void setTotalAmount(Integer amount) {
    this.totalAmount = amount;
  }

  private void setOrderItem(List<OrderItem> items) {
    items.stream()
        .map(
            orderItem -> {
              orderItems.add(orderItem);
              orderItem.setOrder(this);
              return orderItem;
            });
  }

  private void setStatus(OrderState status) {
    this.status = status;
  }
}
