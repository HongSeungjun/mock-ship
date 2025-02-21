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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConfirmOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ConfirmOrderUseCase confirmOrderUseCase;

    private Order order;

    @BeforeEach
    void setUp() {
        OrderNo orderNo = new OrderNo("20240220-001");
        order = new Order(orderNo, "CUSTOMER-001",
                List.of(new OrderItem(1L, "상품1", 1, 1.5))
        );
    }

    @DisplayName("주문을 확정하면 상태가 CONFIRMED 상태로 변경된다.")
    @Test
    void confirmOrder_ShouldChangeStatusToConfirmed() {
        //given
        when(orderRepository.findById(order.getNumber())).thenReturn(Optional.of(order));

        //when
        confirmOrderUseCase.execute(order.getNumber());

        //then
        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());
    }

    @DisplayName("존재하지 않는 주문을 확정하려고 하면 예외를 던진다")
    @Test
    void confirmNonExistentOrder_ShouldThrowException() {
        // given
        when(orderRepository.findById(order.getNumber())).thenReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            confirmOrderUseCase.execute(order.getNumber());
        });

        assertEquals("주문을 찾을 수 없습니다.", exception.getMessage());

        // 검증
        verify(orderRepository, times(1)).findById(order.getNumber());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @DisplayName("취소상태의 주문을 확정하려고 하면 예외를 던진다")
    @Test
    void confirmCanceledOrder_ShouldThrowException() {
        order.cancelOrder();
        // given
        when(orderRepository.findById(order.getNumber())).thenReturn(Optional.of(order));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            confirmOrderUseCase.execute(order.getNumber());
        });

        assertEquals("주문을 확정할 수 없는 상태입니다.", exception.getMessage());

        // 검증
        verify(orderRepository, times(1)).findById(order.getNumber());
        verify(orderRepository, never()).save(any(Order.class));
    }

}