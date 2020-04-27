package com.guijjane.konux.events.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guijjane.konux.events.EventMessage;
import com.guijjane.konux.events.EventProtocol;
import com.guijjane.konux.sockets.Transmission;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventMessageControllerTest {
    private static final int TIMESTAMP = 1518609008;
    private static final int USER_ID = 1123;
    private static final String EVENT_MESSAGE = "2 hours of downtime occurred due to the release of version 1.0.5 of the system";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    Transmission transmission;

    @TempDir
    File tempDir;

    private EventProtocol.Event eventProtocol;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        eventProtocol = buildEventProtocol();
    }

    @Test
    public void succeedWhenEventIsSent() throws Exception {
        File file = new File(tempDir, "events.txt");
        when(transmission.open()).thenReturn(new BufferedOutputStream(new FileOutputStream(file)));

        postEvent().andExpect(status().isOk());

        Assertions.assertThat(Files.readAllBytes(file.toPath())).isEqualTo(eventProtocol.toByteArray());
    }

    @Test
    public void throwServerErrorWhenEventSendingProduceAnException() throws Exception {
        when(transmission.open()).thenThrow(new IOException());

        postEvent().andExpect(status().is5xxServerError());
    }

    private ResultActions postEvent() throws Exception {
        EventMessage eventMessage = new EventMessage(TIMESTAMP, USER_ID, EVENT_MESSAGE);

        return mockMvc.perform(post("/api/event")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventMessage)));
    }

    private EventProtocol.Event buildEventProtocol() {
        return EventProtocol.Event.newBuilder()
                .setTimestamp(TIMESTAMP)
                .setUserId(USER_ID)
                .setEvent(EVENT_MESSAGE)
                .build();
    }
}
