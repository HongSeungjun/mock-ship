package com.mock_ship.integration.persistence;

import com.mock_ship.domain.order.Order;
import com.mock_ship.domain.order.OrderNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<Order, OrderNo>{

}