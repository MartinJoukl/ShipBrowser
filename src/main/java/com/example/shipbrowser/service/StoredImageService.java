package com.example.shipbrowser.service;

import com.example.shipbrowser.repository.StoredImage;
import com.example.shipbrowser.repository.StoredImageRepository;
import org.imgscalr.Scalr;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.imageio.ImageIO;
import javax.naming.spi.DirectoryManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class StoredImageService {
    private final StoredImageRepository storedImageRepository;
    private final HttpClient httpClient;

    private final RemoteToLocalLinkCoverter remoteToLocalLinkCoverter;

    public StoredImageService(StoredImageRepository storedImageRepository, HttpClient httpClient, RemoteToLocalLinkCoverter remoteToLocalLinkCoverter) {
        this.storedImageRepository = storedImageRepository;
        this.httpClient = httpClient;
        this.remoteToLocalLinkCoverter = remoteToLocalLinkCoverter;
    }

    //https://raw.githubusercontent.com/AzurAPI/azurapi-js-setup/master/images/skins/Collab057/Summer_Vacation/chibi.png
    @Async
    public void downloadAllImagesToLocal(Set<String> localLinks) throws IOException {
        for (String localLink : localLinks) {

            Path possibleImagePath = Path.of(localLink);
            if (!Files.exists(possibleImagePath)) {
                try {
                    String remotePath = remoteToLocalLinkCoverter.fromLocalToAzurApiImages(localLink);
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

    public ByteArrayResource getImage(String path) throws IOException {
        return new ByteArrayResource(Files.readAllBytes(Paths.get(
                path)));
    }

    @Async
    public void createSkinPreviews(boolean regenerate) throws IOException {
        System.out.println("Started generating images");
        Path currentPath = Path.of(remoteToLocalLinkCoverter.getImagesBaseLocation().toString(), "skins");
        if (Files.isDirectory(currentPath)) {
            try (Stream<Path> paths = Files.walk(currentPath, 1)) {
                paths
                        .filter(Files::isDirectory)
                        .skip(1)
                        .forEach(((path) -> createPreviewOfSkinsInDir(path, regenerate)));
            }
        }
        System.out.println("Image generation ended");
        System.out.println("Started generating image backgrounds");
        currentPath = Path.of(remoteToLocalLinkCoverter.getImagesBaseLocation().toString(), "backgrounds");
        if (Files.isDirectory(currentPath)) {
            try (Stream<Path> paths = Files.walk(currentPath, 1)) {
                paths
                        .filter((path) -> Files.isRegularFile(path) && !path.getFileName().toString().endsWith("-preview.png"))
                        .forEach(((path) -> createBackgroundPreview(path, regenerate)));
            }
        }
        System.out.println("Generating of image backgrounds finished");
    }

    private void createPreviewOfSkinsInDir(Path path, boolean regenerate) {
        try {
            try (Stream<Path> paths = Files.walk(path, 1)) {
                paths
                        .filter(Files::isDirectory)
                        .skip(1)
                        .forEach((skinDirectory) -> {
                            try {
                                Path pathToCurrentSkinPreview = Path.of(skinDirectory.toString(), "preview.png");
                                if (!regenerate && Files.exists(pathToCurrentSkinPreview)) {
                                    return;
                                }
                                BufferedImage skinImage = ImageIO.read(new File(Path.of(skinDirectory.toString(), "image.png").toUri()));
                                BufferedImage resizedImage = Scalr.resize(skinImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 300, Scalr.OP_ANTIALIAS);
                                File outputfile = new File(pathToCurrentSkinPreview.toUri());
                                ImageIO.write(resizedImage, "png", outputfile);
                            } catch (IOException e) {
                                System.out.println("Creating of preview failed for " + skinDirectory);
                                System.out.println(e.getMessage());
                                throw new RuntimeException(e);
                            }
                        });
            }
        } catch (Exception ignored) {
        }
    }

    private void createBackgroundPreview(Path path, boolean regenerate) {
        try {
            Path pathToCurrentBgPreview = Path.of(path.toString() + "-preview.png");
            if (!regenerate && Files.exists(pathToCurrentBgPreview)) {
                return;
            }
            BufferedImage skinImage = ImageIO.read(new File(Path.of(path.toString()).toUri()));
            BufferedImage resizedImage = Scalr.resize(skinImage, Scalr.Method.AUTOMATIC, Scalr.Mode.AUTOMATIC, 300, Scalr.OP_ANTIALIAS);
            File outputfile = new File(pathToCurrentBgPreview.toUri());
            ImageIO.write(resizedImage, "png", outputfile);
        } catch (IOException e) {
            System.out.println("Creating of background preview failed for " + path);
        }
    }
}
