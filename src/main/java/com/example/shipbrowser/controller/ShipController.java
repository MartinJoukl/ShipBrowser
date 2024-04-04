package com.example.shipbrowser.controller;

import com.example.shipbrowser.model.dto.DtoOut;
import com.example.shipbrowser.model.dto.synchronizeShipsDtoOut;
import com.example.shipbrowser.service.ShipService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ShipController {

    private final ShipService shipService;

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @PutMapping("synchronizeShips")
    public DtoOut synchronizeShips() throws IOException {

        shipService.synchronizeShipsWithRemote();
        return new synchronizeShipsDtoOut();
    }
}
