package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.*;
import com.mock_ship.domain.delivery.event.DeliveryDepartedEvent;
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
class DepartDeliveryUseCaseTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private DepartDeliveryUseCase departDeliveryUseCase;

    private Delivery delivery;
    private DeliveryNo deliveryNo;
    private DeliveryAgentId agentId;

    @BeforeEach
    void setUp() {
        agentId = DeliveryAgentId.of("seoul-001");
        deliveryNo = DeliveryNo.of("D20250312-001");
        delivery = Delivery.createDelivery(deliveryNo, OrderNo.of("20240221-001"), new Address("07035", "동작구", "상도동"));

        delivery.startDelivery();
        delivery.assignAgent(agentId);
    }

    @DisplayName("배송 출발이 정상적으로 실행되면 이벤트가 발행된다.")
    @Test
    void execute_ShouldDepartDeliveryAndRaiseEvent_WhenDeliveryIsInTransitAndAgentAssigned() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        try (MockedStatic<Events> mockedEvents = Mockito.mockStatic(Events.class)) {
            // When
            departDeliveryUseCase.execute(deliveryNo);

            // Then
            assertEquals(DeliveryStatus.OUT_FOR_DELIVERY, delivery.getDeliveryStatus());


            // 이벤트 발생 검증
            ArgumentCaptor<DeliveryDepartedEvent> eventCaptor = ArgumentCaptor.forClass(DeliveryDepartedEvent.class);
            mockedEvents.verify(() -> Events.raise(eventCaptor.capture()), times(1));

            DeliveryDepartedEvent capturedEvent = eventCaptor.getValue();
            assertEquals(deliveryNo, capturedEvent.getDeliveryNo());

            verify(deliveryRepository, times(1)).findById(deliveryNo);
        }
    }

    @DisplayName("배송 기사가 배정되지 않은 경우 출발할 수 없다.")
    @Test
    void execute_ShouldThrowException_WhenAgentNotAssigned() {
        // Given: 배송을 시작했지만 기사가 배정되지 않은 상태로 변경
        delivery = Delivery.createDelivery(deliveryNo, new OrderNo("ORD-20240312-002"),
                new Address("67890", "Busan", "Haeundae"));
        delivery.startDelivery();

        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        try (MockedStatic<Events> mockedEvents = mockStatic(Events.class)) {

            // When & Then
            ApiException exception = assertThrows(ApiException.class, () -> departDeliveryUseCase.execute(deliveryNo));


            assertEquals("배송 기사가 배정된 상태에서만 출발 가능합니다.", exception.getMessage());

            verify(deliveryRepository, times(1)).findById(deliveryNo);

            mockedEvents.verifyNoInteractions();
        }

    }

    @DisplayName("배송이 IN_TRANSIT 상태가 아닌 경우 출발할 수 없다.")
    @Test
    void execute_ShouldThrowException_WhenDeliveryIsNotInTransit() {
        // Given: 배송이 PENDING 상태인 경우
        delivery = Delivery.createDelivery(deliveryNo, new OrderNo("ORD-20240312-003"),
                new Address("11111", "Daegu", "Suseong"));

        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));
        try (MockedStatic<Events> mockedEvents = mockStatic(Events.class)) {

            // When & Then
            ApiException exception = assertThrows(ApiException.class, () -> departDeliveryUseCase.execute(deliveryNo));


            assertEquals("배송 기사가 배정된 상태에서만 출발 가능합니다.", exception.getMessage());

            verify(deliveryRepository, times(1)).findById(deliveryNo);
            mockedEvents.verifyNoInteractions();
        }
    }

    @DisplayName("존재하지 않는 배송번호로 출발 요청하면 예외를 던진다.")
    @Test
    void execute_ShouldThrowException_WhenDeliveryDoesNotExist() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.empty());

        try (MockedStatic<Events> mockedEvents = mockStatic(Events.class)) {

            // When & Then
            ApiException exception = assertThrows(ApiException.class, () -> departDeliveryUseCase.execute(deliveryNo));

            assertEquals("해당 배송을 찾을 수 없습니다.", exception.getMessage());

            verify(deliveryRepository, times(1)).findById(deliveryNo);
            mockedEvents.verifyNoInteractions();
        }
    }
}