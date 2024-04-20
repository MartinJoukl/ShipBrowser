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
    private boolean includeDefaultAndRetrofit;
    @Valid
    private ListShipsSearchCriteriaDtoIn ship;


    public SkinSearchCriteria toDbCriteria() {
        SkinSearchCriteria criteria = new SkinSearchCriteria();
        criteria.setName(name);
        criteria.setIncludeDefaultAndRetrofit(includeDefaultAndRetrofit);
        if (ship != null) {
            criteria.setShipName(ship.getName());
            criteria.setShipCode(ship.getCode());
            criteria.setShipShipClass(ship.getShipClass());
            criteria.setShipNationality(ship.getNationality());
            criteria.setIncludeDefaultAndRetrofit(includeDefaultAndRetrofit);
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
