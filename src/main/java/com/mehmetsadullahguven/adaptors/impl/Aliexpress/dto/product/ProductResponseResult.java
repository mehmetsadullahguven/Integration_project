package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseResult {

    private Boolean ok;

    @JsonProperty("task_id")
    private String taskId;

    private Object errors;

    @JsonProperty("external_id")
    private String externalId;
}
