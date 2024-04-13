package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Skin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkinDtoOut extends DtoOut {

    private Long id;
    private String name;

    public SkinDtoOut(Skin skin) {
        this.name = skin.getName();
        this.id = skin.getId();
    }
}
