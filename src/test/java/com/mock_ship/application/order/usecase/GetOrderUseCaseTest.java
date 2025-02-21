package com.mock_ship.application.order.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderItem;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.domain.order.OrderRepository;
import com.mock_ship.ui.order.dto.OrderResponseDto;
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
class GetOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private GetOrderUseCase getOrderUseCase;

    private Order order;
    private OrderNo orderNo;

    @BeforeEach
    void setUp() {
        orderNo = new OrderNo("20240221-001");
        List<OrderItem> items = List.of(
                new OrderItem(1L, "상품1", 2, 10000),
                new OrderItem(2L, "상품2", 1, 5000)
        );
        order = new Order(orderNo, "CUSTOMER-001", items);
    }

    @DisplayName("존재하는 주문을 조회하면 OrderResponseDto가 반환된다")
    @Test
    void execute_ShouldReturnOrderResponseDto_WhenOrderExists() {
        // Given
        when(orderRepository.findById(orderNo)).thenReturn(Optional.of(order));

        // When
        OrderResponseDto responseDto = getOrderUseCase.execute(orderNo);

        // Then
        assertNotNull(responseDto);
        assertEquals(order.getNumber().getNumber(), responseDto.getOrderNo());
        assertEquals(order.getCustomerId(), responseDto.getCustomerId());
        assertEquals(order.getItems().size(), responseDto.getOrderItems().size());

        verify(orderRepository, times(1)).findById(orderNo);
    }

    @DisplayName("존재하지 않는 주문을 조회하면 예외를 던진다")
    @Test
    void execute_ShouldThrowException_WhenOrderDoesNotExist() {
        // Given
        when(orderRepository.findById(orderNo)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> getOrderUseCase.execute(orderNo));

        assertEquals("주문을 찾을 수 없습니다.", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderNo);
    }
}
