package com.example.shipbrowser.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @NotNull
    @NotBlank
    private String name;

    @Column
    private String imageLink;
    @Column
    private String backgroundLink;

    @Column
    private String chibiLink;

    @ManyToOne
    @NotNull
    private Ship ship;
}
