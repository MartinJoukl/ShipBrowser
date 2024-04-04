package com.example.shipbrowser;

import com.example.shipbrowser.model.dto.DownloadedShipEntityDtoIn;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SpringBootTest
class ShipBrowserApplicationTests {

    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    void contextLoads() throws IOException {
        String data = resourceLoader.getResource("classpath:ships.json").getContentAsString(StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DownloadedShipEntityDtoIn[] dtoIn = mapper.readValue(data, DownloadedShipEntityDtoIn[].class);
        System.out.println("aaa");
    }

}
