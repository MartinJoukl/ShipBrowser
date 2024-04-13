package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Ship;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShipDtoOut extends DtoOut {
    private Long id;
    private String originalId;
    private String wikiUrl;
    private String name;
    private String code;
    private String shipClass;
    private String nationality;
    private String hullType;
    private String rarity;
    private List<SkillDtoOut> skills;
    private String constructionTime;
    private String obtainedFrom;
    private List<SkinDtoOut> skins;

    public ShipDtoOut(Ship ship) {
        this.id = ship.getId();
        this.originalId = ship.getOriginalId();
        this.wikiUrl = ship.getWikiUrl();
        this.name = ship.getName();
        this.code = ship.getCode();
        this.shipClass = ship.getShipClass();
        this.nationality = ship.getNationality();
        this.hullType = ship.getHullType().getName();
        this.rarity = ship.getRarity().getName();
        this.skills = ship.getSkills().stream().map(SkillDtoOut::new).toList();
        this.constructionTime = ship.getConstructionTime();
        this.obtainedFrom = ship.getObtainedFrom();
        this.skins = ship.getSkins().stream().map(SkinDtoOut::new).toList();
    }
}
