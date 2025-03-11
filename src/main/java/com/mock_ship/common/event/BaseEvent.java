package com.mock_ship.common.event;

import java.time.LocalDateTime;

public abstract class BaseEvent {
    private final LocalDateTime occurredAt;

    protected BaseEvent() {
        this.occurredAt = LocalDateTime.now();
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}