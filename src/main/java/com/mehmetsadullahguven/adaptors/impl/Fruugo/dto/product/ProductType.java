package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductType {

    @NotEmpty
    private String productId;

    @NotEmpty
    private String brand;

    @NotEmpty
    private String manufacturer;

    @NotEmpty
    private String category;
}
