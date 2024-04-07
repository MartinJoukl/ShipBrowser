package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.model.dto.dtoIn.PageInfoDtoIn;
import com.example.shipbrowser.repository.Ship;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListShipsDtoOut implements DtoOut {
    private List<ShipDtoOut> itemList;
    private PageInfoDtoOut pageInfo;

    public ListShipsDtoOut(Page<Ship> ships, PageInfoDtoIn dtoInPageInfo) {
        this.itemList = new ArrayList<>();
        for (Ship ship : ships) {
            itemList.add(new ShipDtoOut(ship));
        }
        pageInfo = new PageInfoDtoOut();
        pageInfo.setTotal(ships.getTotalElements());
        pageInfo.setPageIndex(dtoInPageInfo.getPageIndex());
        pageInfo.setPageSize(dtoInPageInfo.getPageSize());
    }
}
