package com.example.shipbrowser.dao;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Ship ship;

    @OneToOne
    private StoredImage icon;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String color;
}
