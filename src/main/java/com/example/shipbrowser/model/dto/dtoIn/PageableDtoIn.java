package com.example.shipbrowser.model.dto.dtoIn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PageableDtoIn extends DtoIn {
    private PageInfoDtoIn pageInfo;
}
