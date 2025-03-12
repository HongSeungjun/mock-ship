package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.event.Events;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.*;
import com.mock_ship.domain.delivery.event.DeliveryCreatedEvent;
import com.mock_ship.domain.order.OrderNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDeliveryUseCaseTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private Events events;

    @InjectMocks
    private CreateDeliveryUseCase createDeliveryUseCase;

    private DeliveryNo deliveryNo;
    private OrderNo orderNo;
    private Address deliveryAddress;

    @BeforeEach
    void setUp() {
        deliveryNo = DeliveryNo.of("D20250312-001");
        orderNo = OrderNo.of("20240221-001");
        deliveryAddress = new Address("07035", "동작구", "상도동");
    }

    @Test
    @DisplayName("배송을 생성하면 DeliveryNo를 반환하고 이벤트가 발행된다.")
    void shouldCreateDeliveryAndPublishEvent() {
        // given
        Delivery delivery = Delivery.createDelivery(deliveryNo, orderNo, deliveryAddress);
        given(deliveryRepository.nextDeliveryNo()).willReturn(deliveryNo);
        given(deliveryRepository.save(any(Delivery.class))).willReturn(delivery);

        // when
        try (MockedStatic<Events> mockedEvents = Mockito.mockStatic(Events.class)) {

            DeliveryNo createdDeliveryNo = createDeliveryUseCase.execute(orderNo, deliveryAddress);

            // then
            assertThat(createdDeliveryNo).isNotNull().isEqualTo(deliveryNo);
            then(deliveryRepository).should(times(1)).save(any(Delivery.class));

            ArgumentCaptor<DeliveryCreatedEvent> eventCaptor = ArgumentCaptor.forClass(DeliveryCreatedEvent.class);
            mockedEvents.verify(() -> Events.raise(eventCaptor.capture()), times(1));

            DeliveryCreatedEvent raisedEvent = eventCaptor.getValue();
            assertThat(raisedEvent).isNotNull();
            assertThat(raisedEvent.getDeliveryNo()).isEqualTo(deliveryNo);
            assertThat(raisedEvent.getOrderNo()).isEqualTo(orderNo);
        }
    }
}