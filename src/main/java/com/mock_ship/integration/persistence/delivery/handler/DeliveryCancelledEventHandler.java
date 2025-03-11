package com.mock_ship.integration.persistence.delivery.handler;

import com.mock_ship.domain.delivery.event.DeliveryCancelledEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryCancelledEventHandler {

    @EventListener
    public void handle(DeliveryCancelledEvent event) {
        log.info(" 배송 취소됨: 배송번호={}", event.getDeliveryNo());

        // TODO: 환불 처리, 주문 상태 업데이트 등의 로직 추가 가능
    }
}