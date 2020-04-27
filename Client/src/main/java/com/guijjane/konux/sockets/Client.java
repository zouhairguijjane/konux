package com.guijjane.konux.sockets;

import com.guijjane.konux.events.Event;

import java.io.IOException;

public interface Client {
    void send(Event event) throws IOException;
}
