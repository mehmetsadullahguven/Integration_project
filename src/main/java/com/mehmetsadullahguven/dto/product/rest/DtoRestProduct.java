package com.mehmetsadullahguven.dto.product.rest;

import com.mehmetsadullahguven.enums.StatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoRestProduct {

    private String merchantProductId;

    private StatusType Status;


}
