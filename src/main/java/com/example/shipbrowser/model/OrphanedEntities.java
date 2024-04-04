package com.example.shipbrowser.model;

import com.example.shipbrowser.dao.Skill;
import com.example.shipbrowser.dao.Skin;
import com.example.shipbrowser.dao.StoredImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class OrphanedEntities {
    private List<StoredImage> orphanedImages;
    private List<Skin> orphanedSkins;
    private List<Skill> orphanedSkills;

    public OrphanedEntities() {
        this.orphanedImages = new ArrayList<>();
        this.orphanedSkins = new ArrayList<>();
        this.orphanedSkills = new ArrayList<>();
    }
}
