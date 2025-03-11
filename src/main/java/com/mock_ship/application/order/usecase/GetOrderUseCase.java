package com.mock_ship.application.order.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.domain.order.OrderRepository;
import com.mock_ship.ui.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOrderUseCase {
    private final OrderRepository orderRepository;

    public OrderResponseDto execute(OrderNo orderNo) {
        Order order = orderRepository.findById(orderNo)
                .orElseThrow(() -> new ApiException(ExceptionCode.NO_CONTENT,"주문을 찾을 수 없습니다."));
        return new OrderResponseDto(order);

    }
}
