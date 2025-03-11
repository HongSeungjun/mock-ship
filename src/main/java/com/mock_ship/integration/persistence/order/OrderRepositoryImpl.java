package com.mock_ship.integration.persistence.order;

import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public void save(Order order) {
        orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(OrderNo orderNo) {
        return orderJpaRepository.findById(orderNo);
    }
}
