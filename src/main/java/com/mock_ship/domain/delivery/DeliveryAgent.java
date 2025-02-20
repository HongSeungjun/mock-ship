package com.mock_ship.domain.delivery;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 배송 담당자 애그리게잇
 */
@Entity
@Table(name = "delivery_agents")
public class DeliveryAgent {
    @EmbeddedId
    private DeliveryAgentId agentId;

    private String name;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private AgentStatus status;

    protected DeliveryAgent() {}

    public DeliveryAgent(DeliveryAgentId agentId, String name, String phoneNumber) {
        if (agentId == null) throw new IllegalArgumentException("배송 담당자 ID는 필수입니다.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("이름은 필수입니다.");
        if (phoneNumber == null || phoneNumber.isBlank()) throw new IllegalArgumentException("전화번호는 필수입니다.");

        this.agentId = agentId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = AgentStatus.AVAILABLE;
    }

    public void markAsUnavailable() {
        this.status = AgentStatus.UNAVAILABLE;
    }

    public void markAsAvailable() {
        this.status = AgentStatus.AVAILABLE;
    }

    public DeliveryAgentId getAgentId() { return agentId; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public AgentStatus getStatus() { return status; }
}
