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
        System.out.println("Downloading of images began");
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
    public void createSkinPreviews() throws IOException {
        System.out.println("Started generating images");
        Path currentPath = Path.of(remoteToLocalLinkCoverter.getImagesBaseLocation().toString(), "skins");
        if (Files.isDirectory(currentPath)) {
            try (Stream<Path> paths = Files.walk(currentPath)) {
                paths
                        .filter(Files::isDirectory)
                        .forEach((this::createPreviewOfSkinsInDir));
            }
        }
        System.out.println("Image generation ended");
    }

    private void createPreviewOfSkinsInDir(Path path) {
        try {
            try (Stream<Path> paths = Files.walk(path)) {
                paths
                        .filter(Files::isDirectory)
                        .forEach((skinDirectory) -> {
                            try {
                                System.out.println(path.toString());
                                BufferedImage skinImage = ImageIO.read(new File(Path.of(path.toString(), "image.png").toUri()));
                                BufferedImage resizedImage = Scalr.resize(skinImage, 512);
                                File outputfile = new File(Path.of(path.toString(), "preview.png").toUri());
                                ImageIO.write(resizedImage, "png", outputfile);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        } catch (Exception e) {
            System.out.println("Creating of preview failed for " + path.toString());
        }
    }
}
