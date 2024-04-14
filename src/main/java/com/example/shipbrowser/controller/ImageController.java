package com.example.shipbrowser.controller;

import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.repository.Skill;
import com.example.shipbrowser.repository.Skin;
import com.example.shipbrowser.service.ShipService;
import com.example.shipbrowser.service.SkillService;
import com.example.shipbrowser.service.SkinService;
import com.example.shipbrowser.service.StoredImageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    public ImageController(StoredImageService storedImageService, ShipService shipService, SkinService skinService, SkillService skillService) {
        this.shipService = shipService;
        this.skinService = skinService;
        this.storedImageService = storedImageService;
        this.skillService = skillService;
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
