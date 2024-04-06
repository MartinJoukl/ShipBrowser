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

    @ManyToOne()
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StoredImage image;
    @ManyToOne()
    @JoinColumn(name = "background_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StoredImage background;

    @ManyToOne()
    @JoinColumn(name = "chibi_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StoredImage chibi;

    @ManyToOne
    private Ship ship;
}
