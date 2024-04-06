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

    @Column
    private String iconLink;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column
    private String color;
}
