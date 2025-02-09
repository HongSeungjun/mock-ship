package com.mock_ship.Tracking.command.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tracking")
public class Tracking {
    @EmbeddedId
    private TrackingNo trackingNo;

    private String deliveryId; // Delivery 애그리게잇과 ID로 연관

    private String currentLocation;
    private LocalDateTime lastUpdated;

    protected Tracking() {}

    public Tracking(TrackingNo trackingNo, String deliveryId, String currentLocation) {
        if (trackingNo == null) throw new IllegalArgumentException("추적 번호는 필수입니다.");
        if (deliveryId == null || deliveryId.isBlank()) throw new IllegalArgumentException("배송 ID는 필수입니다.");
        if (currentLocation == null || currentLocation.isBlank()) throw new IllegalArgumentException("현재 위치는 필수입니다.");

        this.trackingNo = trackingNo;
        this.deliveryId = deliveryId;
        this.currentLocation = currentLocation;
        this.lastUpdated = LocalDateTime.now();
    }

    public void updateLocation(String newLocation) {
        if (newLocation == null || newLocation.isBlank()) throw new IllegalArgumentException("새로운 위치는 필수입니다.");
        this.currentLocation = newLocation;
        this.lastUpdated = LocalDateTime.now();
    }

    public TrackingNo getTrackingNo() { return trackingNo; }
    public String getDeliveryId() { return deliveryId; }
    public String getCurrentLocation() { return currentLocation; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}
