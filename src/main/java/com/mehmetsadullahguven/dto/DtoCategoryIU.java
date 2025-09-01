package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoCategoryIU {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("googleId")
    private Integer google_id;

    @JsonProperty("uniqueId")
    private Integer unique_id;

    private List<DtoCategoryTranslationIU> translations;


}
