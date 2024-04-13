package com.example.shipbrowser.service;

import com.example.shipbrowser.model.dto.dtoIn.*;
import com.example.shipbrowser.repository.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.shipbrowser.repository.ShipSpecification.createFilterQuery;

@Service
public class ShipService {
    private final SkinService skinService;
    private final SkillService skillService;
    private final ShipRepository shipRepository;
    private final HttpClient httpClient;
    private final StoredImageService storedImageService;
    private final RemoteToLocalLinkCoverter remoteToLocalLinkCoverter;

    @Value("${httpService.uri.azur-api-url}")
    private String azurApiShipgirlUrl;
    private String shipsJson = "/ships.json";

    public ShipService(ShipRepository shipRepository, StoredImageService storedImageService, HttpClient httpClient, RemoteToLocalLinkCoverter remoteToLocalLinkCoverter, SkinService skinService, SkillService skillService) {
        this.shipRepository = shipRepository;
        this.httpClient = httpClient;
        this.storedImageService = storedImageService;
        this.remoteToLocalLinkCoverter = remoteToLocalLinkCoverter;
        this.skillService = skillService;
        this.skinService = skinService;
    }

    @Transactional()
    public List<Ship> synchronizeShipsWithRemote() throws IOException {
        List<Ship> shipsFromDao = shipRepository.findAll();
        Map<String, Ship> shipMap = shipsFromDao.stream().collect(Collectors.toMap(Ship::getOriginalId, Function.identity()));
        String response = httpClient.performGet(azurApiShipgirlUrl + shipsJson);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        DownloadedShipEntityDtoIn[] shipsFromResponse = mapper.readValue(response, DownloadedShipEntityDtoIn[].class);

        List<Ship> newShips = new ArrayList<>();
        List<Ship> updatedShips = new ArrayList<>();
        List<Skin> skinsToDelete = new ArrayList<>();
        List<Skill> skillsToDelete = new ArrayList<>();
        for (DownloadedShipEntityDtoIn responseShip : shipsFromResponse) {
            if (shipMap.containsKey(responseShip.getId())) {
                Ship updatedShip = shipMap.get(responseShip.getId());
                mergeShipWithShipDtoIn(updatedShip, responseShip, skinsToDelete, skillsToDelete);
                updatedShips.add(updatedShip);
            } else if (responseShip.existsOnEn()) {
                Ship newShip = responseShip.toEntity(remoteToLocalLinkCoverter);
                newShips.add(newShip);
            }
        }
        List<Ship> mergedShips = Stream.concat(newShips.stream(), updatedShips.stream()).collect(Collectors.toCollection(ArrayList::new));

        Set<String> links = getAllImageLinks(mergedShips);

        skinService.remove(skinsToDelete);
        skillService.remove(skillsToDelete);
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

    private void mergeShipWithShipDtoIn(Ship ship, DownloadedShipEntityDtoIn dtoIn, List<Skin> skinsToDelete, List<Skill> skillsToDelete) {
        // Returns list of changed images - so source can be deleted
        ship.setWikiUrl(dtoIn.getWikiUrl());
        ship.setName(dtoIn.getEnglishName());
        ship.setShipClass(dtoIn.getShipClass());
        ship.setNationality(dtoIn.getNationality());
        ship.setHullType(dtoIn.getHullType());

        if (ship.getThumbnailLink() == null || !Objects.equals(ship.getThumbnailLink(), remoteToLocalLinkCoverter.fromRemoteToLocal(dtoIn.getThumbnail()))) {
            ship.setThumbnailLink(remoteToLocalLinkCoverter.fromRemoteToLocal(dtoIn.getThumbnail()));
        }
        ship.setRarity(dtoIn.getRarity());
        mergeSkins(ship, dtoIn, skinsToDelete);

        mergeSkills(ship, dtoIn, skillsToDelete);

        ship.setConstructionTime(dtoIn.getConstructionTime());
    }

    private void mergeSkins(Ship ship, DownloadedShipEntityDtoIn dtoIn, List<Skin> skinsToDelete) {
        Map<String, Skin> skinNameToSkinMap = ship.getSkins().stream().collect(Collectors.toMap(Skin::getName, Function.identity()));
        Map<String, DownloadedSkinEntityDtoIn> skinDtoNameToSkinMap = dtoIn.getSkins().stream().collect(Collectors.toMap(DownloadedSkinEntityDtoIn::getName, Function.identity()));
        // Delete all not contained skins
        List<Skin> currentSkinsToDelete = ship.getSkins().stream().filter((skin) -> !skinDtoNameToSkinMap.containsKey(skin.getName())).toList();
        ship.getSkins().removeAll(currentSkinsToDelete);
        skinsToDelete.addAll(currentSkinsToDelete);
        for (var skinDtoIn : dtoIn.getSkins()) {
            if (skinNameToSkinMap.containsKey(skinDtoIn.getName())) {
                Skin skinEntity = skinNameToSkinMap.get(skinDtoIn.getName());
                skinEntity.setChibiLink(remoteToLocalLinkCoverter.fromRemoteToLocal(skinDtoIn.getChibi()));
                skinEntity.setBackgroundLink(remoteToLocalLinkCoverter.fromRemoteToLocal(skinDtoIn.getBackground()));
                skinEntity.setImageLink(remoteToLocalLinkCoverter.fromRemoteToLocal(skinDtoIn.getImage()));
            } else {
                ship.getSkins().add(skinDtoIn.toEntity(ship, remoteToLocalLinkCoverter));
            }
        }
    }

    private void mergeSkills(Ship ship, DownloadedShipEntityDtoIn dtoIn, List<Skill> skillsToDelete) {
        Map<String, Skill> skillNameToSkillMap = ship.getSkills().stream().collect(Collectors.toMap(skill -> skill.getName() + skill.getCnName(), Function.identity()));
        Map<String, DownloadedSkillEntityDtoIn> skillDtoNameToSkillMap = dtoIn.getSkills().stream().collect(Collectors.toMap(SkillDtoIn -> SkillDtoIn.getEnName() + SkillDtoIn.getCnName(), Function.identity()));
        // Delete all not contained skins
        List<Skill> currentSkillsToDelete = ship.getSkills().stream().filter((skill) -> !skillDtoNameToSkillMap.containsKey(skill.getName() + skill.getCnName())).toList();
        ship.getSkills().removeAll(currentSkillsToDelete);
        skillsToDelete.addAll(currentSkillsToDelete);

        for (var skillDtoIn : dtoIn.getSkills()) {
            if (skillNameToSkillMap.containsKey(skillDtoIn.getEnName() + skillDtoIn.getCnName())) {
                Skill skillEntity = skillNameToSkillMap.get(skillDtoIn.getEnName() + skillDtoIn.getCnName());
                skillEntity.setIconLink(remoteToLocalLinkCoverter.fromRemoteToLocal(skillDtoIn.getIcon()));
                skillEntity.setDescription(skillDtoIn.getDescription());
                skillEntity.setColor(skillDtoIn.getColor());
            } else {
                ship.getSkills().add(skillDtoIn.toEntity(ship, remoteToLocalLinkCoverter));
            }
        }
    }

    public Optional<Ship> getShipById(long id) {
        return shipRepository.findById(id);
    }

    public Page<Ship> listShips(ListShipsDtoIn dtoIn) {
        ShipsSearchCriteria criteria;
        PageInfoDtoIn pageInfoDtoIn;
        if (dtoIn.getSearchCriteria() == null) {
            criteria = new ShipsSearchCriteria();
        } else {
            criteria = dtoIn.getSearchCriteria().toDbCriteria();
        }
        if (dtoIn.getPageInfo() == null) {
            pageInfoDtoIn = new PageInfoDtoIn();
        } else {
            pageInfoDtoIn = dtoIn.getPageInfo();
        }
        PageRequest pageRequest = PageRequest.of(pageInfoDtoIn.getPageIndex(), pageInfoDtoIn.getPageSize());
        if (dtoIn.getSortCriteria() != null) {
            for (Map.Entry<String, String> entry : dtoIn.getSortCriteria().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                pageRequest = pageRequest.withSort(pageRequest.getSort().and(Sort.by(value.toUpperCase().equals(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC, key)));
            }
        }
        //Add default sort
        pageRequest = pageRequest.withSort(pageRequest.getSort().and(Sort.by(Sort.Direction.ASC, "id")));
        return shipRepository.findAll(createFilterQuery(criteria), pageRequest);
    }

    public Optional<Ship> deleteShipById(long id) {
        Optional<Ship> deletedShip = shipRepository.findById(id);
        shipRepository.deleteById(id);
        return deletedShip;
    }

    public void dropShips() {
        shipRepository.deleteAll();
    }
}
