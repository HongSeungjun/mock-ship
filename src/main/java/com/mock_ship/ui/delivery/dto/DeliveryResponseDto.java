package com.mock_ship.ui.delivery.dto;

import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.Delivery;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeliveryResponseDto {
    private String deliveryNo;
    private String orderNo;
    private String deliveryStatus;
    private Address deliveryAddress;
    private LocalDateTime deliveryDate;
    private String currentLocation;

    public static DeliveryResponseDto from(Delivery delivery) {
        return new DeliveryResponseDto(
                delivery.getDeliveryNo().getNumber(),
                delivery.getOrderNo().getNumber(),
                delivery.getDeliveryStatus().name(),
                delivery.getDeliveryAddress(),
                delivery.getDeliveryDate(),
                delivery.getCurrentLocation()
        );
    }
}