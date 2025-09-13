package com.mehmetsadullahguven.dto.product.restIU;

import com.mehmetsadullahguven.dto.DtoBase;
import com.mehmetsadullahguven.enums.CurrencyType;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.enums.StatusType;
import com.mehmetsadullahguven.enums.VatRateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoRestPartiallyProductIU extends DtoBase {

    @NotBlank(message = "merchantProductId boş olamaz")
    private String merchantProductId;

    private String name;

    private String description;

    private StatusType status;

    private String brand;

    private String size;

    private String color;

    private String ean;

    private Integer stock;

    private Float price;

    private Integer purchasePrice;

    private VatRateType vatRateType;

    private CurrencyType currency;

    private LanguageType language;

    private List<DtoImageIU> images;

    private Integer googleCategoryId;
}
