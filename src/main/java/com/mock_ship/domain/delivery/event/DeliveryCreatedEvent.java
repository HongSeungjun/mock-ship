package com.mock_ship.domain.delivery.event;

import com.mock_ship.common.event.BaseEvent;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.common.model.Address;

public class DeliveryCreatedEvent extends BaseEvent {
    private final DeliveryNo deliveryNo;
    private final OrderNo orderNo;
    private final Address deliveryAddress;

    public DeliveryCreatedEvent(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress) {
        this.deliveryNo = deliveryNo;
        this.orderNo = orderNo;
        this.deliveryAddress = deliveryAddress;
    }

    public DeliveryNo getDeliveryNo() {
        return deliveryNo;
    }

    public OrderNo getOrderNo() {
        return orderNo;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }
}