package com.mock_ship.domain.order;

import com.mock_ship.common.exception.ApiException;
import com.mock_ship.common.exception.ExceptionCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @EmbeddedId
    private OrderNo number;
    private String customerId;

    @ElementCollection(fetch = FetchType.LAZY) // 필요할때만 로딩
    @CollectionTable(name = "order_item", joinColumns = @JoinColumn(name = "order_number")) // 벨류 컬랙션 매핑
    @OrderColumn(name = "item_idx")
    private List<OrderItem> items;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Order(OrderNo number, String customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new ApiException(ExceptionCode.BAD_REQUEST, "주문 항목이 없습니다.");
        }
        this.number = number;
        this.customerId = customerId;
        this.items = items;
        this.orderStatus = OrderStatus.PENDING;
    }

    public void confirmOrder() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new ApiException(ExceptionCode.BAD_REQUEST, "주문을 확정할 수 없는 상태입니다.");
        }
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    public void cancelOrder() {
        if (this.orderStatus != OrderStatus.PENDING) {
            throw new ApiException(ExceptionCode.BAD_REQUEST, "대기 상태의 주문만 취소할 수 있습니다.");
        }
        this.orderStatus = OrderStatus.CANCELED;
    }

}
