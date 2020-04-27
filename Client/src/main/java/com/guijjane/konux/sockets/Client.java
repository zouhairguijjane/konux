package com.guijjane.konux.sockets;

import com.guijjane.konux.events.EventMessage;

import java.io.IOException;

public interface Client {
    void send(EventMessage event) throws IOException;
}
