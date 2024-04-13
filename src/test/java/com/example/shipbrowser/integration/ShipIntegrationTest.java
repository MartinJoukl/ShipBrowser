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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
        byte[] shipData = resourceLoader.getResource("classpath:integrationShips.json").getContentAsByteArray();
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
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("itemList").isArray())
                    .andExpect(jsonPath("itemList[0].id").exists())
                    .andExpect(jsonPath("itemList[0].originalId").value("155"))
                    .andExpect(jsonPath("itemList[0].wikiUrl").value("https://azurlane.koumakan.jp/wiki/Ayanami"))
                    .andExpect(jsonPath("itemList[0].name").value("Ayanami"))
                    .andExpect(jsonPath("itemList[0].code").value("IJN Ayanami"))
                    .andExpect(jsonPath("itemList[0].shipClass").value("Fubuki (Ayanami subclass)"))
                    .andExpect(jsonPath("itemList[0].nationality").value("Sakura Empire"))
                    .andExpect(jsonPath("itemList[0].rarity").value("Elite"))
                    .andExpect(jsonPath("itemList[0].hullType").value("Destroyer"))
                    .andExpect(jsonPath("itemList[0].thumbnailLink").value("..\\images\\skins\\155\\thumbnail.png"))
                    .andExpect(jsonPath("itemList[0].constructionTime").value("00:23:00"))
                    .andExpect(jsonPath("itemList[0].obtainedFrom").value("CN/EN: Awarded and unlocked in construction when Collection goal is met using the following ships: Javelin, Laffey, Z23"))
                    .andExpect(jsonPath("itemList[0].skins[1].name").value("Retrofit"))
                    .andExpect(jsonPath("itemList[0].skins[1].imageLink").value("..\\images\\skins\\155\\Retrofit\\image.png"))
                    .andExpect(jsonPath("itemList[0].skins[1].backgroundLink").value("..\\images\\backgrounds\\MainDayBG.png"))
                    .andExpect(jsonPath("itemList[0].skins[1].chibiLink").value("..\\images\\skins\\155\\Retrofit\\chibi.png"))
                    .andExpect(jsonPath("itemList[0].skills[3].name").value("All Out Assault"))
                    .andExpect(jsonPath("itemList[0].skills[3].iconLink").value("..\\images\\skills\\155\\all_out_assault.png"))
                    .andExpect(jsonPath("itemList[0].skills[3].description").value("Every 15 (10) times the main gun is fired, triggers All Out Assault - Ayanami I (II)"))
                    .andExpect(jsonPath("itemList[0].skills[3].color").value("pink"));
        } catch (Exception e) {
            fail(e);
        }
    }
}
