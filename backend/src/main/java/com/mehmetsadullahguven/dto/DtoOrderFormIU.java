package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoOrderFormIU {

    @JsonProperty("integration_type")
    private String integrationType;

    private DtoOrderBarcodeIU barcodes;
}
