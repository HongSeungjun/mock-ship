package com.mock_ship.application.delivery;

import com.mock_ship.application.delivery.usecase.*;
import com.mock_ship.common.model.Address;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.order.OrderNo;
import com.mock_ship.ui.delivery.dto.DeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryApplicationService {

    private final CreateDeliveryUseCase createDeliveryUseCase;
    private final StartDeliveryUseCase startDeliveryUseCase;
    private final AssignDeliveryAgentUseCase assignDeliveryAgentUseCase;
    private final DepartDeliveryUseCase departDeliveryUseCase;
    private final CompleteDeliveryUseCase completeDeliveryUseCase;
    private final CancelDeliveryUseCase cancelDeliveryUseCase;
    private final GetDeliveryUseCase getDeliveryUseCase;

    public DeliveryNo createDelivery(OrderNo orderNo, Address address) {
        return createDeliveryUseCase.execute(orderNo, address);
    }

    public void startDelivery(String deliveryNo) {
        startDeliveryUseCase.execute(DeliveryNo.of(deliveryNo));
    }

    public void assignAgent(String deliveryNo, String agentId) {
        assignDeliveryAgentUseCase.execute(DeliveryNo.of(deliveryNo), agentId);
    }

    public void departDelivery(String deliveryNo) {
        departDeliveryUseCase.execute(DeliveryNo.of(deliveryNo));
    }

    public void completeDelivery(String deliveryNo) {
        completeDeliveryUseCase.execute(DeliveryNo.of(deliveryNo));
    }

    public void cancelDelivery(String deliveryNo) {
        cancelDeliveryUseCase.execute(DeliveryNo.of(deliveryNo));
    }

    public DeliveryResponseDto getDelivery(String deliveryNo) {
        return getDeliveryUseCase.execute(DeliveryNo.of(deliveryNo));
    }
}