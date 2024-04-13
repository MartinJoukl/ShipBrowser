package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Skin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkinDtoOut extends DtoOut {

    private Long id;
    private String name;
    private Integer cost;

    public SkinDtoOut(Skin skin) {
        this.name = skin.getName();
        this.cost = skin.getCost();
        this.id = skin.getId();
    }
}
