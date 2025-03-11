package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.domain.delivery.*;
import com.mock_ship.domain.delivery.event.DeliveryStartedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StartDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void execute(DeliveryNo deliveryNo) {
        Delivery delivery = deliveryRepository.findById(deliveryNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송을 찾을 수 없습니다."));

        delivery.startDelivery();
        deliveryRepository.save(delivery);

        // 이벤트 발행
        Events.raise(new DeliveryStartedEvent(deliveryNo));
    }
}