package com.example.shipbrowser.repository;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    // Just for synchronization purposes... for example (Fate Simulation) and retrofit (Fate Simulation) is named same...
    @Column
    private String cnName;

    @Column(length = 1024)
    private String description;

    @Column
    private String color;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return id.equals(skill.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
