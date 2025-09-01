package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mehmetsadullahguven.enums.LanguageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoImageIU {

    private LanguageType language;

    private String path;

    @JsonProperty("original_path")
    private String originalPath;

    @JsonProperty("original_checksum")
    private String originalChecksum;
}
