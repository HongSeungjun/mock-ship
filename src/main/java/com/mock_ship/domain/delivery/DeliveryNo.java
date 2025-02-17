package com.mock_ship.domain.delivery;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
public class DeliveryNo implements Serializable {

    @Column(name = "delivery_no")
    private String number;

    protected DeliveryNo() {

    }

    public DeliveryNo(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeliveryNo that = (DeliveryNo) o;

        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return number != null ? number.hashCode() : 0;
    }

    public static DeliveryNo of(String number) {
        return new DeliveryNo(number);
    }
}
