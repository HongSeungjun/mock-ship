package com.mock_ship.integration.persistence.delivery.handler;

import com.mock_ship.domain.delivery.event.DeliveryCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryCreatedEventHandler {

    @EventListener
    public void handle(DeliveryCreatedEvent event) {
        log.info(" 배송 생성 이벤트 처리됨: 배송번호={}, 주문번호={}",
                event.getDeliveryNo(), event.getOrderNo());

        // TODO: 메시지 큐 전송, 알림 전송 등의 로직 추가 가능
    }
}