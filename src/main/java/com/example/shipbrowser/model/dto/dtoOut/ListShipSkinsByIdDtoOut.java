package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.repository.Skin;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListShipSkinsByIdDtoOut extends DtoOut {
    public List<SkinDtoOut> itemList;

    public ListShipSkinsByIdDtoOut(List<Skin> skins) {
        this.itemList = new ArrayList<>();
        for (Skin skin : skins) {
            itemList.add(new SkinDtoOut(skin));
        }
    }
}
