package com.mock_ship.application.order.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmOrderUseCase {

    private final OrderRepository orderRepository;

    @Transactional
    public void execute(OrderNo orderNo) {
        Order order = orderRepository.findById(orderNo)
                .orElseThrow(() -> new ApiException(ExceptionCode.ORDER_NO_CONTENT,"주문을 찾을 수 없습니다."));
        order.confirm();
    }

}
