package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import com.example.shipbrowser.repository.ShipsSearchCriteria;
import com.example.shipbrowser.repository.SkinSearchCriteria;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListSkinCriteriaDtoIn extends DtoIn {
    private String name;
    @Valid
    private ListShipsSearchCriteriaDtoIn ship;


    public SkinSearchCriteria toDbCriteria() {
        SkinSearchCriteria criteria = new SkinSearchCriteria();
        criteria.setName(name);
        if (ship != null) {
            criteria.setShipCode(ship.getCode());
            criteria.setShipShipClass(ship.getShipClass());
            criteria.setShipNationality(ship.getNationality());
            if (ship.getHullType() != null && !ship.getHullType().isBlank()) {
                criteria.setShipHullType(HullType.fromString(ship.getHullType()));
            }
            if (ship.getRarity() != null && !ship.getRarity().isBlank()) {
                criteria.setShipRarity(Rarity.fromString(ship.getRarity()));
            }
        }
        return criteria;
    }

}
