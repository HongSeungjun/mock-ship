package com.mock_ship.domain.delivery;


import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public interface DeliveryRepository {
    Optional<Delivery> findById(DeliveryNo deliveryNo);

    Delivery save(Delivery delivery);

    default DeliveryNo nextDeliveryNo() {
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("D-%tY%<tm%<td%<tH-%d", new Date(), randomNo);
        return new DeliveryNo(number);
    }
}