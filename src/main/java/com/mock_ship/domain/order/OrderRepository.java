package com.mock_ship.domain.order;


import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> findById(OrderNo orderNo);

    default OrderNo nextOrderNo() {
        int randomNo = ThreadLocalRandom.current().nextInt(900000) + 100000;
        String number = String.format("%tY%<tm%<td%<tH-%d", new Date(), randomNo);
        return new OrderNo(number);
    }
}