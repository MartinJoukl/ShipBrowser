package com.example.shipbrowser.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.engine.internal.Cascade;

@Entity
@Data
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private StoredImage image;
    @OneToOne(cascade = CascadeType.ALL)
    private StoredImage background;

    @OneToOne(cascade = CascadeType.ALL)
    private StoredImage chibi;

    @ManyToOne
    private Ship ship;
}
