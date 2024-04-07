package com.example.shipbrowser.controller;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.Skill;
import com.example.shipbrowser.repository.Skin;
import com.example.shipbrowser.service.ShipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShipController.class)
@Import(ShipController.class)
class ShipControllerTest {

    @MockBean
    private RestClient mockClient;

    @MockBean
    private ShipService shipService;

    @Autowired
    private MockMvc mockMvc;

    private Ship mockedShip;

    @BeforeEach
    void setupMocks() throws IOException {
        mockedShip = new Ship();
        List<Ship> ships = new ArrayList<>();
        ships.add(mockedShip);
        mockedShip.setRarity(Rarity.RARE);
        mockedShip.setId(1L);
        mockedShip.setCode("IJN Fubuki");
        mockedShip.setNationality("Sakura Empire");
        mockedShip.setShipClass("Fubuki");
        mockedShip.setName("Fubuki");
        mockedShip.setHullType(HullType.DESTROYER);
        mockedShip.setObtainedFrom("Unit test");
        mockedShip.setOriginalId("remoteId");
        mockedShip.setThumbnailLink("thumbnail");
        mockedShip.setWikiUrl("Url");

        Skill skill = new Skill();
        skill.setShip(mockedShip);
        skill.setName("Skill1");
        skill.setId(1L);
        skill.setDescription("description");
        skill.setColor("Red");
        skill.setIconLink("icon");
        Skin skin = new Skin();
        skin.setShip(mockedShip);
        skin.setName("Skin1");
        skin.setBackgroundLink("backgroundLink");
        skin.setImageLink("Image link");
        skin.setChibiLink("Chibi");
        mockedShip.setSkins(Collections.singletonList(skin));
        mockedShip.setSkills(Collections.singletonList(skill));
        when(shipService.synchronizeShipsWithRemote()).thenReturn(ships);
    }

    @Test
    @WithMockUser(username = "user", password = "password")
    void synchronizeShips() throws Exception {
        setupMocks();
        mockMvc.perform(
                        put("/synchronizeShips")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("itemList[0].id").value(mockedShip.getId()))
                .andExpect(jsonPath("itemList[0].originalId").value(mockedShip.getOriginalId()))
                .andExpect(jsonPath("itemList[0].wikiUrl").value(mockedShip.getWikiUrl()))
                .andExpect(jsonPath("itemList[0].name").value(mockedShip.getName()))
                .andExpect(jsonPath("itemList[0].code").value(mockedShip.getCode()))
                .andExpect(jsonPath("itemList[0].shipClass").value(mockedShip.getShipClass()))
                .andExpect(jsonPath("itemList[0].nationality").value(mockedShip.getNationality()))
                .andExpect(jsonPath("itemList[0].rarity").value(mockedShip.getRarity().getName()))
                .andExpect(jsonPath("itemList[0].hullType").value(mockedShip.getHullType().getName()))
                .andExpect(jsonPath("itemList[0].thumbnailLink").value(mockedShip.getThumbnailLink()))
                .andExpect(jsonPath("itemList[0].constructionTime").value(mockedShip.getConstructionTime()))
                .andExpect(jsonPath("itemList[0].obtainedFrom").value(mockedShip.getObtainedFrom()))
                .andExpect(jsonPath("itemList[0].skins[0].name").value(mockedShip.getSkins().get(0).getName()))
                .andExpect(jsonPath("itemList[0].skins[0].imageLink").value(mockedShip.getSkins().get(0).getImageLink()))
                .andExpect(jsonPath("itemList[0].skins[0].backgroundLink").value(mockedShip.getSkins().get(0).getBackgroundLink()))
                .andExpect(jsonPath("itemList[0].skins[0].chibiLink").value(mockedShip.getSkins().get(0).getChibiLink()))
                .andExpect(jsonPath("itemList[0].skills[0].name").value(mockedShip.getSkills().get(0).getName()))
                .andExpect(jsonPath("itemList[0].skills[0].iconLink").value(mockedShip.getSkills().get(0).getIconLink()))
                .andExpect(jsonPath("itemList[0].skills[0].description").value(mockedShip.getSkills().get(0).getDescription()))
                .andExpect(jsonPath("itemList[0].skills[0].color").value(mockedShip.getSkills().get(0).getColor()));
    }
}