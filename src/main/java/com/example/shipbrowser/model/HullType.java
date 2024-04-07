package com.example.shipbrowser.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum HullType {
    DESTROYER("Destroyer"),
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

    private final String name;

    HullType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static HullType fromString(String hullType) {
        for (HullType hullTypeValue : HullType.values()) {
            if (hullTypeValue.name.equals(hullType)) {
                return hullTypeValue;
            }
        }

        throw new RuntimeException("Invalid hullType");
    }
}
