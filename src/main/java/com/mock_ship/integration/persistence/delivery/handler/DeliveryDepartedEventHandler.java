package com.mock_ship.integration.persistence.delivery.handler;

import com.mock_ship.domain.delivery.event.DeliveryDepartedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryDepartedEventHandler {

    @EventListener
    public void handle(DeliveryDepartedEvent event) {
        log.info(" 배송 출발: 배송번호={}", event.getDeliveryNo());

        // TODO: 고객 알림 시스템 연동
    }
}