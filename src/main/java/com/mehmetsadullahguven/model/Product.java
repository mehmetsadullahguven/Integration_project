package com.mehmetsadullahguven.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mehmetsadullahguven.enums.CurrencyType;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.enums.StatusType;
import com.mehmetsadullahguven.enums.VatRateType;
import jakarta.persistence.*;
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

    private String brand;

    private String ean;

    private Integer stock;

    private Float price;

    private CurrencyType currency;

    private Integer purchasePrice;

    private VatRateType vatRateType;

    private Integer googleCategoryId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Translation> translations = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options = new ArrayList<>();

}
