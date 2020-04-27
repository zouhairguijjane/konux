package com.guijjane.konux.events.rest;

import com.guijjane.konux.events.EventMessage;
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
public class EventMessageController {
    private final SocketClient socketClient;

    @PostMapping("/event")
    public ResponseEntity<Void> sendEvent(@RequestBody EventMessage eventMessage) {
        HttpStatus status = HttpStatus.OK;

        try {
            socketClient.send(eventMessage);
            log.info("Sending an event for UserId : {}", eventMessage.getUserId());
        } catch (IOException e) {
            log.info("I/O error : {}", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(status);
    }
}
