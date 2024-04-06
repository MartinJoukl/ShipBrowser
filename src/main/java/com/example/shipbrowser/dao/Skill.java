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

    @ManyToOne()
    @JoinColumn(name = "icon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StoredImage icon;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column
    private String color;
}
