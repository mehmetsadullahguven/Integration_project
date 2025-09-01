package com.mehmetsadullahguven.dto;

import com.mehmetsadullahguven.enums.LanguageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCategoryTranslationIU {

    private String title;

    @Enumerated(EnumType.STRING)
    private LanguageType language;

    private String slug;

    private String source;
}
