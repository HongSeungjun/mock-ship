package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.domain.delivery.*;
import com.mock_ship.domain.delivery.event.DeliveryCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void execute(DeliveryNo deliveryNo) {
        Delivery delivery = deliveryRepository.findById(deliveryNo)
                .orElseThrow(() -> new ApiException(ExceptionCode.NO_CONTENT, "해당 배송을 찾을 수 없습니다."));

        delivery.completeDelivery();
    }
}