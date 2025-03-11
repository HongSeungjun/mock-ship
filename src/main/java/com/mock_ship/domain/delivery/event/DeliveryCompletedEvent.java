package com.mock_ship.domain.delivery.event;

import com.mock_ship.domain.delivery.DeliveryNo;
import java.time.LocalDateTime;

/**
 *  배송 완료 이벤트
 * - 배송이 정상적으로 완료되었음을 알리는 이벤트
 */
public record DeliveryCompletedEvent(DeliveryNo deliveryNo, LocalDateTime completedAt) {
    public DeliveryCompletedEvent(DeliveryNo deliveryNo) {
        this(deliveryNo, LocalDateTime.now());
    }
}