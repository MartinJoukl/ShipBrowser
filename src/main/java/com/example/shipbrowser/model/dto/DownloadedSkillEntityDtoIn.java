package com.example.shipbrowser.model.dto;

import lombok.Data;
@Data
public class DownloadedSkillEntityDtoIn {
    private String icon;
    private DtoInNames names;
    private String description;
    private String color;

    @Data
    private static class DtoInNames {
        private String en;
    }
}
