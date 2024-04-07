package com.example.shipbrowser.model.dto.dtoIn;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ListShipsDtoIn extends PageableDtoIn {
    @Valid
    private ListShipsSearchCriteriaDtoIn searchCriteria;
}
