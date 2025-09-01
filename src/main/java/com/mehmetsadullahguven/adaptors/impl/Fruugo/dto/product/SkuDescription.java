package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.Language;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SkuDescription {

    @Size(max = 2, min = 2)
    @Enumerated(EnumType.STRING)
    private Language language;

    @Size(max = 1, min = 150)
    private String title;

    @Size(max = 1, min = 5000)
    private String text;

    @Size(min = 1, message = "En az bir attribute olmalıdır")
    private List<Attribute> attributes;
}
