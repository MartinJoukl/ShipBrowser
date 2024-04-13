package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.Skin;
import com.example.shipbrowser.service.RemoteToLocalLinkCoverter;
import lombok.Data;

import java.util.Objects;

@Data
public class DownloadedSkinEntityDtoIn {
    private String name;
    private String image;
    private String background;
    private String chibi;
    private Info info;

    public Skin toEntity(Ship ship, RemoteToLocalLinkCoverter remoteToLocalLinkCoverter) {
        Skin skin = new Skin();
        if (info == null || info.enClient == null || info.enClient.equals("Skin unavailable")) {
            skin.setName(name);
        } else {
            skin.setName(info.enClient);
        }
        if (info != null) {
            skin.setCost(info.cost);
        }
        skin.setShip(ship);
        skin.setImageLink(remoteToLocalLinkCoverter.fromRemoteToLocal(image));
        skin.setBackgroundLink(remoteToLocalLinkCoverter.fromRemoteToLocal(background));
        skin.setChibiLink(remoteToLocalLinkCoverter.fromRemoteToLocal(chibi));
        return skin;
    }

    public boolean equalsToEntity(Skin skinEntity, RemoteToLocalLinkCoverter remoteToLocalLinkCoverter) {
        return (image == null && skinEntity.getImageLink() == null || Objects.equals(remoteToLocalLinkCoverter.fromRemoteToLocal(image), skinEntity.getImageLink()))
                && (background == null && skinEntity.getBackgroundLink() == null || Objects.equals(remoteToLocalLinkCoverter.fromRemoteToLocal(background), skinEntity.getBackgroundLink()))
                && (chibi == null && skinEntity.getChibiLink() == null || Objects.equals(remoteToLocalLinkCoverter.fromRemoteToLocal(chibi), skinEntity.getChibiLink())) &&
                Objects.equals(name, skinEntity.getName());

    }

    @Data
    private static class Info {
        String enClient;
        Integer cost;
    }
}
