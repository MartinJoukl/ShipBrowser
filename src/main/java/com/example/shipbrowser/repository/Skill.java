package com.example.shipbrowser.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @NotNull
    private Ship ship;

    @Column
    private String iconLink;

    @Column
    @NotNull
    @NotBlank
    private String name;

    @Column(length = 1024)
    private String description;

    @Column
    private String color;
}
