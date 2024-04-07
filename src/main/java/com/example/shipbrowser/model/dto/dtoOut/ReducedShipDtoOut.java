package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Ship;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReducedShipDtoOut {
    private Long id;
    private String originalId;
    private String wikiUrl;
    private String name;
    private String code;
    private String shipClass;
    private String nationality;
    private String hullType;
    private String rarity;
    private String thumbnailLink;
    private String constructionTime;
    private String obtainedFrom;


    public ReducedShipDtoOut(Ship ship) {
        this.id = ship.getId();
        this.originalId = ship.getOriginalId();
        this.wikiUrl = ship.getWikiUrl();
        this.name = ship.getName();
        this.code = ship.getCode();
        this.shipClass = ship.getShipClass();
        this.nationality = ship.getNationality();
        this.hullType = ship.getHullType().getName();
        this.rarity = ship.getRarity().getName();
        this.thumbnailLink = ship.getThumbnailLink();
        this.constructionTime = ship.getConstructionTime();
        this.obtainedFrom = ship.getObtainedFrom();
    }
}
