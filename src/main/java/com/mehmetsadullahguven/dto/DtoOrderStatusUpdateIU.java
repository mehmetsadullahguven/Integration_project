package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoOrderStatusUpdateIU {

    @JsonProperty("status")
    private String status;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("r2s_product_list_id")
    private String r2sProductListId;

    @JsonProperty("base_url")
    private String baseUrl;

    @JsonProperty("access_token")
    private String accessToken;
}
