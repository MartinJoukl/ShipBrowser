package com.example.shipbrowser.service;

import com.example.shipbrowser.dao.*;
import com.example.shipbrowser.model.dto.DownloadedShipEntityDtoIn;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        //TODO maybe just one list?
        List<Ship> newShips = new ArrayList<>();
        List<Ship> updatedShips = new ArrayList<>();
        Map<String, StoredImage> storedImageMap = new HashMap<>();
        for (DownloadedShipEntityDtoIn responseShip : shipsFromResponse) {
            if (shipMap.containsKey(responseShip.getId())) {
                Ship updatedShip = shipMap.get(responseShip.getId());
                mergeShipWithShipDtoIn(updatedShip, responseShip, storedImageMap);
                updatedShips.add(updatedShip);
            } else if (responseShip.existsOnEn()) {
                Ship newShip = createNewShip(storedImageMap, responseShip);
                newShips.add(newShip);
            }
        }
        List<Ship> mergedShips = Stream.concat(newShips.stream(), updatedShips.stream()).collect(Collectors.toCollection(ArrayList::new));

        List<StoredImage> images = storedImageService.saveImages(storedImageMap.values().stream().toList());
        // Need to call again because of ids
        linkEntitiesWithCreatedImages(mergedShips, images);
        mergedShips = shipRepository.saveAll(mergedShips);

        storedImageService.downloadAllImagesToLocal();

        return mergedShips;
    }

    private Ship createNewShip(Map<String, StoredImage> storedImageMap, DownloadedShipEntityDtoIn responseShip) {
        Ship newShip = responseShip.toEntity();
        tryToLinkSkillImagesWithAlreadyExisting(newShip, storedImageMap);
        tryToLinkSkinImagesWithAlreadyExisting(newShip, storedImageMap);
        if (newShip.getThumbnail() == null || !Objects.equals(newShip.getThumbnail().getOriginalSource(), newShip.getThumbnail())) {
            tryLinkingThumbnailWithExistingStoredImage(newShip, responseShip, storedImageMap);
        }
        return newShip;
    }

    private List<Ship> linkEntitiesWithCreatedImages(List<Ship> mergedShips, List<StoredImage> images) {
        Map<String, StoredImage> storedImageMap;
        storedImageMap = images.stream().collect(Collectors.toMap(
                StoredImage::getOriginalSource,
                Function.identity()
        ));
        for (Ship ship : mergedShips) {
            for (Skill skill : ship.getSkills()) {
                if (skill.getIcon() != null && storedImageMap.containsKey(skill.getIcon().getOriginalSource())) {
                    skill.setIcon(storedImageMap.get(skill.getIcon().getOriginalSource()));
                }
            }
            for (Skin skin : ship.getSkins()) {
                if (skin.getImage() != null && storedImageMap.containsKey(skin.getImage().getOriginalSource())) {
                    skin.setImage(storedImageMap.get(skin.getImage().getOriginalSource()));
                }
                if (skin.getChibi() != null && storedImageMap.containsKey(skin.getChibi().getOriginalSource())) {
                    skin.setChibi(storedImageMap.get(skin.getChibi().getOriginalSource()));
                }
                if (skin.getBackground() != null && storedImageMap.containsKey(skin.getBackground().getOriginalSource())) {
                    skin.setBackground(storedImageMap.get(skin.getBackground().getOriginalSource()));
                }
            }

            if (ship.getThumbnail() != null) {
                ship.setThumbnail(storedImageMap.get(ship.getThumbnail().getOriginalSource()));
            }
        }

        return mergedShips;
    }

    private void addDetectedSkinImagesToStoredImageMap(List<Ship> mergedShips, List<StoredImage> images) {

    }

    private void mergeShipWithShipDtoIn(Ship ship, DownloadedShipEntityDtoIn dtoIn, Map<String, StoredImage> storedImageMap) {
        // Returns list of changed images - so source can be deleted
        ship.setWikiUrl(dtoIn.getWikiUrl());
        ship.setName(dtoIn.getEnglishName());
        ship.setShipClass(dtoIn.getShipClass());
        ship.setNationality(dtoIn.getNationality());
        ship.setHullType(dtoIn.getHullType());

        if (ship.getThumbnail() == null || !Objects.equals(ship.getThumbnail().getOriginalSource(), dtoIn.getThumbnail())) {
            tryLinkingThumbnailWithExistingStoredImage(ship, dtoIn, storedImageMap);
        }
        ship.setRarity(dtoIn.getRarity());
        if (dtoIn.getSkins().size() == ship.getSkins().size() && dtoIn.getSkins().stream().allMatch((skin) -> ship.getSkins().stream().anyMatch(skin::equalsToEntity))) {
            ship.setSkins(dtoIn.getSkins().stream().map((skin) -> skin.toEntity(ship)).collect(Collectors.toCollection(ArrayList::new)));
            tryToLinkSkinImagesWithAlreadyExisting(ship, storedImageMap);
        }

        if (dtoIn.getSkills().size() == ship.getSkills().size() && dtoIn.getSkills().stream().allMatch((skill) -> ship.getSkills().stream().anyMatch(skill::equalsToEntity))) {
            ship.setSkills((dtoIn.getSkills().stream().map((skin) -> skin.toEntity(ship)).collect(Collectors.toCollection(ArrayList::new))));
            tryToLinkSkillImagesWithAlreadyExisting(ship, storedImageMap);
        }

        ship.setConstructionTime(dtoIn.getConstructionTime());
    }

    private void tryLinkingThumbnailWithExistingStoredImage(Ship ship, DownloadedShipEntityDtoIn dtoIn, Map<String, StoredImage> storedImageMap) {
        if (!storedImageMap.containsKey(dtoIn.getThumbnail())) {
            StoredImage storedImage = new StoredImage();
            // Will have to sync later
            storedImage.setOriginalSource(dtoIn.getThumbnail());
            ship.setThumbnail(storedImage);
            storedImageMap.put(storedImage.getOriginalSource(), storedImage);
        } else {
            ship.setThumbnail(storedImageMap.get(dtoIn.getThumbnail()));
        }
    }

    private void tryToLinkSkillImagesWithAlreadyExisting(Ship ship, Map<String, StoredImage> storedImageMap) {
        for (Skill skill : ship.getSkills()) {
            if (skill.getIcon() != null) {
                if (storedImageMap.containsKey(skill.getIcon().getOriginalSource())) {
                    skill.setIcon(storedImageMap.get(skill.getIcon().getOriginalSource()));
                } else {
                    storedImageMap.put(skill.getIcon().getOriginalSource(), skill.getIcon());
                }
            }
        }
    }

    private void tryToLinkSkinImagesWithAlreadyExisting(Ship ship, Map<String, StoredImage> storedImageMap) {
        for (Skin skin : ship.getSkins()) {
            if (skin.getImage() != null) {
                if (storedImageMap.containsKey(skin.getImage().getOriginalSource())) {
                    skin.setImage(storedImageMap.get(skin.getImage().getOriginalSource()));
                } else {
                    storedImageMap.put(skin.getImage().getOriginalSource(), skin.getImage());
                }
            }
            if (skin.getChibi() != null) {
                if (storedImageMap.containsKey(skin.getChibi().getOriginalSource())) {
                    skin.setChibi(storedImageMap.get(skin.getChibi().getOriginalSource()));
                } else {
                    storedImageMap.put(skin.getChibi().getOriginalSource(), skin.getChibi());
                }
            }
            if (skin.getBackground() != null) {
                if (storedImageMap.containsKey(skin.getBackground().getOriginalSource())) {
                    skin.setBackground(storedImageMap.get(skin.getBackground().getOriginalSource()));
                } else {
                    storedImageMap.put(skin.getBackground().getOriginalSource(), skin.getBackground());
                }
            }
        }
    }
}
