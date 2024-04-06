package com.example.shipbrowser.service;

import com.example.shipbrowser.dao.*;
import com.example.shipbrowser.helpers.RemoteToLocalLinkCoverter;
import com.example.shipbrowser.model.dto.DownloadedShipEntityDtoIn;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

        Set<String> links = getAllImageLinks(mergedShips);
        mergedShips = shipRepository.saveAll(mergedShips);

        storedImageService.downloadAllImagesToLocal(links);

        return mergedShips;
    }

    private Set<String> getAllImageLinks(List<Ship> mergedShips) {
        Set<String> links = new HashSet<>();
        for (Ship ship : mergedShips) {
            if (ship.getThumbnailLink() != null) {
                links.add(ship.getThumbnailLink());
            }
            for (Skin skin : ship.getSkins()) {
                if (skin.getImageLink() != null) {
                    links.add(skin.getImageLink());
                }
                if (skin.getBackgroundLink() != null) {
                    links.add(skin.getBackgroundLink());
                }
                if (skin.getChibiLink() != null) {
                    links.add(skin.getChibiLink());
                }
            }

            for (Skill skill : ship.getSkills()) {
                if (skill.getIconLink() != null) {
                    links.add(skill.getIconLink());
                }
            }
        }
        return links;
    }

    private void mergeShipWithShipDtoIn(Ship ship, DownloadedShipEntityDtoIn dtoIn) {
        // Returns list of changed images - so source can be deleted
        ship.setWikiUrl(dtoIn.getWikiUrl());
        ship.setName(dtoIn.getEnglishName());
        ship.setShipClass(dtoIn.getShipClass());
        ship.setNationality(dtoIn.getNationality());
        ship.setHullType(dtoIn.getHullType());

        if (ship.getThumbnailLink() == null || !Objects.equals(ship.getThumbnailLink(), RemoteToLocalLinkCoverter.fromRemoteToLocal(dtoIn.getThumbnail()))) {
            ship.setThumbnailLink(RemoteToLocalLinkCoverter.fromRemoteToLocal(dtoIn.getThumbnail()));
        }
        ship.setRarity(dtoIn.getRarity());
        if (dtoIn.getSkins().size() == ship.getSkins().size() && dtoIn.getSkins().stream().allMatch((skin) -> ship.getSkins().stream().anyMatch(skin::equalsToEntity))) {
            ship.setSkins(dtoIn.getSkins().stream().map((skin) -> skin.toEntity(ship)).collect(Collectors.toCollection(ArrayList::new)));
        }

        if (dtoIn.getSkills().size() == ship.getSkills().size() && dtoIn.getSkills().stream().allMatch((skill) -> ship.getSkills().stream().anyMatch(skill::equalsToEntity))) {
            ship.setSkills((dtoIn.getSkills().stream().map((skin) -> skin.toEntity(ship)).collect(Collectors.toCollection(ArrayList::new))));
        }

        ship.setConstructionTime(dtoIn.getConstructionTime());
    }
}
