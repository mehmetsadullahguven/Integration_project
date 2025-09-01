package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductObject {

    private ProductType product;

    private List<Sku> skus;
}
