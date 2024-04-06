package com.example.shipbrowser.model.dto;

import com.example.shipbrowser.dao.Skin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkinDtoOut {
    private String name;
    private String imageLink;
    private String backgroundLink;
    private String chibiLink;

    public SkinDtoOut(Skin skin) {
        this.name = skin.getName();
        this.imageLink = skin.getImageLink();
        this.backgroundLink = skin.getBackgroundLink();
        this.chibiLink = skin.getChibiLink();
    }
}
