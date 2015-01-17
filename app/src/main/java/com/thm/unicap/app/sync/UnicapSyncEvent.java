package com.thm.unicap.app.sync;

public class UnicapSyncEvent {

    public enum EventType {
        SYNC_STARTED,
        SYNC_FAILED,
        SYNC_COMPLETED,
    }

    private final EventType eventType;
    private String eventMessage;

    public UnicapSyncEvent(EventType eventType, String eventMessage) {
        this.eventType = eventType;
        this.eventMessage = eventMessage;
    }

    public UnicapSyncEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getEventMessage() {
        return eventMessage;
    }
}
