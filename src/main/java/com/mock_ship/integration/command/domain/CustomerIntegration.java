package com.mock_ship.integration.command.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "customer_integration")
@Getter
@NoArgsConstructor
public class CustomerIntegration {

    @Id
    private String customerId;
    private String customerName;
    private String apiKey;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WebhookSubscription> webhookSubscriptions;

    @Builder
    public CustomerIntegration(String customerId, String customerName, String apiKey, List<WebhookSubscription> webhookSubscriptions) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.apiKey = apiKey;
        this.webhookSubscriptions = webhookSubscriptions;
    }
}
