package com.mock_ship.domain.delivery;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class DeliveryAgentId {
    @Column(name = "delivery_id")
    private String id;

    protected DeliveryAgentId() {}

    public DeliveryAgentId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("DeliveryAgentId는 필수 값입니다.");
        }
        this.id = id;
    }

    public static DeliveryAgentId generate() {
        return new DeliveryAgentId(UUID.randomUUID().toString());
    }

    public String getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryAgentId)) return false;
        DeliveryAgentId agentId = (DeliveryAgentId) o;
        return Objects.equals(id, agentId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static DeliveryAgentId of(String number) {
        return new DeliveryAgentId(number);
    }

}
