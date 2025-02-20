package com.mock_ship.domain.tracking;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class TrackingNo {
    private String number;

    protected TrackingNo() {}

    public TrackingNo(String number) {
        if (number == null || number.isBlank()) {
            throw new IllegalArgumentException("TrackingNo는 필수 값입니다.");
        }
        this.number = number;
    }

    public static TrackingNo generate() {
        return new TrackingNo(UUID.randomUUID().toString());
    }
}
