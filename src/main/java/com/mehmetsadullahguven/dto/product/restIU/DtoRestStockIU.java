package com.mehmetsadullahguven.dto.product.restIU;

import com.mehmetsadullahguven.dto.DtoBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoRestStockIU extends DtoBase {

    @NotBlank(message = "merchantProductId boş olamaz")
    private String merchantProductId;

    @NotBlank(message = "Stok alanı boş olamaz")
    private Integer stock;

}
