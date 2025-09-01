package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.CodeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Gtin {

    @NotNull
    @Enumerated(EnumType.STRING)
    private CodeType codeType;

    private String code;
}
