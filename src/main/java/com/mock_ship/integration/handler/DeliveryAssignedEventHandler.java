package com.mock_ship.integration.handler;

import com.mock_ship.domain.delivery.event.DeliveryAssignedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeliveryAssignedEventHandler {

    @EventListener
    public void handle(DeliveryAssignedEvent event) {
        log.info(" 배송 기사 배정됨: 배송번호={}, 기사ID={}",
                event.getDeliveryNo(), event.getAgentId());

        // TODO: 기사 스케줄 업데이트, 알림 전송 등의 로직 추가 가능
    }
}