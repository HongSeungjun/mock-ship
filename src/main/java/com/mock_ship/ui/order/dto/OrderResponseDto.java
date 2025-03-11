package com.mock_ship.ui.order.dto;

import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderItem;
import com.mock_ship.domain.order.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderResponseDto {
    private String orderNo;
    private String customerId;
    private OrderStatus orderStatus;
    private List<OrderItem> orderItems;

    public OrderResponseDto(Order order) {
        this.orderNo = order.getOrderNo().getNumber();
        this.customerId = order.getCustomerId();
        this.orderStatus = order.getOrderStatus();
        this.orderItems = order.getItems();
    }
}
