package com.mock_ship.domain.delivery.event;

import com.mock_ship.common.event.BaseEvent;
import com.mock_ship.domain.delivery.DeliveryNo;

public class DeliveryDepartedEvent extends BaseEvent {
    private final DeliveryNo deliveryNo;

    public DeliveryDepartedEvent(DeliveryNo deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public DeliveryNo getDeliveryNo() {
        return deliveryNo;
    }
}