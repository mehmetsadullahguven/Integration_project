package com.mehmetsadullahguven.dto;

import com.mehmetsadullahguven.enums.LanguageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoVariantParameterTranslationIU {

    private String title;

    private String slug;

    @Enumerated(EnumType.STRING)
    private LanguageType language;
}
