package com.mock_ship.domain.delivery;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.domain.tracking.TrackingNo;
import com.mock_ship.common.model.Address;
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
    private TrackingNo trackingNo;

    @Embedded
    private DeliveryAgentId deliveryAgentId;

    private LocalDateTime assignedAt;
    private LocalDateTime departedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime cancelledAt;

    private String currentLocation;

    protected Delivery() {}

    private Delivery(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress, DeliveryAgentId deliveryAgentId) {
        if (deliveryNo == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 번호는 필수입니다.");
        if (orderNo == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "주문 번호는 필수입니다.");
        if (deliveryAddress == null) throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 주소는 필수입니다.");

        this.deliveryNo = deliveryNo;
        this.orderNo = orderNo;
        this.deliveryAddress = deliveryAddress;
        this.deliveryStatus = DeliveryStatus.PENDING;
        this.deliveryDate = LocalDateTime.now();
        this.deliveryAgentId = deliveryAgentId;
        this.currentLocation = "배송 준비 중";
    }

    public static Delivery createDelivery(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress, DeliveryAgentId deliveryAgentId) {
        return new Delivery(deliveryNo, orderNo, deliveryAddress, deliveryAgentId);
    }

    public void assignDeliveryAgent(DeliveryAgentId agentId) {
        if (this.deliveryStatus != DeliveryStatus.PENDING)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 준비중일 때만 배정이 가능합니다.");

        this.deliveryAgentId = agentId;
        this.deliveryStatus = DeliveryStatus.IN_TRANSIT;
        this.assignedAt = LocalDateTime.now();
    }

    public void markOutForDelivery() {
        if (this.deliveryStatus != DeliveryStatus.IN_TRANSIT)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 중(IN_TRANSIT) 상태일 때만 출발할 수 있습니다.");

        this.deliveryStatus = DeliveryStatus.OUT_FOR_DELIVERY;
        this.departedAt = LocalDateTime.now();
    }

    public void completeDelivery() {
        if (this.deliveryStatus != DeliveryStatus.OUT_FOR_DELIVERY)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 출발 상태(OUT_FOR_DELIVERY)일 때만 완료할 수 있습니다.");

        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = LocalDateTime.now();
    }

    public void cancelDelivery() {
        if (this.deliveryStatus != DeliveryStatus.PENDING)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 준비중일 때만 취소가 가능합니다.");

        this.deliveryStatus = DeliveryStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }

    public void updateLocation(String newLocation) {
        if (this.deliveryStatus != DeliveryStatus.IN_TRANSIT && this.deliveryStatus != DeliveryStatus.OUT_FOR_DELIVERY)
            throw new ApiException(ExceptionCode.BAD_REQUEST, "배송 중(IN_TRANSIT) 또는 배송 출발(OUT_FOR_DELIVERY) 상태일 때만 위치를 업데이트할 수 있습니다.");

        this.currentLocation = newLocation;
    }

    public DeliveryStatus status() {
        return deliveryStatus;
    }

    public String currentLocation() {
        return currentLocation;
    }

    public DeliveryAgentId assignedAgent() {
        return deliveryAgentId;
    }
}
