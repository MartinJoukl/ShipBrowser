package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.model.HullType;
import com.example.shipbrowser.model.Rarity;
import com.example.shipbrowser.model.validation.MapValidation;
import com.example.shipbrowser.repository.ShipsSearchCriteria;
import com.example.shipbrowser.repository.SkinSearchCriteria;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ListSkinDtoIn extends PageableDtoIn {
    @Valid
    private ListSkinCriteriaDtoIn searchCriteria;
    @MapValidation(allowedKeys = {"name", "ship.name", "ship.code", "ship.shipClass", "ship.nationality"}, valueRestriction = {"asc", "desc", "ASC", "DESC"},
            message = "Sorting value must contain only keys of one of the following: [name ,ship.name, ship.code, ship.shipClass, ship.nationality]. \n Key value must be one of the: [asc, desc, ASC, DESC]")
    private LinkedHashMap<String, String> sortCriteria;
}
