package com.example.shipbrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoredImageRepository extends JpaRepository<StoredImage, Long> {
    List<StoredImage> getStoredImageByStoredLocation(String storedLocation);
}
