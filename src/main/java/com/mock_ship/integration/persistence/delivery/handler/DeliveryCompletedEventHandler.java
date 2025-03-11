package com.mock_ship.integration.persistence.delivery.handler;

import com.mock_ship.domain.delivery.event.DeliveryCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryCompletedEventHandler {

    @EventListener
    public void handle(DeliveryCompletedEvent event) {
        log.info(" 배송 완료됨: 배송번호={}", event.deliveryNo());

        // TODO: 반품 가능 상태 업데이트 등의 후처리
    }
}