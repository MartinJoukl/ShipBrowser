package com.example.shipbrowser.model.dto.dtoIn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfoDtoIn {
    private int pageIndex;
    private int pageSize;
}
