package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductStatusDataResponse {

    private String id;
    private String group_id;
    private String action;
    private String status;
    private String product_id;
    private List<String> sku_codes;
    private ProductStatusDataErrorResponse error;
    private String created_at;
    private String updated_at;
    private String external_id;
}
