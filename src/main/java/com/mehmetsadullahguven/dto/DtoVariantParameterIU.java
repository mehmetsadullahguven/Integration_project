package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoVariantParameterIU {

    @JsonProperty("_id")
    private String id;

    private List<DtoVariantParameterTranslationIU> translations;
}
