package com.example.shipbrowser.repository;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import lombok.Data;

@Data
public class SkinSearchCriteria {
    private String name;
    private String shipName;
    private String shipCode;
    private String shipShipClass;
    private String shipNationality;

    private HullType shipHullType;
    private Rarity shipRarity;
}