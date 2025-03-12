package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.Delivery;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryRepository;
import com.mock_ship.domain.delivery.DeliveryStatus;
import com.mock_ship.domain.delivery.event.DeliveryStartedEvent;
import com.mock_ship.domain.order.OrderNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartDeliveryUseCaseTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private StartDeliveryUseCase startDeliveryUseCase;

    private Delivery delivery;
    private DeliveryNo deliveryNo;
    private Address address;

    private OrderNo orderNo;

    @BeforeEach
    void setUp() {
        deliveryNo = DeliveryNo.of("D20250312-001");
        orderNo = OrderNo.of("20240221-001");
        address = new Address("07035", "동작구", "상도동");
        delivery = Delivery.createDelivery(deliveryNo, orderNo, address);
    }


    @DisplayName("배송 정보가 존재할 때 배송을 시작하면 상태가 IN_TRANSIT으로 변경되고 이벤트가 발생한다.")
    @Test
    void execute_ShouldStartDeliveryAndRaiseEvent_WhenDeliveryExists() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        try (MockedStatic<Events> mockedEvents = Mockito.mockStatic(Events.class)) {
            // When
            startDeliveryUseCase.execute(deliveryNo);

            // Then
            assertEquals(DeliveryStatus.IN_TRANSIT, delivery.getDeliveryStatus());
            verify(deliveryRepository, times(1)).save(delivery);

            // 이벤트 발생 검증
            ArgumentCaptor<DeliveryStartedEvent> eventCaptor = ArgumentCaptor.forClass(DeliveryStartedEvent.class);
            mockedEvents.verify(() -> Events.raise(eventCaptor.capture()), times(1));

            DeliveryStartedEvent event = eventCaptor.getValue();
            assertEquals(deliveryNo, event.getDeliveryNo());
        }
    }

    @DisplayName("존재하지 않는 배송을 시작하려 하면 예외를 던진다.")
    @Test
    void execute_ShouldThrowException_WhenDeliveryDoesNotExist() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> startDeliveryUseCase.execute(deliveryNo));

        assertEquals("해당 배송을 찾을 수 없습니다.", exception.getMessage());
        verify(deliveryRepository, times(1)).findById(deliveryNo);
        verify(deliveryRepository, never()).save(any());
    }
}