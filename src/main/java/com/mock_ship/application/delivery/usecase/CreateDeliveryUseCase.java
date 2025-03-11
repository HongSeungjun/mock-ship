package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.Delivery;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryRepository;
import com.mock_ship.domain.delivery.event.DeliveryCreatedEvent;
import com.mock_ship.domain.order.OrderNo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateDeliveryUseCase {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryNo execute(OrderNo orderNo, Address deliveryAddress) {
        DeliveryNo deliveryNo = deliveryRepository.nextDeliveryNo();
        Delivery delivery = Delivery.createDelivery(deliveryNo, orderNo, deliveryAddress);
        deliveryRepository.save(delivery);

        Events.raise(new DeliveryCreatedEvent(deliveryNo, orderNo, deliveryAddress));

        return deliveryNo;
    }
}