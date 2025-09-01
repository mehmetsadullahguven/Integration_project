package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NormalPrice {

    @Min(0)
    @NotNull
    private Float price;

    @NotNull
    private Boolean vatInclusive;
}
