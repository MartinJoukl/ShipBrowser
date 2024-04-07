package com.example.shipbrowser.service;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import com.example.shipbrowser.model.dto.dtoIn.ListShipsDtoIn;
import com.example.shipbrowser.model.dto.dtoIn.ListShipsSearchCriteriaDtoIn;
import com.example.shipbrowser.model.dto.dtoIn.PageInfoDtoIn;
import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.ShipRepository;
import com.example.shipbrowser.model.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class ShipServiceTest {
    @Autowired
    private ResourceLoader resourceLoader;
    @MockBean
    HttpClient httpClient;
    @MockBean
    StoredImageService storedImageService;
    @Autowired
    ShipService shipService;

    @Autowired
    ShipRepository shipRepository;

    @BeforeEach
    void setupTest() throws IOException {
        shipRepository.deleteAll();
        String shipData = resourceLoader.getResource("classpath:ships.json").getContentAsString(StandardCharsets.UTF_8);
        when(httpClient.performGet(eq(Constants.AZUR_API_SHIPGIRL_URL + Constants.SHIPS_JSON))).thenReturn(shipData);
    }

    @Test
    void synchronizeShips() throws Exception {
        List<Ship> ships = shipService.synchronizeShipsWithRemote();
        assertNotNull(ships);
        assertEquals(ships.size(), 606);
        for (Ship ship : ships) {
            assertNotNull(ship.getName());
            assertNotNull(ship.getCode());
            assertNotNull(ship.getShipClass());
            assertNotNull(ship.getSkins());
            assertNotNull(ship.getSkills());
            assertNotNull(ship.getThumbnailLink());
            assertNotNull(ship.getRarity());
            assertNotNull(ship.getConstructionTime());
            assertNotNull(ship.getNationality());
            assertNotNull(ship.getId());
            assertNotNull(ship.getOriginalId());
            // can be null
            //assertNotNull(ship.getObtainedFrom());
        }
        Ship firstShip = ships.stream().min(Comparator.comparing(Ship::getName)).get();
        assertEquals("Abercrombie", firstShip.getName());
        assertEquals("HMS Abercrombie", firstShip.getCode());
        assertEquals("Roberts", firstShip.getShipClass());
        assertEquals(3, firstShip.getSkins().size());
        assertEquals(1, firstShip.getSkills().size());
        assertEquals("..\\images\\skins\\336\\thumbnail.png", firstShip.getThumbnailLink());
        assertEquals(Rarity.ELITE, firstShip.getRarity());
        assertEquals("01:05:00", firstShip.getConstructionTime());
        assertEquals("Royal Navy", firstShip.getNationality());
        assertEquals("336", firstShip.getOriginalId());
    }

    @Test
    void listAllShips() throws IOException {
        List<Ship> ships = shipService.synchronizeShipsWithRemote();
        //Call with empty dtoIn
        ListShipsDtoIn dtoIn = new ListShipsDtoIn();
        dtoIn.setPageInfo(new PageInfoDtoIn(0, 1000));
        Page<Ship> listedShips = shipService.listShips(dtoIn);
        assertEquals(listedShips.getTotalElements(), 606);
        assertEquals(listedShips.getPageable().getPageSize(), 1000);
        assertEquals(listedShips.getPageable().getOffset(), 0);

        assertEquals(ships.size(), listedShips.getContent().size());
        assertArrayEquals(ships.toArray(), listedShips.getContent().toArray());
    }

    @Test
    void listSpecificShips() throws IOException {
        List<Ship> ships = shipService.synchronizeShipsWithRemote();
        //Call with empty dtoIn
        ListShipsDtoIn dtoIn = new ListShipsDtoIn();
        dtoIn.setSortCriteria(new LinkedHashMap<>());
        dtoIn.getSortCriteria().put("name", "asc");
        dtoIn.setSearchCriteria(new ListShipsSearchCriteriaDtoIn());
        dtoIn.getSearchCriteria().setNationality("Iron Blood");
        dtoIn.getSearchCriteria().setHullType(HullType.DESTROYER.getName());
        dtoIn.setPageInfo(new PageInfoDtoIn(1, 7));
        Page<Ship> listedShips = shipService.listShips(dtoIn);
        assertEquals(listedShips.getTotalElements(), 15);
        assertEquals(listedShips.getPageable().getPageSize(), 7);
        assertEquals(listedShips.getPageable().getPageNumber(), 1);
        assertEquals(listedShips.getContent().size(), 7);

        Ship nimi = listedShips.getContent().get(0);
        assertEquals("Z23", nimi.getName());
        assertEquals("KMS Z23", nimi.getCode());
        assertEquals("https://azurlane.koumakan.jp/wiki/Z23", nimi.getWikiUrl());
        assertEquals("Type 1936A", nimi.getShipClass());
        assertEquals("236", nimi.getOriginalId());
        assertEquals("Iron Blood", nimi.getNationality());
        assertEquals(Rarity.ELITE, nimi.getRarity());
        assertEquals(HullType.DESTROYER, nimi.getHullType());
        assertEquals("..\\images\\skins\\236\\thumbnail.png", nimi.getThumbnailLink());
        assertEquals("00:33:00", nimi.getConstructionTime());
        assertEquals("CN/EN: Available as a starter ship\n" +
                        "JP: Awarded and unlocked in construction when Collection" +
                        " goal is met using the following ships:" +
                        " Javelin, Laffey, Ayanami",
                nimi.getObtainedFrom());
    }
}