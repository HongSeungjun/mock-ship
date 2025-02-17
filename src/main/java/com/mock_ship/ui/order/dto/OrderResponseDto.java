package com.mock_ship.ui.order.dto;

import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderStatus;
import lombok.Getter;

@Getter
public class OrderResponseDto {
    private String orderNo;
    private String customerId;
    private OrderStatus orderStatus;

    public OrderResponseDto(Order order) {
        this.orderNo = order.getNumber().getNumber();
        this.customerId = order.getCustomerId();
        this.orderStatus = order.getOrderStatus();
    }
}
