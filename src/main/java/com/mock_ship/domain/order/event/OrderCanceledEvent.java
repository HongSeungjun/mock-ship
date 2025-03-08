package com.mock_ship.domain.order.event;

import com.mock_ship.domain.order.OrderNo;

public class OrderCanceledEvent {
    private final OrderNo orderNo;

    public OrderCanceledEvent(OrderNo orderNo) {
        this.orderNo = orderNo;
    }

    public OrderNo getOrderNo() {
        return orderNo;
    }
}