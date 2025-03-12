package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.*;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.domain.order.OrderNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignDeliveryAgentUseCaseTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private AssignDeliveryAgentUseCase assignDeliveryAgentUseCase;

    private Delivery delivery;
    private DeliveryNo deliveryNo;
    private DeliveryAgentId agentId;

    @BeforeEach
    void setUp() {
        deliveryNo = DeliveryNo.of("D567890");
        agentId = DeliveryAgentId.of("A002");
        delivery = Delivery.createDelivery(deliveryNo, new OrderNo("O67890"), new Address("67890", "Street 6", "Unit 8"));
        delivery.startDelivery(); // IN_TRANSIT 상태로 변경
    }

    @DisplayName("배송 중 상태일 때 배송 기사를 배정하면 정상적으로 설정된다.")
    @Test
    void execute_ShouldAssignAgent_WhenDeliveryIsInTransit() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        // When
        assignDeliveryAgentUseCase.execute(deliveryNo, agentId.getId());

        // Then
        assertEquals(agentId, delivery.getDeliveryAgentId());
        verify(deliveryRepository, times(1)).findById(deliveryNo);
    }

    @DisplayName("배송이 배송 중 상태가 아니면 기사 배정을 할 수 없다.")
    @Test
    void execute_ShouldThrowException_WhenDeliveryIsNotInTransit() {
        // Given
        delivery = Delivery.createDelivery(deliveryNo, new OrderNo("ORD-20240312-003"),
                new Address("11111", "Daegu", "Suseong"));

        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> assignDeliveryAgentUseCase.execute(deliveryNo, agentId.getId()));
        assertEquals("배송 중 상태에서만 배송 기사를 배정할 수 있습니다.", exception.getMessage());

        verify(deliveryRepository, times(1)).findById(deliveryNo);
    }
}