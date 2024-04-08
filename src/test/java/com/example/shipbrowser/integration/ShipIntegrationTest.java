package com.example.shipbrowser.integration;

import org.junit.jupiter.api.*;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.mock.OpenAPIExpectation.openAPIExpectation;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@SpringBootTest
@AutoConfigureMockMvc
public class ShipIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private ClientAndServer mockServer;

    @Autowired
    private ResourceLoader resourceLoader;

    @BeforeEach
    public void startServer() {
        mockServer = startClientAndServer(65100);
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    @WithMockUser(username = "user", password = "password")
    void synchronizeShips() throws IOException {
        byte[] shipData = resourceLoader.getResource("classpath:ships.json").getContentAsByteArray();
        try (MockServerClient mockserverClient = new MockServerClient("localhost", 65100)) {
            mockserverClient.when(
                            request()
                                    .withMethod("GET")
                                    .withPath("/ships.json")
                                    .withHeader("Accept", "application/json")
                    )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(shipData));


            mockMvc.perform(
                    put("/synchronizeShips")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf())
            );
        } catch (Exception e) {
            fail(e);
        }
    }
}
