package com.example.shipbrowser.controller;

import com.example.shipbrowser.model.dto.dtoIn.ListSkinDtoIn;
import com.example.shipbrowser.model.dto.dtoIn.PageInfoDtoIn;
import com.example.shipbrowser.model.dto.dtoOut.*;
import com.example.shipbrowser.model.exception.ObjectNotFoundById;
import com.example.shipbrowser.repository.Skin;
import com.example.shipbrowser.service.SkinService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(
        origins = {
                "http://localhost:3000",
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })
@RestController
public class SkinController {
    private final SkinService skinService;

    public SkinController(SkinService skinService) {
        this.skinService = skinService;
    }


    @GetMapping("getSkin")
    public DtoOut getShip(@RequestParam long id) {
        Optional<Skin> currentSkin = skinService.getBySkinId(id);
        if (currentSkin.isEmpty()) {
            throw new ObjectNotFoundById("Given skin was not found by ID.");
        }
        return new SkinWithShipDtoOut(currentSkin.get());
    }

    @PostMapping("listSkins")
    public DtoOut listSkin(@Valid @RequestBody ListSkinDtoIn dtoIn) {
        if (dtoIn.getPageInfo() == null) {
            dtoIn.setPageInfo(new PageInfoDtoIn(0, 20));
        }
        Page<Skin> skins = skinService.listSkins(dtoIn);
        return new ListSkinsDtoOut(skins, dtoIn.getPageInfo());
    }

    @GetMapping("listSkinsByShipId")
    public DtoOut listSkinByShipId(@RequestParam long shipId) {
        List<Skin> skins = skinService.listAllShipSkins(shipId);
        return new ListShipSkinsByIdDtoOut(skins);
    }

}
