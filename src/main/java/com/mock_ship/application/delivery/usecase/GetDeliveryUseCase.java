package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.domain.delivery.Delivery;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryRepository;
import com.mock_ship.ui.delivery.dto.DeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    public DeliveryResponseDto execute(DeliveryNo deliveryNo) {
        Delivery delivery = deliveryRepository.findById(deliveryNo)
                .orElseThrow(() -> new ApiException(ExceptionCode.NO_CONTENT, "해당 배송을 찾을 수 없습니다."));
        return DeliveryResponseDto.from(delivery);
    }
}