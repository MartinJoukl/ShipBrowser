package com.example.shipbrowser.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum HullType {
    DESTROYER("Destroyer"),
    CARRIER("Aircraft Carrier"),
    BATTLESHIP("Battleship"),
    LIGHT_CRUISER("Light Cruiser"),
    HEAVY_CRUISER("Heavy Cruiser"),
    LARGE_CRUISER("Large Cruiser"),
    MONITOR("Monitor"),
    BATTLECRUISER("Battlecruiser"),
    AVIATION_BATTLESHIP("Aviation Battleship"),
    LIGHT_AIRCRAFT_CARRIER("Light Carrier"),
    AIRCRAFT_CARRIER("Aircraft Carrier"),
    REPAIR_SHIP("Repair"),
    MUNITION_SHIP("Munition Ship"),
    SUBMARINE("Submarine"),
    SAILING_FRIGATE("Sailing Frigate"),
    SUBMARINE_CARRIER("Submarine Carrier");

    private String name;

    HullType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static HullType forValues(String hullType) {
        for (HullType hullTypeValue : HullType.values()) {
            if (hullTypeValue.name.equals(hullType)) {
                return hullTypeValue;
            }
        }

        return null;
    }
}
