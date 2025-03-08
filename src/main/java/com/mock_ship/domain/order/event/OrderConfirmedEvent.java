package com.mock_ship.domain.order.event;

import com.mock_ship.domain.order.OrderNo;

public class OrderConfirmedEvent {
    private final OrderNo orderNo;

    public OrderConfirmedEvent(OrderNo orderNo) {
        this.orderNo = orderNo;
    }

    public OrderNo getOrderNo() {
        return orderNo;
    }
}