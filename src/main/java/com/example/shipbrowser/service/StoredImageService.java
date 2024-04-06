package com.example.shipbrowser.service;

import com.example.shipbrowser.dao.StoredImage;
import com.example.shipbrowser.dao.StoredImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.example.shipbrowser.model.Constants.IMAGES_BASE_LOCATION;

@Service
public class StoredImageService {
    private StoredImageRepository storedImageRepository;
    private final HttpClient httpClient;

    public StoredImageService(StoredImageRepository storedImageRepository, HttpClient httpClient) {
        this.storedImageRepository = storedImageRepository;
        this.httpClient = httpClient;
    }

    //https://raw.githubusercontent.com/AzurAPI/azurapi-js-setup/master/images/skins/Collab057/Summer_Vacation/chibi.png
    public void downloadAllImagesToLocal() throws IOException {
        List<StoredImage> notLocallySavedImages = storedImageRepository.getStoredImageByStoredLocation(null);
        for (StoredImage notLocallySavedImage : notLocallySavedImages) {
            String determinedImagePath = notLocallySavedImage.getOriginalSource().split("/images/")[1];

            Path possibleImagePath = Path.of(IMAGES_BASE_LOCATION, determinedImagePath);
            if (Files.exists(possibleImagePath)) {
                notLocallySavedImage.setStoredLocation(possibleImagePath.toString());
                storedImageRepository.save(notLocallySavedImage);
            } else {
                try {
                    byte[] imageBytes = httpClient.returnImageBytes(notLocallySavedImage.getOriginalSource());
                    Files.createDirectories(possibleImagePath.getParent());
                    Files.write(possibleImagePath, imageBytes);
                    storedImageRepository.save(notLocallySavedImage);
                } catch (HttpClientErrorException e) {
                    // probably null image
                    //TODO - call with retry?
                    System.out.println(e);
                }
            }
        }
    }

    public List<StoredImage> saveImages(List<StoredImage> imagesToInsert) {
        return storedImageRepository.saveAll(imagesToInsert);
    }
}
