package com.guijjane.konux.sockets;

import com.guijjane.konux.events.Event;
import com.guijjane.konux.events.EventProtocol;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.IOException;

@Component
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SocketClient implements Client {
    private final Transmission transmission;

    @Override
    public void send(Event event) throws IOException {
        EventProtocol.Event eventProtocol = buildEventProtocol(event);

        try (BufferedOutputStream bos = transmission.open()) {
            bos.write(eventProtocol.toByteArray());
        }
    }

    private EventProtocol.Event buildEventProtocol(Event event) {
        return EventProtocol.Event.newBuilder()
                .setTimestamp(event.getTimestamp())
                .setUserId(event.getUserId())
                .setEvent(event.getEventMessage())
                .build();
    }
}
