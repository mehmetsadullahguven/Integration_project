package com.mehmetsadullahguven.dto;

import com.mehmetsadullahguven.dto.product.restIU.DtoImageIU;
import com.mehmetsadullahguven.enums.LanguageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoTranslationIU {

    @Enumerated(EnumType.STRING)
    private LanguageType language;

    private String title;

    private String description;

    private List<DtoImageIU> images;
}
