package com.example.shipbrowser.model.dto;

import com.example.shipbrowser.dao.Ship;
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

    public Ship toEntity() {
        Ship ship = new Ship();
        ship.setWikiUrl();
        ship.setName();
        ship.setShipClass();
        ship.setNationality();
        ship.setHullType();
        ship.setThumbnail();
        ship.setThumbnail();
        ship.setRarity();
        ship.setSkills();
        ship.setConstructionTime();
        ship.setSkins();
        return ship;
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
