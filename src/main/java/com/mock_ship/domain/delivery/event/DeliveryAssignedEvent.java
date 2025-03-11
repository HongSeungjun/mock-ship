package com.mock_ship.domain.delivery.event;

import com.mock_ship.common.event.BaseEvent;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryAgentId;

public class DeliveryAssignedEvent extends BaseEvent {
    private final DeliveryNo deliveryNo;
    private final DeliveryAgentId agentId;

    public DeliveryAssignedEvent(DeliveryNo deliveryNo, DeliveryAgentId agentId) {
        this.deliveryNo = deliveryNo;
        this.agentId = agentId;
    }

    public DeliveryNo getDeliveryNo() {
        return deliveryNo;
    }

    public DeliveryAgentId getAgentId() {
        return agentId;
    }
}