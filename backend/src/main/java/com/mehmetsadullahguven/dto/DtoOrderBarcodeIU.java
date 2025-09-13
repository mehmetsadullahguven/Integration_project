package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoOrderBarcodeIU {

    private String code;

    @JsonProperty("cargo_tracking_url")
    private String cargoTrackingUrl;
}
