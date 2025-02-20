package com.mock_ship.application.order.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.domain.order.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CancelOrderUseCaseIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    private CancelOrderUseCase cancelOrderUseCase;

    @PersistenceContext
    private EntityManager entityManager;

    private OrderNo orderNo;

    @BeforeEach
    void setUp() {
        cancelOrderUseCase = new CancelOrderUseCase(orderRepository);

        // Given
        orderNo = orderRepository.nextOrderNo();
        Order order = new Order(orderNo, "CUSTOMER-002",
                List.of(new OrderItem(2L, "상품2", 1, 2.0))
        );

        orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void 주문을_취소하면_DB에서도_상태가_CANCELED로_변경된다() {
        // When
        cancelOrderUseCase.execute(orderNo);

        // Then
        Order canceledOrder = orderRepository.findById(orderNo)
                .orElseThrow(() -> new RuntimeException("주문이 DB에서 사라졌습니다."));

        assertEquals(OrderStatus.CANCELED, canceledOrder.getOrderStatus());
    }
}
