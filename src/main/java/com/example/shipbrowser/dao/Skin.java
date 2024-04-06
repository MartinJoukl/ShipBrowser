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

    @Column
    private String imageLink;
    @Column
    private String backgroundLink;

    @Column
    private String chibiLink;

    @ManyToOne
    private Ship ship;
}
