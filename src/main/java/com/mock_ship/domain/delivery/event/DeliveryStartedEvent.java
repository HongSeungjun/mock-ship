package com.mock_ship.domain.delivery.event;

import com.mock_ship.common.event.BaseEvent;
import com.mock_ship.domain.delivery.DeliveryNo;

public class DeliveryStartedEvent extends BaseEvent {
    private final DeliveryNo deliveryNo;

    public DeliveryStartedEvent(DeliveryNo deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public DeliveryNo getDeliveryNo() {
        return deliveryNo;
    }
}