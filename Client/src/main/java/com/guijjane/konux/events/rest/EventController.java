package com.guijjane.konux.events.rest;

import com.guijjane.konux.events.Event;
import com.guijjane.konux.sockets.SocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final SocketClient socketClient;

    @PostMapping("/event")
    public ResponseEntity<Void> sendEvent(@RequestBody Event event) {
        HttpStatus status = HttpStatus.OK;

        try {
            socketClient.send(event);
            log.info("Sending an event for UserId : {}", event.getUserId());
        } catch (IOException e) {
            log.info("Error while sending the event", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(status);
    }
}
