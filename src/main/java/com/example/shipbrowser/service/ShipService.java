package com.example.shipbrowser.service;

import com.example.shipbrowser.dao.*;
import com.example.shipbrowser.model.dto.DownloadedShipEntityDtoIn;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Ship> synchronizeShipsWithRemote() throws IOException {
        List<Ship> shipsFromDao = shipRepository.findAll();
        Map<String, Ship> shipMap = shipsFromDao.stream().collect(Collectors.toMap(Ship::getOriginalId, Function.identity()));
        String response = httpClient.performGet(AZUR_API_SHIPGIRL_URL + SHIPS_JSON);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DownloadedShipEntityDtoIn[] shipsFromResponse = mapper.readValue(response, DownloadedShipEntityDtoIn[].class);
        //TODO maybe just one list?
        List<Ship> newShips = new ArrayList<>();
        List<Ship> updatedShips = new ArrayList<>();
        for (DownloadedShipEntityDtoIn responseShip : shipsFromResponse) {
            if (shipMap.containsKey(responseShip.getId())) {
                Ship updatedShip = shipMap.get(responseShip.getId());
                mergeShipWithShipDtoIn(updatedShip, responseShip);
                updatedShips.add(updatedShip);
            } else if (responseShip.existsOnEn()) {
                Ship newShip = responseShip.toEntity();
                newShips.add(newShip);
            }
        }
        List<Ship> mergedShips = Stream.concat(newShips.stream(), updatedShips.stream()).collect(Collectors.toCollection(ArrayList::new));
        /*
        List<Ship> savedEntities = new ArrayList<>();
        List<Ship> shipsInBatch = new ArrayList<>();
        for (int i = 0; i < mergedShips.size(); i++) {
            if (shipsInBatch.size() < 50 && i < mergedShips.size() - 1) {
                shipsInBatch.add(mergedShips.get(i));
            } else {
                savedEntities.addAll(shipRepository.saveAll(shipsInBatch));
            }
        }

         */
        mergedShips = shipRepository.saveAll(mergedShips);

        storedImageService.downloadAllImagesToLocal();

        return mergedShips;
    }

    private void mergeShipWithShipDtoIn(Ship ship, DownloadedShipEntityDtoIn dtoIn) {
        // Returns list of changed images - so source can be deleted
        ship.setWikiUrl(dtoIn.getWikiUrl());
        ship.setName(dtoIn.getEnglishName());
        ship.setShipClass(dtoIn.getShipClass());
        ship.setNationality(dtoIn.getNationality());
        ship.setHullType(dtoIn.getHullType());

        if (ship.getThumbnail() == null || !Objects.equals(ship.getThumbnail().getOriginalSource(), dtoIn.getThumbnail())) {

            StoredImage storedImage = new StoredImage();
            // Will have to sync later
            storedImage.setOriginalSource(dtoIn.getThumbnail());
            ship.setThumbnail(storedImage);
        }
        ship.setRarity(dtoIn.getRarity());
        if (dtoIn.getSkills().size() == ship.getSkills().size() && dtoIn.getSkills().stream().allMatch((skill) -> ship.getSkills().stream().anyMatch(skill::equalsToEntity))) {

            ship.setSkills((dtoIn.getSkills().stream().map((skin) -> skin.toEntity(ship)).collect(Collectors.toCollection(ArrayList::new))));
        }
        try {
            ship.setConstructionTime(ZonedDateTime.parse(dtoIn.getConstructionTime()));
        } catch (Exception ex) {
            ship.setConstructionTime(null);
        }
        if (dtoIn.getSkins().size() == ship.getSkins().size() && dtoIn.getSkins().stream().allMatch((skin) -> ship.getSkins().stream().anyMatch(skin::equalsToEntity))) {

            ship.setSkills((dtoIn.getSkills().stream().map((skin) -> skin.toEntity(ship)).collect(Collectors.toCollection(ArrayList::new))));
        }

        ship.setSkins(dtoIn.getSkins().stream().map((skin) -> skin.toEntity(ship)).collect(Collectors.toCollection(ArrayList::new)));
    }
}
