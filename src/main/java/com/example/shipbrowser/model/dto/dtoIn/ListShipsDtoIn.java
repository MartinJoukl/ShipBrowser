package com.example.shipbrowser.model.dto.dtoIn;

import com.example.shipbrowser.model.validation.MapValidation;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ListShipsDtoIn extends PageableDtoIn {
    @Valid
    private ListShipsSearchCriteriaDtoIn searchCriteria;
    @MapValidation(allowedKeys = {"name", "code", "shipClass", "nationality"}, valueRestriction = {"asc", "desc", "ASC", "DESC"},
            message = "Sorting value must contain only keys of one of the following: [name, code, shipClass, nationality]. \n Key value must be one of the: [asc, desc, ASC, DESC]")
    private LinkedHashMap<String, String> sortCriteria;
}
