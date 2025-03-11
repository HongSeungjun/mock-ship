package com.mock_ship.application.order.usecase;

import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderRepository;
import com.mock_ship.ui.order.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public void execute(OrderRequestDto dto) {
        Order order = new Order(orderRepository.nextOrderNo(), dto.getCustomerId(), dto.getItems());
        orderRepository.save(order);
    }
}