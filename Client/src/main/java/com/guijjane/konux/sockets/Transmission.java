package com.guijjane.konux.sockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

@Component
@Configuration
@Slf4j
public class Transmission {

    @org.springframework.beans.factory.annotation.Value("${client.port}")
    private int port;

    @org.springframework.beans.factory.annotation.Value("${client.host}")
    private String host;

    public BufferedOutputStream open() throws IOException {
        BufferedOutputStream bos;
        try (Socket socket = new Socket(host, port)) {
            bos = new BufferedOutputStream(socket.getOutputStream());
            log.info("Open transmission to  {}", socket.getLocalAddress());
        }
        return bos;
    }
}
