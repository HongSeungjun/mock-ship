package com.mock_ship.delivery.command.domain;

import com.mock_ship.common.model.Address;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
// TODO : 빌더 패턴이 나을것인가??
//@Builder
public class Delivery {
    @EmbeddedId
    private DeliveryNo deliveryNo;

    // TODO : 객체 연관 관계
    private String orderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipCode", column = @Column(name = "delivery_zip_code")),
            @AttributeOverride(name = "address1", column = @Column(name = "delivery_addr1")),
            @AttributeOverride(name = "address2", column = @Column(name = "delivery_addr2"))
    })
    private Address deliveryAddress;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime deliveryDate;
    @Embedded
    private TrackingInfo trackingInfo;
}
