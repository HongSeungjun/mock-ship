package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.Delivery;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryRepository;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.ui.delivery.dto.DeliveryResponseDto;
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
class GetDeliveryUseCaseTest {


    @Mock
    private DeliveryRepository deliveryRepository;

    @InjectMocks
    private GetDeliveryUseCase getDeliveryUseCase;

    private Delivery delivery;
    private DeliveryNo deliveryNo;
    private OrderNo orderNo;
    private Address address;


    @BeforeEach
    void setUp() {
        deliveryNo = DeliveryNo.of("D20250312-001");
        orderNo = OrderNo.of("20240221-001");
        address = new Address("07035", "동작구", "상도동");
        delivery = Delivery.createDelivery(deliveryNo, orderNo, address);
    }

    @DisplayName("배송 번호를 통해 배송 내역을 조회 할 수 있다.")
    @Test
    void execute_ShouldReturnDeliveryResponseDto_WhenDeliveryExists() {
        // given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.of(delivery));

        // when
        DeliveryResponseDto responseDto = getDeliveryUseCase.execute(deliveryNo);

        // then
        assertNotNull(responseDto);
        assertEquals(responseDto.getDeliveryNo(), delivery.getDeliveryNo().getNumber());

        verify(deliveryRepository, times(1)).findById(deliveryNo);
    }
    @DisplayName("존재하지 않는 배송정보를 조회하면 예외를 던진다")
    @Test
    void execute_ShouldThrowException_WhenDeliveryDoesNotExist() {
        // Given
        when(deliveryRepository.findById(deliveryNo)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> getDeliveryUseCase.execute(deliveryNo));

        assertEquals("해당 배송을 찾을 수 없습니다.", exception.getMessage());
        verify(deliveryRepository, times(1)).findById(deliveryNo);
    }
}