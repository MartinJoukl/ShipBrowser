package com.example.shipbrowser.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @OneToOne
    private StoredImage image;
    @OneToOne
    private StoredImage background;

    @OneToOne
    private StoredImage chibi;

    @ManyToOne
    private Ship ship;
}
