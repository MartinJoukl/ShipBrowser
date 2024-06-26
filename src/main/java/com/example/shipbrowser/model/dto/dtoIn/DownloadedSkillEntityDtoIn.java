package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.Skill;
import com.example.shipbrowser.service.RemoteToLocalLinkCoverter;
import lombok.Data;

import java.util.Objects;

@Data
public class DownloadedSkillEntityDtoIn {
    private String icon;
    private DtoInNames names;
    private String description;
    private String color;

    public Skill toEntity(Ship ship, RemoteToLocalLinkCoverter remoteToLocalLinkCoverter) {
        Skill skill = new Skill();
        skill.setColor(color);
        skill.setShip(ship);
        skill.setIconLink(remoteToLocalLinkCoverter.fromRemoteToLocal(icon));
        skill.setName(names.en);
        skill.setDescription(description);
        skill.setCnName(names.cn);
        return skill;
    }

    public String getEnName() {
        if (names == null) {
            return null;
        }
        return names.en;
    }

    public String getCnName() {
        if (names == null) {
            return null;
        }
        return names.cn;
    }

    public boolean equalsToEntity(Skill skillEntity, RemoteToLocalLinkCoverter remoteToLocalLinkCoverter) {
        return (icon == null && skillEntity.getIconLink() == null || Objects.equals(remoteToLocalLinkCoverter.fromRemoteToLocal(icon), skillEntity.getIconLink()))
                && Objects.equals(skillEntity.getColor(), color)
                && Objects.equals(skillEntity.getName(), names.en)
                && Objects.equals(skillEntity.getName(), names.cn)
                && Objects.equals(skillEntity.getDescription(), description);
    }

    @Data
    private static class DtoInNames {
        private String en;
        private String cn;
    }
}
