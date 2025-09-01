package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.StockStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SupplyInfo {

    @NotNull
    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus;

    @Size(min = 0, max = 99999)
    private Integer stockQuantity;

    @Size(min = 1, max = 99)
    private Integer leadTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date restockDate;
}
