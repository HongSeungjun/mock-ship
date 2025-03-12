package com.mock_ship.application.delivery.usecase;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.domain.delivery.Delivery;
import com.mock_ship.domain.delivery.DeliveryNo;
import com.mock_ship.domain.delivery.DeliveryRepository;
import com.mock_ship.domain.delivery.DeliveryAgentId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignDeliveryAgentUseCase {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public void execute(DeliveryNo deliveryNo, String agentId) {
        Delivery delivery = deliveryRepository.findById(deliveryNo)
                .orElseThrow(() -> new ApiException(ExceptionCode.NO_CONTENT, "해당 배송을 찾을 수 없습니다."));
        delivery.assignAgent(DeliveryAgentId.of(agentId));
    }
}