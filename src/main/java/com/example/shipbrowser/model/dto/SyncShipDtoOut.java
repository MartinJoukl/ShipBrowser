package com.example.shipbrowser.model.dto;

import com.example.shipbrowser.dao.Ship;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SyncShipDtoOut extends DtoOut {
    private String additionalMessage;
    private List<ShipDtoOut> synchronizedShips;

    public SyncShipDtoOut(List<Ship> ships) {
        this.synchronizedShips = new ArrayList<>();
        for (Ship ship : ships) {
            synchronizedShips.add(new ShipDtoOut(ship));
        }
    }
}
