package com.guijjane.konux.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private int timestamp;
    private int userId;
    private String eventMessage;
}
