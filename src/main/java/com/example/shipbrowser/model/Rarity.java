package com.example.shipbrowser.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Rarity {
    NORMAL("Normal"),
    RARE("Rare"),
    ELITE("Elite"),
    PRIORITY("Priority"),
    SUPER_RARE("Super Rare"),
    DECISIVE("Decisive"),
    ULTRA_RARE("Ultra Rare");

    private String name;

    Rarity(String name) {
        this.name = name;
    }

    @JsonCreator
    public static Rarity forValues(String rarity) {
        for (Rarity rarityValue : Rarity.values()) {
            if (rarityValue.name.equals(rarity)) {
                return rarityValue;
            }
        }

        return null;
    }
}
