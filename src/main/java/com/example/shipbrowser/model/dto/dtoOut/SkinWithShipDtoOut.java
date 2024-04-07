package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Skin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkinWithShipDtoOut extends DtoOut {
    private String name;
    private String imageLink;
    private String backgroundLink;
    private String chibiLink;
    private ReducedShipDtoOut ship;

    public SkinWithShipDtoOut(Skin skin) {
        this.name = skin.getName();
        this.imageLink = skin.getImageLink();
        this.backgroundLink = skin.getBackgroundLink();
        this.chibiLink = skin.getChibiLink();
        this.ship = new ReducedShipDtoOut(skin.getShip());
    }
}
