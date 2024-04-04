package com.example.shipbrowser.model.dto;

import com.example.shipbrowser.dao.Ship;
import com.example.shipbrowser.dao.Skill;
import com.example.shipbrowser.dao.Skin;
import com.example.shipbrowser.dao.StoredImage;
import lombok.Data;

import java.util.Objects;

@Data
public class DownloadedSkinEntityDtoIn {
    private String name;
    private String image;
    private String background;
    private String chibi;

    public Skin toEntity(Ship ship) {
        Skin skin = new Skin();
        skin.setName(name);
        skin.setShip(ship);
        StoredImage mainImage = new StoredImage();
        mainImage.setOriginalSource(image);
        skin.setImage(mainImage);
        StoredImage backgroundImage = new StoredImage();
        backgroundImage.setOriginalSource(background);
        skin.setBackground(backgroundImage);
        StoredImage chibiImage = new StoredImage();
        chibiImage.setOriginalSource(chibi);
        skin.setChibi(chibiImage);
        return skin;
    }

    public boolean equalsToEntity(Skin skinEntity) {
        return (image == null && skinEntity.getImage() == null || Objects.equals(image, skinEntity.getImage().getOriginalSource()))
                && (background == null && skinEntity.getBackground() == null || Objects.equals(background, skinEntity.getBackground().getOriginalSource()))
                && (chibi == null && skinEntity.getChibi() == null || Objects.equals(chibi, skinEntity.getChibi().getOriginalSource())) &&
                Objects.equals(name, skinEntity.getName());

    }
}
