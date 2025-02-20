package com.mock_ship.application.order.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.domain.order.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CancelOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CancelOrderUseCase cancelOrderUseCase;

    private Order order;

    @BeforeEach
    void setUp() {
        OrderNo orderNo = new OrderNo("20240220-001");
        order = new Order(orderNo, "CUSTOMER-001",
                List.of(new OrderItem(1L, "상품1", 1, 1.5))
        );
    }

    @Test
    void 주문을_취소하면_상태가_CANCELED로_변경된다() {
        // given
        when(orderRepository.findById(order.getNumber())).thenReturn(Optional.of(order));

        // when
        cancelOrderUseCase.execute(order.getNumber());

        // then
        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());

        verify(orderRepository, times(1)).findById(order.getNumber());
    }

    @Test
    void 존재하지_않는_주문을_취소하려고하면_예외를_던진다() {
        // given
        when(orderRepository.findById(order.getNumber())).thenReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            cancelOrderUseCase.execute(order.getNumber());
        });

        assertEquals("주문을 찾을 수 없습니다.", exception.getMessage());

        // 검증: findById는 1회 호출되었지만 save는 호출되지 않아야 함
        verify(orderRepository, times(1)).findById(order.getNumber());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void 확정된_주문을_취소하려고하면_예외를_던진다() {
        // given
        order.confirmOrder(); // 주문을 확정 상태로 변경
        when(orderRepository.findById(order.getNumber())).thenReturn(Optional.of(order));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            cancelOrderUseCase.execute(order.getNumber());
        });

        assertEquals("대기 상태의 주문만 취소할 수 있습니다.", exception.getMessage());

        // 검증: findById는 1회 호출되었지만 save는 호출되지 않아야 함
        verify(orderRepository, times(1)).findById(order.getNumber());
        verify(orderRepository, never()).save(any(Order.class));
    }
}
