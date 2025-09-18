package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoVariantIU {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("price_diff")
    private Float priceDiff;

    private DtoVariantParameterIU parameter;

    private List<DtoVariantTranslationIU> translations;

}
