package com.example.shipbrowser.controller;

import com.example.shipbrowser.model.dto.dtoIn.CreateSkinImagePreviewsDtoIn;
import com.example.shipbrowser.model.dto.dtoOut.CreateSkinImagesPreviewsDtoOut;
import com.example.shipbrowser.model.dto.dtoOut.DtoOut;
import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.Skill;
import com.example.shipbrowser.repository.Skin;
import com.example.shipbrowser.service.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

@CrossOrigin(
        origins = {
                "http://localhost:3000"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })
@RestController
public class ImageController {
    private StoredImageService storedImageService;
    private ShipService shipService;
    private SkinService skinService;
    private SkillService skillService;
    private RemoteToLocalLinkCoverter remoteToLocalLinkCoverter;

    public ImageController(StoredImageService storedImageService, ShipService shipService, SkinService skinService, SkillService skillService, RemoteToLocalLinkCoverter remoteToLocalLinkCoverter) {
        this.shipService = shipService;
        this.skinService = skinService;
        this.storedImageService = storedImageService;
        this.skillService = skillService;
        this.remoteToLocalLinkCoverter = remoteToLocalLinkCoverter;
    }

    @PostMapping("createSkinImagesPreviews")
    public DtoOut createSkinImagesPreviews(@RequestBody CreateSkinImagePreviewsDtoIn dtoIn) throws IOException {
        if (dtoIn.getRegenerate() == null) {
            dtoIn.setRegenerate(false);
        }
        storedImageService.createSkinPreviews(dtoIn.getRegenerate());
        return new CreateSkinImagesPreviewsDtoOut("Previews are being generated on background.");
    }

    @GetMapping(value = "getShipImage", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getShipImage(@RequestParam long id) throws IOException {
        ByteArrayResource inputStream;
        try {
            Optional<Ship> ship = shipService.getShipById(id);
            if (ship.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            inputStream = storedImageService.getImage(ship.get().getThumbnailLink());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping(value = "getSkinImage", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getSkinImage(@RequestParam long id) {
        ByteArrayResource inputStream;
        try {
            Optional<Skin> skin = skinService.getBySkinId(id);
            if (skin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            inputStream = storedImageService.getImage(skin.get().getImageLink());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping(value = "getSkinImagePreview", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getSkinImagePreview(@RequestParam long id) {
        ByteArrayResource inputStream;
        try {
            Optional<Skin> skin = skinService.getBySkinId(id);
            if (skin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            inputStream = storedImageService.getImage(
                    remoteToLocalLinkCoverter.getSkinPreviewLocation(
                            Path.of(skin.get().getImageLink())
                    ).toString()
            );
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping(value = "getSkinBackground", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getSkinBackground(@RequestParam long id) {
        ByteArrayResource inputStream;
        try {
            Optional<Skin> skin = skinService.getBySkinId(id);
            if (skin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            inputStream = storedImageService.getImage(skin.get().getBackgroundLink());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping(value = "getSkinBackgroundPreview", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getSkinBackgroundPreview(@RequestParam long id) {
        ByteArrayResource inputStream;
        try {
            Optional<Skin> skin = skinService.getBySkinId(id);
            if (skin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            inputStream = storedImageService.getImage(
                    remoteToLocalLinkCoverter.getBackgroundPreviewLocation(
                            Path.of(skin.get().getBackgroundLink())
                    ).toString()
            );
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping(value = "getSkinChibi", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getSkinChibi(@RequestParam long id) {
        ByteArrayResource inputStream;
        try {
            Optional<Skin> skin = skinService.getBySkinId(id);
            if (skin.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            inputStream = storedImageService.getImage(skin.get().getChibiLink());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }

    @GetMapping(value = "getSkillImage", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<Resource> getSkillImage(@RequestParam long id) {
        ByteArrayResource inputStream;
        try {
            Optional<Skill> skill = skillService.findById(id);
            if (skill.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            inputStream = storedImageService.getImage(skill.get().getIconLink());
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentLength(inputStream.contentLength())
                .body(inputStream);
    }
}
