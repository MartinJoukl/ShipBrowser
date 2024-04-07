package com.example.shipbrowser.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum Rarity {
    NORMAL("Normal"),
    RARE("Rare"),
    ELITE("Elite"),
    PRIORITY("Priority"),
    SUPER_RARE("Super Rare"),
    DECISIVE("Decisive"),
    ULTRA_RARE("Ultra Rare");

    private final String name;

    Rarity(String name) {
        this.name = name;
    }

    @JsonCreator
    public static Rarity fromString(String rarity) {
        for (Rarity rarityValue : Rarity.values()) {
            if (rarityValue.name.equals(rarity)) {
                return rarityValue;
            }
        }

        throw new RuntimeException("Invalid rarity");
    }
}
