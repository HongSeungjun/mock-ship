package com.mock_ship.delivery.command.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@AllArgsConstructor
public class TrackingInfo {

    private String trackingNumber;
    // TODO : 좌표를 어떻게 표시 할 것인지
    private String currentLocation;
    private LocalDateTime lastUpdated;

    protected TrackingInfo() {
    }
}
