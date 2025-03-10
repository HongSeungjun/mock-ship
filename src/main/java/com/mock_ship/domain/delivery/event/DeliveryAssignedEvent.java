package com.mock_ship.domain.delivery.event;

import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryAgentId;
import java.time.LocalDateTime;

/**
 *  배송 기사 배정 이벤트
 * - 배송 출발 시 배송 기사가 배정됨
 * - 해당 이벤트를 통해 담당 기사가 배정되었음을 알림
 */
public class DeliveryAssignedEvent {

    private final DeliveryNo deliveryNo;
    private final DeliveryAgentId deliveryAgentId;
    private final LocalDateTime assignedAt;

    public DeliveryAssignedEvent(DeliveryNo deliveryNo, DeliveryAgentId deliveryAgentId) {
        this.deliveryNo = deliveryNo;
        this.deliveryAgentId = deliveryAgentId;
        this.assignedAt = LocalDateTime.now();
    }

    public DeliveryNo getDeliveryNo() {
        return deliveryNo;
    }

    public DeliveryAgentId getDeliveryAgentId() {
        return deliveryAgentId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }
}