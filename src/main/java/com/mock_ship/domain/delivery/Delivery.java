package com.mock_ship.domain.delivery;

import com.mock_ship.common.event.DomainEventPublisher;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.event.DeliveryAssignedEvent;
import com.mock_ship.domain.delivery.event.DeliveryCompletedEvent;
import com.mock_ship.domain.delivery.event.DeliveryCreatedEvent;
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

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime deliveryDate;

    @Embedded
    private DeliveryAgentId deliveryAgentId;

    private LocalDateTime assignedAt;
    private LocalDateTime departedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;

    private String currentLocation;

    protected Delivery() {} // JPA용 기본 생성자

    private Delivery(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress) {
        if (deliveryNo == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 번호는 필수입니다.");
        if (orderNo == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "주문 번호는 필수입니다.");
        if (deliveryAddress == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 주소는 필수입니다.");

        this.deliveryNo = deliveryNo;
        this.orderNo = orderNo;
        this.deliveryAddress = deliveryAddress;
        this.deliveryStatus = DeliveryStatus.IN_TRANSIT; // 주문 확정 후 배송 시작
        this.deliveryDate = LocalDateTime.now();

        // 배송 생성 이벤트 발생
        DomainEventPublisher.publish(new DeliveryCreatedEvent(deliveryNo, orderNo, deliveryAddress));
    }

    /**
     * 배송 생성 (주문 확정 후 자동으로 호출됨)
     */
    public static Delivery createDelivery(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress) {
        return new Delivery(deliveryNo, orderNo, deliveryAddress);
    }

    /**
     * 배송 기사 배정 및 배송 출발
     */
    public void assignAgentAndStartDelivery(DeliveryAgentId agentId) {
        if (this.deliveryStatus != DeliveryStatus.IN_TRANSIT)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 출발은 배송 중(IN_TRANSIT) 상태에서만 가능합니다.");

        if (agentId == null)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 기사 정보가 필요합니다.");

        this.deliveryAgentId = agentId;
        this.deliveryStatus = DeliveryStatus.OUT_FOR_DELIVERY;
        this.assignedAt = LocalDateTime.now();
        this.departedAt = LocalDateTime.now();

        DomainEventPublisher.publish(new DeliveryAssignedEvent(this.deliveryNo, agentId));
    }

    /**
     * 배송 완료
     */
    public void completeDelivery() {
        if (this.deliveryStatus != DeliveryStatus.OUT_FOR_DELIVERY)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 출발 상태에서만 배송 완료 가능합니다.");

        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();

        DomainEventPublisher.publish(new DeliveryCompletedEvent(this.deliveryNo));
    }

    /**
     * 배송 취소 (PENDING 상태일 때만 가능)
     */
    public void cancelDelivery() {
        if (this.deliveryStatus != DeliveryStatus.PENDING)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 준비 중 상태에서만 취소 가능합니다.");

        this.deliveryStatus = DeliveryStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    /**
     * 실시간 위치 업데이트 (배송 중, 배송 출발 상태에서만 가능)
     */
    public void updateLocation(String newLocation) {
        if (this.deliveryStatus != DeliveryStatus.IN_TRANSIT && this.deliveryStatus != DeliveryStatus.OUT_FOR_DELIVERY)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 중(IN_TRANSIT) 또는 배송 출발(OUT_FOR_DELIVERY) 상태에서만 위치를 업데이트할 수 있습니다.");

        this.currentLocation = newLocation;
    }
}