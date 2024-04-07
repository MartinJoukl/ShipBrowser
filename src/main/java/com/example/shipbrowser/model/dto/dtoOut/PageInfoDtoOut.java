package com.example.shipbrowser.model.dto.dtoOut;

import lombok.Data;

@Data
public class PageInfoDtoOut extends DtoOut{
    private int pageIndex;
    private int pageSize;
    private long total;
}
