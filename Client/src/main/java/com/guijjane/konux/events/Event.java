package com.guijjane.konux.events;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Event {
    private final int timestamp;
    private final int userId;
    private final String event;

    public int getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public String getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "Event{" +
                "timestamp=" + timestamp +
                ", userId=" + userId +
                ", event='" + event + '\'' +
                '}';
    }
}
