package com.mock_ship.delivery.command.domain;

import com.mock_ship.common.model.Address;
import com.mock_ship.order.command.domain.OrderNo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery")
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
    private TrackingInfo trackingInfo;
}
