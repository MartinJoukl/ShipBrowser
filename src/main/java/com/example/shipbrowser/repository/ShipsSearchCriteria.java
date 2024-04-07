package com.example.shipbrowser.repository;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class ShipsSearchCriteria {
    private String name;
    private String code;
    private String shipClass;
    private String nationality;

    private HullType hullType;
    private Rarity rarity;
}