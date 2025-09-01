package com.mehmetsadullahguven.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoIntegrationIU extends DtoBase{

    private String title;

    private String slug;

    private String baseUrl;

    private String accessToken;

    @Column(name = "r2s_product_list_id")
    private String r2sProductListId;
}
