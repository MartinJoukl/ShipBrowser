package com.example.shipbrowser.model.dto;

import com.example.shipbrowser.dao.Ship;
import com.example.shipbrowser.dao.Skill;
import com.example.shipbrowser.dao.StoredImage;
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
        StoredImage imageIcon = new StoredImage();
        imageIcon.setOriginalSource(icon);
        skill.setIcon(imageIcon);
        skill.setName(names.en);
        skill.setDescription(description);
        return skill;
    }

    public boolean equalsToEntity(Skill skillEntity) {
        return (icon == null && skillEntity.getIcon() == null || Objects.equals(icon, skillEntity.getIcon().getOriginalSource()))
                && Objects.equals(skillEntity.getColor(), color)
                && Objects.equals(skillEntity.getName(), names.en)
                && Objects.equals(skillEntity.getDescription(), description);
    }

    @Data
    private static class DtoInNames {
        private String en;
    }
}
