package com.mock_ship.delivery.command.domain;

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

    @OneToMany(mappedBy = "assignedAgent")
    private List<Delivery> assignedDeliveries = new ArrayList<>();

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

    public void assignDelivery(Delivery delivery) {
        if (this.status == AgentStatus.UNAVAILABLE) {
            throw new IllegalStateException("현재 배정이 불가능한 배송 담당자입니다.");
        }
        assignedDeliveries.add(delivery);
        this.status = AgentStatus.ASSIGNED;
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
