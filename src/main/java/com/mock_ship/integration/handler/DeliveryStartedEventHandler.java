package com.mock_ship.integration.handler;

import com.mock_ship.domain.delivery.event.DeliveryStartedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryStartedEventHandler {

    @EventListener
    public void handle(DeliveryStartedEvent event) {
        log.info(" 배송 시작됨: 배송번호={}", event.getDeliveryNo());

        // TODO: 물류 시스템 연동, 운송 정보 업데이트 등의 로직 추가 가능
    }
}