package com.mock_ship.domain.order;


import java.util.Optional;

public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(OrderNo orderNo);
}