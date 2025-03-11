package com.mock_ship.application.delivery.usecase;

import com.mock_ship.domain.delivery.Delivery;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void execute(DeliveryNo deliveryNo) {
        Delivery delivery = deliveryRepository.findById(deliveryNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송을 찾을 수 없습니다."));
        delivery.departDelivery();
    }
}