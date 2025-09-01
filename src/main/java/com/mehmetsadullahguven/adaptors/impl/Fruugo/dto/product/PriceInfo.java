package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.Country;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.Currency;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceInfo {

    private Float vatRate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Country country;

    private NormalPrice normalPrice;

    private DiscountPrice discountPrice;


}
