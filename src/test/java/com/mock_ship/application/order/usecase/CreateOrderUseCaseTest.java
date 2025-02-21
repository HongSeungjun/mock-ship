package com.mock_ship.application.order.usecase;

import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderItem;
import com.mock_ship.domain.order.OrderRepository;
import com.mock_ship.ui.order.dto.OrderRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private OrderRequestDto orderRequestDto;

    @BeforeEach
    void setUp() {
        List<OrderItem> orderItems = List.of(
                new OrderItem(1L, "상품1", 2, 10000),
                new OrderItem(2L, "상품2", 1, 5000)
        );
        orderRequestDto = new OrderRequestDto("20240221-001", "CUSTOMER-001", orderItems);
    }

    @DisplayName("새로운 주문을 생성하면 저장 메서드가 호출된다")
    @Test
    void execute_ShouldSaveOrder() {
        // When
        createOrderUseCase.execute(orderRequestDto);

        // Then
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
