package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductStatusResponse {
    private List<ProductStatusDataResponse> data;
}
