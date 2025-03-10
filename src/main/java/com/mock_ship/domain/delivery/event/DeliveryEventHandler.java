package com.mock_ship.domain.delivery.event;

import com.mock_ship.domain.delivery.event.DeliveryAssignedEvent;
import com.mock_ship.domain.delivery.event.DeliveryCompletedEvent;
import com.mock_ship.domain.delivery.event.DeliveryCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryEventHandler {

    @EventListener
    public void handleDeliveryCreated(DeliveryCreatedEvent event) {
        log.info(" 배송 생성됨: 주문번호={}, 배송번호={}", event.getOrderNo(), event.getDeliveryNo());

    }

    @EventListener
    public void handleDeliveryAssigned(DeliveryAssignedEvent event) {
        log.info(" 배송 기사 배정됨: 배송번호={}, 기사ID={}", event.getDeliveryNo(), event.getDeliveryAgentId());
        // TODO: 배송 기사 시스템 업데이트 로직
    }

    @EventListener
    public void handleDeliveryCompleted(DeliveryCompletedEvent event) {
        log.info(" 배송 완료됨: 배송번호={}", event.getDeliveryNo());
        // TODO: 고객 알림 서비스 호출
    }
}