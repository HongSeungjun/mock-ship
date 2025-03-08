package com.mock_ship.common.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 도메인 이벤트 발행기 (Spring Event 기반)
 */
@Component
public class DomainEventPublisher {

    private static ApplicationEventPublisher eventPublisher;

    public DomainEventPublisher(ApplicationEventPublisher eventPublisher) {
        DomainEventPublisher.eventPublisher = eventPublisher;
    }

    /** 이벤트 발행 */
    public static void publish(Object event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }
}