package com.example.shipbrowser.helpers;

import java.nio.file.Path;

import static com.example.shipbrowser.model.Constants.*;

public class RemoteToLocalLinkCoverter {
    public static String fromRemoteToLocal(String originalSource) {
        if (originalSource == null) {
            return null;
        }
        String determinedImagePath = originalSource.split("/images/")[1];

        Path possibleImagePath = Path.of(IMAGES_BASE_LOCATION, determinedImagePath);
        return possibleImagePath.toString();
    }

    public static String fromLocalToAzurApiImages(String localSource) {
        if (localSource == null) {
            return null;
        }
        String determinedImagePath = localSource.replaceAll("\\\\", "/");
        determinedImagePath = determinedImagePath.split("/images/")[1];

        determinedImagePath = AZUR_API_SHIPGIRL_IMAGE_URL + "/" + determinedImagePath;
        return determinedImagePath;
    }

    public static Path fromRemoteToLocalAsPath(String originalSource) {
        if (originalSource == null) {
            return null;
        }
        String determinedImagePath = originalSource.split("/images/")[1];

        return Path.of(IMAGES_BASE_LOCATION, determinedImagePath);
    }
}
