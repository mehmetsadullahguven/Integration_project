package com.mehmetsadullahguven.dto;

import com.mehmetsadullahguven.enums.LanguageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DtoTranslationIU {

    @Enumerated(EnumType.STRING)
    private LanguageType language;

    private String title;

    private String description;

    private List<DtoImageIU> images;
}
