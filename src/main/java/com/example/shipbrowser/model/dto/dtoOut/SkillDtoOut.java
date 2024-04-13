package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Skill;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillDtoOut extends DtoOut {
    private Long id;
    private String name;
    private String description;
    private String color;

    public SkillDtoOut(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.description = skill.getDescription();
        this.color = skill.getColor();
    }
}
