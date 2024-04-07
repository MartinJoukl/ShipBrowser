package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.model.dto.dtoIn.PageInfoDtoIn;
import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.Skill;
import com.example.shipbrowser.repository.Skin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListSkinsDtoOut extends DtoOut {
    private List<SkinWithShipDtoOut> itemList;
    private PageInfoDtoOut pageInfo;

    public ListSkinsDtoOut(Page<Skin> skins, PageInfoDtoIn dtoInPageInfo) {
        this.itemList = new ArrayList<>();
        for (Skin skin : skins) {
            itemList.add(new SkinWithShipDtoOut(skin));
        }
        pageInfo = new PageInfoDtoOut();
        pageInfo.setTotal(skins.getTotalElements());
        pageInfo.setPageIndex(dtoInPageInfo.getPageIndex());
        pageInfo.setPageSize(dtoInPageInfo.getPageSize());
    }
}
