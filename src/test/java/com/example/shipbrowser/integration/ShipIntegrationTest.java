package com.example.shipbrowser.integration;

import com.example.shipbrowser.controller.ShipController;
import com.example.shipbrowser.repository.ShipRepository;
import com.example.shipbrowser.repository.StoredImageRepository;
import com.example.shipbrowser.service.ShipService;
import com.example.shipbrowser.service.StoredImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(ShipController.class)
@Import({ShipController.class, ShipService.class, ShipRepository.class, StoredImageService.class})
public class ShipIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Test
    @WithMockUser(username = "user", password = "password")
    void synchronizeShips() throws Exception {
        mockMvc.perform(
                        put("/synchronizeShips")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                );
    }
}
