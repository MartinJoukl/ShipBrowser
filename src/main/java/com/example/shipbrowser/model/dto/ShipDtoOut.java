package com.example.shipbrowser.model.dto;

import com.example.shipbrowser.dao.Ship;
import com.example.shipbrowser.dao.Skill;
import com.example.shipbrowser.dao.Skin;
import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShipDtoOut {
    private Long id;
    private String originalId;
    private String wikiUrl;
    private String name;
    private String code;
    private String shipClass;
    private String nationality;
    private HullType hullType;
    private Rarity rarity;
    private List<SkillDtoOut> skills;
    private String thumbnailLink;
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
        this.hullType = ship.getHullType();
        this.rarity = ship.getRarity();
        this.skills = ship.getSkills().stream().map(SkillDtoOut::new).toList();
        this.thumbnailLink = ship.getThumbnailLink();
        this.constructionTime = ship.getConstructionTime();
        this.obtainedFrom = ship.getObtainedFrom();
        this.skins = ship.getSkins().stream().map(SkinDtoOut::new).toList();
    }
}
