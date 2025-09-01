package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoCombineIU {

    @JsonProperty("combine_id")
    private String combineId;

    private Integer quantity;

    @JsonProperty("price_diff")
    private Float priceDiff;

    private List<DtoVariantIU> variant;
}
