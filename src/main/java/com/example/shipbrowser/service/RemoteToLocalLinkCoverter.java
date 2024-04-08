package com.example.shipbrowser.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class RemoteToLocalLinkCoverter {

    @Value("${httpService.uri.azur-api-url}")
    private String azurApiShipgirlUrl;
    private final String imagesBaseLocation = "../images";
    private final String images = "/images";

    public String fromRemoteToLocal(String originalSource) {
        if (originalSource == null) {
            return null;
        }
        String determinedImagePath = originalSource.split("/images/")[1];

        Path possibleImagePath = Path.of(imagesBaseLocation, determinedImagePath);
        return possibleImagePath.toString();
    }

    public String fromLocalToAzurApiImages(String localSource) {
        if (localSource == null) {
            return null;
        }
        String determinedImagePath = localSource.replaceAll("\\\\", "/");
        determinedImagePath = determinedImagePath.split("/images/")[1];

        determinedImagePath = azurApiShipgirlUrl + images + "/" + determinedImagePath;
        return determinedImagePath;
    }

    public Path fromRemoteToLocalAsPath(String originalSource) {
        if (originalSource == null) {
            return null;
        }
        String determinedImagePath = originalSource.split("/images/")[1];

        return Path.of(imagesBaseLocation, determinedImagePath);
    }
}
