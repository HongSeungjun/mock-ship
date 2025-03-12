package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.*;
import com.mock_ship.domain.delivery.event.DeliveryCompletedEvent;
import com.mock_ship.domain.order.OrderNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteDeliveryUseCaseTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private CompleteDeliveryUseCase completeDeliveryUseCase;

    private Delivery delivery;
    private DeliveryNo deliveryNo;

    @BeforeEach
    void setUp() {
        deliveryNo = DeliveryNo.of("D123456");
        delivery = Delivery.createDelivery(deliveryNo, new OrderNo("O12345"), new Address("12345", "Street 1", "Unit 2"));
        delivery.startDelivery();
        delivery.assignAgent(DeliveryAgentId.of("A001"));
        delivery.departDelivery();
    }

    @DisplayName("배송 완료가 정상적으로 처리되면 상태가 DELIVERED로 변경되고 이벤트가 발행된다.")
    @Test
    void execute_ShouldCompleteDeliveryAndRaiseEvent() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        try (MockedStatic<Events> mockedEvents = mockStatic(Events.class)) {
            // When
            completeDeliveryUseCase.execute(deliveryNo);

            // Then
            assertEquals(DeliveryStatus.DELIVERED, delivery.getDeliveryStatus());

            mockedEvents.verify(() -> Events.raise(any(DeliveryCompletedEvent.class)), times(1));
            verify(deliveryRepository, times(1)).findById(deliveryNo);
        }
    }

    @DisplayName("존재하지 않는 배송을 완료하려 하면 예외를 던진다.")
    @Test
    void execute_ShouldThrowException_WhenDeliveryDoesNotExist() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> completeDeliveryUseCase.execute(deliveryNo));
        assertEquals("해당 배송을 찾을 수 없습니다.", exception.getMessage());

        verify(deliveryRepository, times(1)).findById(deliveryNo);
    }
}