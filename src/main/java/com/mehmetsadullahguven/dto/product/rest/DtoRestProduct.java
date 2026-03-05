package com.mehmetsadullahguven.dto.product.rest;

import com.mehmetsadullahguven.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoRestProduct {

    private Long id;

    private String merchantProductId;

    private String image;

    private StatusType Status;

    private Integer stock;

    private Float price;

    private String action;

    private String error;
}
