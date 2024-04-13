package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Skin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkinWithShipDtoOut extends DtoOut {
    private Long id;
    private String name;
    private Integer cost;
    private ReducedShipDtoOut ship;

    public SkinWithShipDtoOut(Skin skin) {
        this.id = skin.getId();
        this.name = skin.getName();
        this.cost = skin.getCost();
        this.ship = new ReducedShipDtoOut(skin.getShip());
    }
}
