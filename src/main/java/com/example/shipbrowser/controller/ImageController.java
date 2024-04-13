package com.example.shipbrowser.controller;

import com.example.shipbrowser.repository.Ship;
import com.example.shipbrowser.service.ShipService;
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

    public ImageController(StoredImageService storedImageService, ShipService shipService) {
        this.shipService = shipService;
        this.storedImageService = storedImageService;
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
}
