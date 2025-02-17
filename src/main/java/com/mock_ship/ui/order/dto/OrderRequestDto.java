package com.mock_ship.ui.order.dto;

import com.mock_ship.domain.order.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {
    private String orderNo;
    private String customerId;
    private List<OrderItem> items;

    public OrderRequestDto(String orderNo, String customerId, List<OrderItem> items) {
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.items = items;
    }
}
