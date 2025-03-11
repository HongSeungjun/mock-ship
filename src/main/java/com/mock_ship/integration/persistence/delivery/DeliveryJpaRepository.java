package com.mock_ship.integration.persistence.delivery;

import com.mock_ship.domain.delivery.Delivery;
import com.mock_ship.domain.delivery.DeliveryNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryJpaRepository extends JpaRepository<Delivery, DeliveryNo> {
    Optional<Delivery> findByDeliveryNo(DeliveryNo deliveryNo);

}
