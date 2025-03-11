package com.mock_ship.ui.delivery.dto;

import com.mock_ship.common.model.Address;
import com.mock_ship.domain.order.OrderNo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestDto {

    @NotNull(message = "주문 번호는 필수입니다.")
    private String orderNo;

    @NotNull(message = "배송 주소는 필수입니다.")
    private Address deliveryAddress;

    public OrderNo getOrderNoValue() {
        return OrderNo.of(orderNo);
    }
}