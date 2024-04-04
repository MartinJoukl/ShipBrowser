package com.example.shipbrowser.dao;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String originalId;

    @Column
    private String wikiUrl;
    @Column
    private String name;

    @Column
    private String code;
    @Column
    private String shipClass;
    @Column
    private String nationality;
    @Column
    private HullType hullType;
    @Column
    private Rarity rarity;
    @OneToMany
    private List<Skill> skills;

    @OneToOne
    private StoredImage thumbnail;

    @Column
    private ZonedDateTime constructionTime;
    @Column
    private String obtainedFrom;

    @OneToMany
    private List<Skin> skins;
}
