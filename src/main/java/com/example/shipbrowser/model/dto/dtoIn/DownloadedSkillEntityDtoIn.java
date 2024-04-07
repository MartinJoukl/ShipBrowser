package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.Skill;
import com.example.shipbrowser.helpers.RemoteToLocalLinkCoverter;
import lombok.Data;

import java.util.Objects;

@Data
public class DownloadedSkillEntityDtoIn {
    private String icon;
    private DtoInNames names;
    private String description;
    private String color;

    public Skill toEntity(Ship ship) {
        Skill skill = new Skill();
        skill.setColor(color);
        skill.setShip(ship);
        skill.setIconLink(RemoteToLocalLinkCoverter.fromRemoteToLocal(icon));
        skill.setName(names.en);
        skill.setDescription(description);
        return skill;
    }

    public boolean equalsToEntity(Skill skillEntity) {
        return (icon == null && skillEntity.getIconLink() == null || Objects.equals(RemoteToLocalLinkCoverter.fromRemoteToLocal(icon), skillEntity.getIconLink()))
                && Objects.equals(skillEntity.getColor(), color)
                && Objects.equals(skillEntity.getName(), names.en)
                && Objects.equals(skillEntity.getDescription(), description);
    }

    @Data
    private static class DtoInNames {
        private String en;
    }
}
