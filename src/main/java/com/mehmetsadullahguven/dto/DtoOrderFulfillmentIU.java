package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoOrderFulfillmentIU {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("order_form")
    private DtoOrderFormIU orderForm;
}
