package com.example.shipbrowser.model.dto.dtoIn;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdDtoIn {
    @NotNull
    Long Id;
}
