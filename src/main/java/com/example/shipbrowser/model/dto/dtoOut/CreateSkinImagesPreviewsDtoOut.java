package com.example.shipbrowser.model.dto.dtoOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class CreateSkinImagesPreviewsDtoOut extends DtoOut{
    private String message;
}
