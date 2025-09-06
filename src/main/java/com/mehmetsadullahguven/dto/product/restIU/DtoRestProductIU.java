package com.mehmetsadullahguven.dto.product.restIU;

import com.mehmetsadullahguven.dto.DtoBase;
import com.mehmetsadullahguven.enums.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoRestProductIU extends DtoBase {

    @NotBlank(message = "merchantProductId boş olamaz")
    private String merchantProductId;

    @NotBlank(message = "name boş olamaz")
    private String name;

    @NotBlank(message = "description boş olamaz")
    private String description;

    @NotNull(message = "status boş olamaz")
    private StatusType status;

    private String brand;

    private String size;

    private String color;

    private String ean;

    private String merchantParentProductId;

    private Float price;

    private Integer purchasePrice;

    private String vatRateType;

    @NotEmpty(message = "En az 1 resim olmak zorundadır")
    private List<DtoImageIU> images;

    private Integer googleCategoryId;
}
