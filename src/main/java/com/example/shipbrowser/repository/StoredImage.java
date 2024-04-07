package com.example.shipbrowser.repository;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StoredImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String originalSource;

    @Column
    private String storedLocation;
}
