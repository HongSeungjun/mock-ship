package com.mock_ship.application.order;

import com.mock_ship.application.order.usecase.CancelOrderUseCase;
import com.mock_ship.application.order.usecase.ConfirmOrderUseCase;
import com.mock_ship.application.order.usecase.CreateOrderUseCase;
import com.mock_ship.application.order.usecase.GetOrderUseCase;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.ui.order.dto.OrderRequestDto;
import com.mock_ship.ui.order.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final CreateOrderUseCase createOrderUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderResponseDto createOrder(OrderRequestDto dto) {
        return createOrderUseCase.execute(dto);
    }

    public void confirmOrder(String orderNo) {
        confirmOrderUseCase.execute(OrderNo.of(orderNo));
    }

    public void cancelOrder(String orderNo) {
        cancelOrderUseCase.execute(OrderNo.of(orderNo));
    }

    public OrderResponseDto getOrder(String orderNo) {
        return getOrderUseCase.execute(OrderNo.of(orderNo));
    }

}
