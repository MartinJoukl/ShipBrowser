package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.service.RemoteToLocalLinkCoverter;
import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DownloadedShipEntityDtoIn {
    private String wikiUrl;
    private String id;
    private DownloadedNamesDto names;

    private DownloadedExistsDto exists;

    @JsonProperty("class")
    private String shipClass;

    private String nationality;

    private HullType hullType;

    private String thumbnail;
    private Rarity rarity;

    private List<DownloadedSkillEntityDtoIn> skills;
    private DownloadedConstructionDto construction;
    private DownloadedObtainedFromDto obtainedFrom;
    private List<DownloadedSkinEntityDtoIn> skins;

    public Boolean existsOnEn() {
        return exists.en;
    }

    public String getEnglishName() {
        return names.en;
    }

    public Ship toEntity(RemoteToLocalLinkCoverter remoteToLocalLinkCoverter) {
        Ship ship = new Ship();
        ship.setWikiUrl(wikiUrl);
        ship.setName(names.en);
        ship.setCode(names.getCode());
        ship.setOriginalId(id);
        ship.setShipClass(shipClass);
        ship.setNationality(nationality);
        ship.setHullType(hullType);
        ship.setObtainedFrom(obtainedFrom.obtainedFrom);
        ship.setThumbnailLink(remoteToLocalLinkCoverter.fromRemoteToLocal(thumbnail));
        ship.setRarity(rarity);
        ship.setSkills((skills.stream().map((skin) -> skin.toEntity(ship, remoteToLocalLinkCoverter)).toList()));
        ship.setConstructionTime(construction.constructionTime);

        ship.setSkins(skins.stream().map((skin) -> skin.toEntity(ship, remoteToLocalLinkCoverter)).toList());
        return ship;
    }

    public String getConstructionTime() {
        return construction.constructionTime;
    }

    @Data
    private static class DownloadedConstructionDto {
        private String constructionTime;
    }

    @Data
    private static class DownloadedNamesDto {
        String code;
        String en;
    }

    @Data
    private static class DownloadedExistsDto {
        Boolean en;
    }

    @Data
    private static class DownloadedObtainedFromDto {
        String obtainedFrom;
    }
}
