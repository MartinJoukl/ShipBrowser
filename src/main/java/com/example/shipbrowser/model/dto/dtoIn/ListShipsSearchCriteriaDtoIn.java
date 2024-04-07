package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import com.example.shipbrowser.repository.ShipsSearchCriteria;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.aspectj.lang.annotation.RequiredTypes;

@Data
public class ListShipsSearchCriteriaDtoIn {
    private String name;
    private String code;
    private String shipClass;
    private String nationality;
    @Pattern(regexp = "Destroyer|Aircraft Carrier|Battleship|Light Cruiser|Heavy Cruiser|Large Cruiser|Monitor|Battlecruiser|Aviation Battleship|Light Carrier|Repair|Munition Ship|Submarine|Sailing Frigate|Submarine Carrier",
            message = "Invalid ship type. Must be one of: Destroyer, Aircraft Carrier, Battleship, Light Cruiser, Heavy Cruiser, Large Cruiser, Monitor, Battlecruiser, Aviation Battleship, Light Carrier, Repair, Munition Ship, Submarine, Sailing Frigate, Submarine Carrier")
    private String hullType;
    @Pattern(regexp = "Normal|Rare|Elite|Priority|Super Rare|Decisive|Ultra Rare", message = "Invalid rarity, must be one of: Normal, Rare, Elite, Priority, Super Rare, Decisive, Ultra Rare")
    private String rarity;


    public ShipsSearchCriteria toDbCriteria() {
        ShipsSearchCriteria criteria = new ShipsSearchCriteria();
        criteria.setName(name);
        criteria.setCode(code);
        criteria.setShipClass(shipClass);
        criteria.setNationality(nationality);
        if (hullType != null && !hullType.isBlank()) {
            criteria.setHullType(HullType.fromString(hullType));
        }
        if (rarity != null && !rarity.isBlank()) {
            criteria.setRarity(Rarity.fromString(rarity));
        }
        return criteria;
    }
}
