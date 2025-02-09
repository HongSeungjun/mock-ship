package com.mock_ship.Tracking.command.domain;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class TrackingNo {
    private String value;

    protected TrackingNo() {}

    public TrackingNo(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TrackingNo는 필수 값입니다.");
        }
        this.value = value;
    }

    public static TrackingNo generate() {
        return new TrackingNo(UUID.randomUUID().toString());
    }
}
