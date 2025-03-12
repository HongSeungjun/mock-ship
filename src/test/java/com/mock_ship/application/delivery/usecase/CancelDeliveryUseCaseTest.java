package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.*;
import com.mock_ship.domain.delivery.event.DeliveryCancelledEvent;
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
class CancelDeliveryUseCaseTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private CancelDeliveryUseCase cancelDeliveryUseCase;

    private Delivery delivery;
    private DeliveryNo deliveryNo;

    @BeforeEach
    void setUp() {
        deliveryNo = DeliveryNo.of("D987654");
        delivery = Delivery.createDelivery(deliveryNo, new OrderNo("O54321"), new Address("54321", "Street 5", "Unit 9"));
    }

    @DisplayName("배송 준비 상태에서 취소하면 상태가 CANCELLED로 변경되고 이벤트가 발행된다.")
    @Test
    void execute_ShouldCancelDeliveryAndRaiseEvent_WhenPending() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        try (MockedStatic<Events> mockedEvents = mockStatic(Events.class)) {
            // When
            cancelDeliveryUseCase.execute(deliveryNo);

            // Then
            assertEquals(DeliveryStatus.CANCELLED, delivery.getDeliveryStatus());

            mockedEvents.verify(() -> Events.raise(any(DeliveryCancelledEvent.class)), times(1));
            verify(deliveryRepository, times(1)).findById(deliveryNo);
            verify(deliveryRepository, times(1)).save(delivery);
        }
    }

    @DisplayName("배송이 이미 진행 중이면 취소할 수 없어야 한다.")
    @Test
    void execute_ShouldThrowException_WhenNotPending() {
        // Given
        delivery.startDelivery(); // 배송 진행 상태로 변경
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> cancelDeliveryUseCase.execute(deliveryNo));
        assertEquals("배송 준비 상태에서만 취소할 수 있습니다.", exception.getMessage());

        verify(deliveryRepository, times(1)).findById(deliveryNo);
        verify(deliveryRepository, never()).save(any());
    }
}