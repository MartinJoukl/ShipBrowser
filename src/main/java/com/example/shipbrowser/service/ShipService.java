package com.example.shipbrowser.service;

import com.example.shipbrowser.dao.*;
import com.example.shipbrowser.model.OrphanedEntities;
import com.example.shipbrowser.model.dto.DownloadedShipEntityDtoIn;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.shipbrowser.model.Constants.AZUR_API_SHIPGIRL_URL;
import static com.example.shipbrowser.model.Constants.SHIPS_JSON;

@Service
public class ShipService {

    private final ShipRepository shipRepository;
    private final HttpClient httpClient;
    private final SkinService skinService;
    private final SkillService skillService;

    private final StoredImageService storedImageService;

    public ShipService(ShipRepository shipRepository, SkinService skinService, StoredImageService storedImageService, SkillService skillService, HttpClient httpClient) {
        this.shipRepository = shipRepository;
        this.httpClient = httpClient;
        this.skinService = skinService;
        this.skillService = skillService;
        this.storedImageService = storedImageService;
    }

    public List<Ship> synchronizeShipsWithRemote() throws JsonProcessingException {
        List<Ship> shipsFromDao = shipRepository.findAll();
        Map<String, Ship> shipMap = shipsFromDao.stream().collect(Collectors.toMap(Ship::getOriginalId, Function.identity()));
        String response = httpClient.performGet(AZUR_API_SHIPGIRL_URL + SHIPS_JSON);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DownloadedShipEntityDtoIn[] shipsFromResponse = mapper.readValue(response, DownloadedShipEntityDtoIn[].class);
        List<Ship> newShips = new ArrayList<>();
        List<Ship> updatedShips = new ArrayList<>();
        OrphanedEntities orphanedEntities = new OrphanedEntities();
        for (DownloadedShipEntityDtoIn responseShip : shipsFromResponse
        ) {
            if (shipMap.containsKey(responseShip.getId())) {
                Ship updatedShip = shipMap.get(responseShip.getId());
                mergeShipWithShipDtoIn(updatedShip, responseShip, orphanedEntities);
                updatedShips.add(updatedShip);
            } else if (responseShip.existsOnEn()) {
                Ship newShip = responseShip.toEntity();
                newShips.add(newShip);
            }
        }

        skinService.remove(orphanedEntities.getOrphanedSkins());
        skillService.remove(orphanedEntities.getOrphanedSkills());
        storedImageService.remove(orphanedEntities.getOrphanedImages());

        return null;
    }

    private OrphanedEntities mergeShipWithShipDtoIn(Ship ship, DownloadedShipEntityDtoIn dtoIn, OrphanedEntities orphanedEntities) {
        // Returns list of changed images - so source can be deleted
        ship.setWikiUrl(dtoIn.getWikiUrl());
        ship.setName(dtoIn.getEnglishName());
        ship.setShipClass(dtoIn.getShipClass());
        ship.setNationality(dtoIn.getNationality());
        ship.setHullType(dtoIn.getHullType());

        if (ship.getThumbnail() == null || !Objects.equals(ship.getThumbnail().getOriginalSource(), dtoIn.getThumbnail())) {
            if (ship.getThumbnail() != null) {
                orphanedEntities.getOrphanedImages().add(ship.getThumbnail());
            }

            StoredImage storedImage = new StoredImage();
            // Will have to sync later
            storedImage.setOriginalSource(dtoIn.getThumbnail());
            ship.setThumbnail(storedImage);
        }
        ship.setRarity(dtoIn.getRarity());
        if (dtoIn.getSkills().size() == ship.getSkills().size() && dtoIn.getSkills().stream().allMatch((skill) -> ship.getSkills().stream().anyMatch(skill::equalsToEntity))) {
            orphanedEntities.getOrphanedSkills().addAll(ship.getSkills());
            orphanedEntities.getOrphanedImages().addAll(ship.getSkills().stream().map(Skill::getIcon).toList());

            ship.setSkills((dtoIn.getSkills().stream().map((skin) -> skin.toEntity(ship)).toList()));
        }
        try {
            ship.setConstructionTime(ZonedDateTime.parse(dtoIn.getConstructionTime()));
        } catch (Exception ex) {
            ship.setConstructionTime(null);
        }
        if (dtoIn.getSkins().size() == ship.getSkins().size() && dtoIn.getSkins().stream().allMatch((skin) -> ship.getSkins().stream().anyMatch(skin::equalsToEntity))) {
            orphanedEntities.getOrphanedSkills().addAll(ship.getSkills());
            orphanedEntities.getOrphanedImages().addAll(ship.getSkins().stream().map(Skin::getImage).toList());
            orphanedEntities.getOrphanedImages().addAll(ship.getSkins().stream().map(Skin::getChibi).toList());
            orphanedEntities.getOrphanedImages().addAll(ship.getSkins().stream().map(Skin::getBackground).toList());

            ship.setSkills((dtoIn.getSkills().stream().map((skin) -> skin.toEntity(ship)).toList()));
        }

        ship.setSkins(dtoIn.getSkins().stream().map((skin) -> skin.toEntity(ship)).toList());

        return orphanedEntities;
    }
}
