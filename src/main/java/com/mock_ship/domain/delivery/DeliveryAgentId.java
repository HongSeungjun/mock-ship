package com.mock_ship.domain.delivery;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class DeliveryAgentId {
    private String value;

    protected DeliveryAgentId() {}

    public DeliveryAgentId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DeliveryAgentId는 필수 값입니다.");
        }
        this.value = value;
    }

    public static DeliveryAgentId generate() {
        return new DeliveryAgentId(UUID.randomUUID().toString());
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryAgentId)) return false;
        DeliveryAgentId agentId = (DeliveryAgentId) o;
        return Objects.equals(value, agentId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
