package com.example.shipbrowser.controller;

import com.example.shipbrowser.dao.Ship;
import com.example.shipbrowser.model.dto.DtoOut;
import com.example.shipbrowser.model.dto.SyncShipDtoOut;
import com.example.shipbrowser.service.ShipService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @PutMapping("synchronizeShips")
    public DtoOut synchronizeShips() throws IOException {

        List<Ship> affectedShips = shipService.synchronizeShipsWithRemote();
        SyncShipDtoOut dtoOut = new SyncShipDtoOut(affectedShips);
        dtoOut.setAdditionalMessage("Synchronization of images is being performed as background task.");
        return dtoOut;
    }
}
