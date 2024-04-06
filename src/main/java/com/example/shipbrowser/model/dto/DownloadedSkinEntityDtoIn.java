package com.example.shipbrowser.model.dto;

import com.example.shipbrowser.dao.Ship;
import com.example.shipbrowser.dao.Skill;
import com.example.shipbrowser.dao.Skin;
import com.example.shipbrowser.dao.StoredImage;
import com.example.shipbrowser.helpers.RemoteToLocalLinkCoverter;
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
        skin.setImageLink(RemoteToLocalLinkCoverter.fromRemoteToLocal(image));
        skin.setBackgroundLink(RemoteToLocalLinkCoverter.fromRemoteToLocal(background));
        skin.setChibiLink(RemoteToLocalLinkCoverter.fromRemoteToLocal(chibi));
        return skin;
    }

    public boolean equalsToEntity(Skin skinEntity) {
        return (image == null && skinEntity.getImageLink() == null || Objects.equals(RemoteToLocalLinkCoverter.fromRemoteToLocal(image), skinEntity.getImageLink()))
                && (background == null && skinEntity.getBackgroundLink() == null || Objects.equals(RemoteToLocalLinkCoverter.fromRemoteToLocal(background), skinEntity.getBackgroundLink()))
                && (chibi == null && skinEntity.getChibiLink() == null || Objects.equals(RemoteToLocalLinkCoverter.fromRemoteToLocal(chibi), skinEntity.getChibiLink())) &&
                Objects.equals(name, skinEntity.getName());

    }
}
