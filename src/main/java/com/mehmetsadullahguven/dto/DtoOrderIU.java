package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoOrderIU{

    @JsonProperty("id")
    private String id;

    @JsonProperty("r2s_product_list_id")
    private String r2sProductListId;

    @JsonProperty("base_url")
    private String baseUrl;

    @JsonProperty("access_token")
    private String accessToken;

}
