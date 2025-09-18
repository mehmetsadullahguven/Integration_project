package com.mehmetsadullahguven.model;


import com.mehmetsadullahguven.enums.CurrencyType;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.enums.StatusType;
import com.mehmetsadullahguven.enums.VatRateType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity{

    @Column(name = "merchant_product_id")
    private String merchantProductId;

    @Column(name = "merchant_parent_productId")
    private String merchantParentProductId;

    private StatusType status;

    private String name;

    private String description;

    private String brand;

    private String size;

    private String color;

    private String ean;

    private Integer stock;

    private Float price;

    private CurrencyType currency;

    private LanguageType language;

    private Integer purchasePrice;

    private VatRateType vatRateType;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    private Integer googleCategoryId;
}
