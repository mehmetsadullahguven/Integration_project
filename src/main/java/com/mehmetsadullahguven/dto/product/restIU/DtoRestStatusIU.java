package com.mehmetsadullahguven.dto.product.restIU;

import com.mehmetsadullahguven.dto.DtoBase;
import com.mehmetsadullahguven.enums.StatusType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoRestStatusIU extends DtoBase {

    @NotBlank(message = "merchantProductId boş olamaz")
    private String merchantProductId;

    @NotBlank(message = "Status alanı boş olamaz")
    private StatusType status;

}
