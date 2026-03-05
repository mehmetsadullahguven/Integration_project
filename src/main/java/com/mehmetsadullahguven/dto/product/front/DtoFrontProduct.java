package com.mehmetsadullahguven.dto.product.front;

import com.mehmetsadullahguven.dto.product.restIU.DtoOptionIU;
import com.mehmetsadullahguven.enums.CurrencyType;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.enums.StatusType;
import com.mehmetsadullahguven.enums.VatRateType;
import com.mehmetsadullahguven.model.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DtoFrontProduct {

    private Long id;

    private String merchantProductId;

    private StatusType Status;

    private Integer stock;

    private Float price;

    private String merchantParentProductId;

    private String brand;

    private String ean;

    private CurrencyType currency;

    private Integer purchasePrice;

    private VatRateType vatRateType;

    private String imagePath;

    private Integer googleCategoryId;

    private List<DtoFrontTranslation> translations;

    private List<DtoFrontOption> options;
}
