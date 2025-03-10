package com.mock_ship.domain.delivery.event;

import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.order.OrderNo;

import java.time.LocalDateTime;

/**
 *  배송 생성 이벤트
 * - 주문이 확정되면 배송이 생성됨
 * - 배송이 생성되었음을 다른 시스템에 알리기 위한 이벤트
 */
public class DeliveryCreatedEvent {

    private final DeliveryNo deliveryNo;
    private final OrderNo orderNo;
    private final Address deliveryAddress;
    private final LocalDateTime createdAt;

    public DeliveryCreatedEvent(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress) {
        this.deliveryNo = deliveryNo;
        this.orderNo = orderNo;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}