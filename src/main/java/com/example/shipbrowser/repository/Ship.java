package com.example.shipbrowser.repository;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String originalId;

    @Column
    private String wikiUrl;
    @Column
    @NotNull
    @NotBlank
    private String name;

    @Column
    @NotNull
    @NotBlank
    private String code;
    @Column
    @NotNull
    private String shipClass;
    @Column
    @NotNull
    private String nationality;
    @Column
    @NotNull
    private HullType hullType;
    @Column
    @NotNull
    private Rarity rarity;
    @OneToMany(cascade = CascadeType.ALL)
    @NotNull
    private List<Skill> skills;

    @Column
    private String thumbnailLink;

    @Column
    private String constructionTime;
    @Column(length = 1024)
    private String obtainedFrom;

    @OneToMany(cascade = CascadeType.ALL)
    @NotNull
    private List<Skin> skins;
}
