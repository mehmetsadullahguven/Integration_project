package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkuAttributesList {

    @JsonProperty("sku_attribute_name_id")
    private String skuAttributeNameId;

    @JsonProperty("sku_attribute_value_id")
    private String skuAttributeValueId;

    @JsonProperty("sku_attribute_value_definition_name")
    private String skuAttributeValueDefinitionName;

    @JsonProperty("sku_image_url")
    private String skuImageUrl;

}
