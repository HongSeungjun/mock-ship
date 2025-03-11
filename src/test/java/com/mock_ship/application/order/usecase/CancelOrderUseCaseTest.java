package com.mock_ship.application.order.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.domain.order.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("주문을 취소하면 상태가 CANCELED로 변경된다")
    @Test
    void cancelOrder_ShouldChangeStatusToCanceled() {
        // given
        when(orderRepository.findById(order.getOrderNo())).thenReturn(Optional.of(order));

        // when
        cancelOrderUseCase.execute(order.getOrderNo());

        // then
        assertEquals(OrderStatus.CANCELED, order.getOrderStatus());

        verify(orderRepository, times(1)).findById(order.getOrderNo());
    }

    @DisplayName("존재하지 않는 주문을 취소하려고 하면 예외를 던진다")
    @Test
    void cancelNonExistentOrder_ShouldThrowException() {
        // given
        when(orderRepository.findById(order.getOrderNo())).thenReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            cancelOrderUseCase.execute(order.getOrderNo());
        });

        assertEquals("주문을 찾을 수 없습니다.", exception.getMessage());

        // 검증
        verify(orderRepository, times(1)).findById(order.getOrderNo());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @DisplayName("확정된 주문을 취소하려고 하면 예외를 던진다")
    @Test
    void cancelConfirmedOrder_ShouldThrowException() {
        // given
        order.confirm(); // 주문을 확정 상태로 변경
        when(orderRepository.findById(order.getOrderNo())).thenReturn(Optional.of(order));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            cancelOrderUseCase.execute(order.getOrderNo());
        });

        assertEquals("대기 상태의 주문만 취소할 수 있습니다.", exception.getMessage());

        // 검증
        verify(orderRepository, times(1)).findById(order.getOrderNo());
        verify(orderRepository, never()).save(any(Order.class));
    }
}
