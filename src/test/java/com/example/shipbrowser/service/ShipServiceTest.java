package com.example.shipbrowser.service;

import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.ShipRepository;
import com.example.shipbrowser.model.Constants;
import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class ShipServiceTest {
    @Autowired
    private ResourceLoader resourceLoader;
    @MockBean
    HttpClient httpClient;
    @MockBean
    ShipRepository shipRepository;

    @BeforeEach
    void setupMocks() throws IOException {
        String shipData = resourceLoader.getResource("classpath:ships.json").getContentAsString(StandardCharsets.UTF_8);
        when(httpClient.performGet(eq(Constants.AZUR_API_SHIPGIRL_URL + Constants.SHIPS_JSON))).thenReturn(shipData);
        List<Ship> ships = new ArrayList<>();
        Ship ship = new Ship();
        ship.setId(23L);
        ship.setCode("KMS Z23");
        ship.setName("Z23");
        ship.setNationality("Iron Blood");
        ship.setShipClass("Iron Blood");
        ship.setHullType(HullType.DESTROYER);
        ship.setRarity(Rarity.ELITE);
        ships.add(ship);
        when(shipRepository.findAll()).thenReturn(ships);
    }

    @Test
    void synchronizeShips() throws Exception {

    }
}