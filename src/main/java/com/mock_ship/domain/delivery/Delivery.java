package com.mock_ship.domain.delivery;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.event.*;
import com.mock_ship.domain.order.OrderNo;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// Builder pattern을 애그리거트에서 지양하는 이유는 무분별한 객체 생성을 막아준다
// 생성자 시점에서 값을 초기화 시킬수도 없고 내부 검증 로직을 거칠수 없다 -> 팩토리 메서드 사용해서 해결 가능

// Getter와 같은 lombok을 사용하지 않는 이유는 get이나 set보다 직관적인 메서드 이름을 지을 수있다
// 또한 Lazy Loading를 할때 로드 되지 않은 객체를 getter로 부르면 충돌이 날 수 있다.
@Entity
@Table(name = "deliveries")
public class Delivery {

    @EmbeddedId
    private DeliveryNo deliveryNo;

    @Embedded
    private OrderNo orderNo;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "delivery_zip_code")),
            @AttributeOverride(name = "address1", column = @Column(name = "delivery_addr1")),
            @AttributeOverride(name = "address2", column = @Column(name = "delivery_addr2"))
    })
    private Address deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime deliveryDate;
    private LocalDateTime assignedAt;
    private LocalDateTime departedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;

    @Embedded
    private DeliveryAgentId deliveryAgentId;

    private String currentLocation;

    protected Delivery() {}

    private Delivery(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress) {
        if (deliveryNo == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 번호는 필수입니다.");
        if (orderNo == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "주문 번호는 필수입니다.");
        if (deliveryAddress == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 주소는 필수입니다.");

        this.deliveryNo = deliveryNo;
        this.orderNo = orderNo;
        this.deliveryAddress = deliveryAddress;
        this.deliveryStatus = DeliveryStatus.PENDING;
        this.deliveryDate = LocalDateTime.now();

        Events.raise(new DeliveryCreatedEvent(deliveryNo, orderNo, deliveryAddress));
    }

    public static Delivery createDelivery(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress) {
        return new Delivery(deliveryNo, orderNo, deliveryAddress);
    }

    public void startDelivery() {
        if (this.deliveryStatus != DeliveryStatus.PENDING)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 준비 상태에서만 배송을 시작할 수 있습니다.");

        this.deliveryStatus = DeliveryStatus.IN_TRANSIT;
        Events.raise(new DeliveryStartedEvent(this.deliveryNo));
    }

    public void assignAgent(DeliveryAgentId agentId) {
        if (this.deliveryStatus != DeliveryStatus.IN_TRANSIT)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 중 상태에서만 배송 기사를 배정할 수 있습니다.");

        this.deliveryAgentId = agentId;
        this.assignedAt = LocalDateTime.now();
        Events.raise(new DeliveryAssignedEvent(this.deliveryNo, agentId));
    }

    public void departDelivery() {
        if (this.deliveryStatus != DeliveryStatus.IN_TRANSIT || this.deliveryAgentId == null)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 기사가 배정된 상태에서만 출발 가능합니다.");

        this.deliveryStatus = DeliveryStatus.OUT_FOR_DELIVERY;
        this.departedAt = LocalDateTime.now();
        Events.raise(new DeliveryDepartedEvent(this.deliveryNo));
    }

    public void completeDelivery() {
        if (this.deliveryStatus != DeliveryStatus.OUT_FOR_DELIVERY)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 출발 상태에서만 배송 완료 가능합니다.");

        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
        Events.raise(new DeliveryCompletedEvent(this.deliveryNo));
    }

    public void cancelDelivery() {
        if (this.deliveryStatus != DeliveryStatus.PENDING)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 준비 상태에서만 취소할 수 있습니다.");

        this.deliveryStatus = DeliveryStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
        Events.raise(new DeliveryCancelledEvent(this.deliveryNo));
    }

    public void updateLocation(String newLocation) {
        if (this.deliveryStatus != DeliveryStatus.IN_TRANSIT && this.deliveryStatus != DeliveryStatus.OUT_FOR_DELIVERY)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 중 또는 배송 출발 상태에서만 위치를 업데이트할 수 있습니다.");

        this.currentLocation = newLocation;
    }

    public DeliveryNo getDeliveryNo() {
        return deliveryNo;
    }

    public OrderNo getOrderNo() {
        return orderNo;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }
}