package com.example.shipbrowser.model.dto.dtoIn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateSkinImagePreviewsDtoIn extends DtoIn {
    private Boolean regenerate;
}
