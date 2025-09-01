package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SkuInfoList {

    @JsonProperty("sku_code")
    private String skuCode;

    @JsonProperty("price")
    private String price;

    @JsonProperty("discount_price")
    private String discountPrice;

    @JsonProperty("inventory")
    private Integer inventory;

    @JsonProperty("sku_attributes_list")
    private List<SkuAttributesList> skuAttributesList;

}
