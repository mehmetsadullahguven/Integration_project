package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DiscountPrice {

    public DiscountPrice(Float price, Boolean vatInclusive) {
        this.vatInclusive = vatInclusive;
        this.price = price;
    }

    @Min(0)
    @NotNull
    private Float price;

    @NotNull
    private Boolean vatInclusive;

    private Date startDate;

    private Date endDate;
}
