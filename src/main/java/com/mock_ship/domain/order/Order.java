package com.mock_ship.domain.order;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import com.mock_ship.domain.order.event.OrderCanceledEvent;
import com.mock_ship.domain.order.event.OrderConfirmedEvent;
import com.mock_ship.common.event.Events;
import jakarta.persistence.*;

import java.util.List;

/**
 * 주문 애그리게잇
 */
@Entity
@Table(name = "orders")
public class Order {

    @EmbeddedId
    private OrderNo number;

    private String customerId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_item", joinColumns = @JoinColumn(name = "order_number"))
    @OrderColumn(name = "item_idx")
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    protected Order() {
        // JPA 기본 생성자
    }

    public Order(OrderNo number, String customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new ApiException(ExceptionCode.BAD_REQUEST, "주문 항목이 없습니다.");
        }
        this.number = number;
        this.customerId = customerId;
        this.items = items;
        this.orderStatus = OrderStatus.PENDING;
    }

    /** 주문 확정 */
    public void confirm() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new ApiException(ExceptionCode.BAD_REQUEST, "주문을 확정할 수 없는 상태입니다.");
        }
        orderStatus = OrderStatus.CONFIRMED;

        // 주문 확정 이벤트 발행
        Events.raise(new OrderConfirmedEvent(number));
    }

    /** 주문 취소 */
    public void cancel() {
        if (orderStatus != OrderStatus.PENDING) {
            throw new ApiException(ExceptionCode.BAD_REQUEST, "대기 상태의 주문만 취소할 수 있습니다.");
        }
        orderStatus = OrderStatus.CANCELED;

        // 주문 취소 이벤트 발행
        Events.raise(new OrderCanceledEvent(number));
    }

    /** 주문번호 반환 */
    public OrderNo getOrderNo() {
        return number;
    }

    /** 주문 상태 반환 */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /** 고객 ID 반환 */
    public String getCustomerId() {
        return customerId;
    }

    /** 주문 아이템 반환 */
    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }
}