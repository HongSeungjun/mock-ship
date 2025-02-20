package com.mock_ship.domain.delivery;

import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class DeliveryAgentId {
    private String number;

    protected DeliveryAgentId() {}

    public DeliveryAgentId(String number) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("DeliveryAgentId는 필수 값입니다.");
        }
        this.number = number;
    }

    public static DeliveryAgentId generate() {
        return new DeliveryAgentId(UUID.randomUUID().toString());
    }

    public String getNumber() { return number; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryAgentId)) return false;
        DeliveryAgentId agentId = (DeliveryAgentId) o;
        return Objects.equals(number, agentId.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
