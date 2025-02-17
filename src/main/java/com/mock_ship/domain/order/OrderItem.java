package com.mock_ship.domain.order;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
public class OrderItem {

    private Long productId;
    private String productName;
    private int quantity;
    private double weight;

    protected OrderItem() {
    }

    public OrderItem(Long productId, String productName, int quantity, double weight) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.weight = weight;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getWeight() {
        return weight;
    }

}
