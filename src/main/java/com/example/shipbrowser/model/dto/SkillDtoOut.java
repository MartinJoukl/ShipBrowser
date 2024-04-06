package com.example.shipbrowser.model.dto;
import com.example.shipbrowser.dao.Skill;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillDtoOut {
    private String iconLink;
    private String name;
    private String description;
    private String color;

    public SkillDtoOut(Skill skill) {
        this.iconLink = skill.getIconLink();
        this.name = skill.getName();
        this.description = skill.getDescription();
        this.color = skill.getColor();
    }
}
