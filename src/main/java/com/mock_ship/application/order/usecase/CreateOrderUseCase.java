package com.mock_ship.application.order.usecase;

import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.domain.order.OrderRepository;
import com.mock_ship.ui.order.dto.OrderRequestDto;
import com.mock_ship.ui.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public OrderResponseDto execute(OrderRequestDto dto) {
        Order order = new Order(OrderNo.of(dto.getOrderNo()), dto.getCustomerId(), dto.getItems());
        orderRepository.save(order);
        return new OrderResponseDto(order);
    }
}