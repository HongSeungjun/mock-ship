package com.mock_ship.integration.command.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "webhook_subscription")
@Getter
@NoArgsConstructor
public class WebhookSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 내부적으로 관리하는 고유 ID

    private String customerId; // 구독한 외부 시스템 (쇼핑몰) ID

    private String eventType; // 구독할 이벤트 타입 (예: "DELIVERY_STATUS_UPDATED")

    private String callbackUrl; // Webhook 요청을 보낼 URL

    private boolean active; // Webhook 활성화 여부

    @Builder
    public WebhookSubscription(String customerId, String eventType, String callbackUrl, boolean active) {
        this.customerId = customerId;
        this.eventType = eventType;
        this.callbackUrl = callbackUrl;
        this.active = active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }
}
