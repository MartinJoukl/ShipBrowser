package com.example.shipbrowser.service;

import com.example.shipbrowser.repository.StoredImage;
import com.example.shipbrowser.repository.StoredImageRepository;
import com.example.shipbrowser.helpers.RemoteToLocalLinkCoverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Service
public class StoredImageService {
    private StoredImageRepository storedImageRepository;
    private final HttpClient httpClient;

    public StoredImageService(StoredImageRepository storedImageRepository, HttpClient httpClient) {
        this.storedImageRepository = storedImageRepository;
        this.httpClient = httpClient;
    }

    //https://raw.githubusercontent.com/AzurAPI/azurapi-js-setup/master/images/skins/Collab057/Summer_Vacation/chibi.png
    @Async
    public void downloadAllImagesToLocal(Set<String> localLinks) throws IOException {
        System.out.println("Downloading of images began");
        for (String localLink : localLinks) {

            Path possibleImagePath = Path.of(localLink);
            if (!Files.exists(possibleImagePath)) {
                try {
                    String remotePath = RemoteToLocalLinkCoverter.fromLocalToAzurApiImages(localLink);
                    byte[] imageBytes = httpClient.returnImageBytes(remotePath);
                    Files.createDirectories(possibleImagePath.getParent());
                    Files.write(possibleImagePath, imageBytes);
                } catch (HttpClientErrorException e) {
                    // probably null image
                    //TODO - call with retry?
                    System.out.println(e);
                }
            }
        }
        System.out.println("Downloading of images ended");
    }

    public List<StoredImage> saveImages(List<StoredImage> imagesToInsert) {
        return storedImageRepository.saveAll(imagesToInsert);
    }
}
