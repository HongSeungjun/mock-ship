package com.mock_ship.delivery.command.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus {
    PENDING(0, "pending","배송 전"),
    IN_TRANSIT(1, "in_transit","배송 중"),
    DELIVERED(2, "delivered","배송 완료"),
    CANCELLED(3, "cancelled","취소");

    private Integer value;
    private String name;
    private String description;

}
