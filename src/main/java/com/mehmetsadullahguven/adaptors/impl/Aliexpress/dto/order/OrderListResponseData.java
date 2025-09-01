package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListResponseData {

    @JsonProperty("total_count")
    private Integer totalCount;

    private List<OrderListResponseOrder> orders;
}
