package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoOrderProduct {

    @JsonProperty("r2s_product_id")
    private String r2sProductId;

    @JsonProperty("seller_product_id")
    private String sellerProductId;

    private Integer quantity;

    @JsonProperty("unit_price")
    private Float unitPrice;

    @JsonProperty("total_price")
    private Float totalPrice;

    @JsonProperty("sell_unit_price")
    private Float sellUnitPrice;

    @JsonProperty("sell_total_price")
    private Float sellTotalPrice;

    @JsonProperty("combine_id")
    private String combineId;

}
