package com.mehmetsadullahguven.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoIntegration {

    private String title;

    @Column(name = "r2s_product_list_id")
    private String r2sProductListId;
}
