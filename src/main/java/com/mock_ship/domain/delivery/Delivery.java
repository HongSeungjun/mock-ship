package com.mock_ship.domain.delivery;

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
    @AttributeOverrides({ // 저장될 컬럼명 지정
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

    protected Delivery() {
    }

    public Delivery(DeliveryNo deliveryNo, OrderNo orderNo, Address deliveryAddress) {
        if (deliveryNo == null) throw new IllegalArgumentException("배송 번호는 필수입니다.");
        if (orderNo == null) throw new IllegalArgumentException("주문 번호는 필수입니다.");
        if (deliveryAddress == null) throw new IllegalArgumentException("배송 주소는 필수입니다.");

        this.deliveryNo = deliveryNo;
        this.orderNo = orderNo;
        this.deliveryAddress = deliveryAddress;
        this.deliveryStatus = DeliveryStatus.PENDING; // 기본 상태 설정
        this.deliveryDate = LocalDateTime.now(); // 현재 시간으로 설정
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


}
