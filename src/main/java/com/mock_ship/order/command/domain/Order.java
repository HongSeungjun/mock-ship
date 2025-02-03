package com.mock_ship.order.command.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @EmbeddedId
    private OrderNo number;
    private String customerId;

    @ElementCollection(fetch = FetchType.LAZY) // 필요할때만 로딩
    @CollectionTable(name = "order_item", joinColumns = @JoinColumn(name = "order_number")) // 벨류 컬랙션 매핑
    @OrderColumn(name = "item_idx")
    private List<OrderItem> items;

    private double totalWeight;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


}
