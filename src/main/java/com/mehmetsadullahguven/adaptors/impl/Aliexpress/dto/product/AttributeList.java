package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttributeList {

    @JsonProperty("attribute_name")
    private String attributeName;

    @JsonProperty("attribute_name_id")
    private String attributeNameId;

    @JsonProperty("attribute_value")
    private String attributeValue;

    @JsonProperty("attribute_value_id")
    private String attributeValueId;
}
