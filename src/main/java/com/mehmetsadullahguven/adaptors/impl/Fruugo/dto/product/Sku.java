package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Sku {

    @NotNull
    private String skuId;

    @NotEmpty
    private List<Gtin> gtins;

    @NotNull
    private Detail details;

    @NotNull
    private SupplyInfo supplyInfo;

    @NotEmpty
    private List<PriceInfo> pricingInfo;

    private Integer packageWeight;

    private Integer volume;
}
