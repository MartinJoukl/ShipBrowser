package com.example.shipbrowser.controller;

import com.example.shipbrowser.model.dto.dtoIn.ListShipsDtoIn;
import com.example.shipbrowser.model.dto.dtoIn.PageInfoDtoIn;
import com.example.shipbrowser.model.dto.dtoOut.DtoOut;
import com.example.shipbrowser.model.dto.dtoOut.ListShipsDtoOut;
import com.example.shipbrowser.model.dto.dtoOut.ShipDtoOut;
import com.example.shipbrowser.model.dto.dtoOut.SyncShipDtoOut;
import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.model.exception.ObjectNotFoundById;
import com.example.shipbrowser.service.ShipService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("getShip")
    public DtoOut getShip(@RequestParam long id) {
        Optional<Ship> currentShip = shipService.getShipById(id);
        if (currentShip.isEmpty()) {
            throw new ObjectNotFoundById("Given ship was not found by ID.");
        }
        return new ShipDtoOut(currentShip.get());
    }

    @GetMapping("listShips")
    public DtoOut listShips(@Valid ListShipsDtoIn dtoIn) {
        if (dtoIn.getPageInfo() == null) {
            dtoIn.setPageInfo(new PageInfoDtoIn(0, 20));
        }
        Page<Ship> ships = shipService.listShips(dtoIn);
        return new ListShipsDtoOut(ships, dtoIn.getPageInfo());
    }
}
