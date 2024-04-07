package com.example.shipbrowser.model.dto.dtoOut;

import com.example.shipbrowser.model.dto.dtoOut.DtoOut;
import com.example.shipbrowser.model.dto.dtoOut.ShipDtoOut;
import com.example.shipbrowser.repository.Ship;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SyncShipDtoOut implements DtoOut {
    private String additionalMessage;
    private List<ShipDtoOut> itemList;

    public SyncShipDtoOut(List<Ship> ships) {
        this.itemList = new ArrayList<>();
        for (Ship ship : ships) {
            itemList.add(new ShipDtoOut(ship));
        }
    }
}
