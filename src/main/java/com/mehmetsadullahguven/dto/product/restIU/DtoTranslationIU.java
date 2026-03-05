package com.mehmetsadullahguven.dto.product.restIU;

import com.mehmetsadullahguven.enums.LanguageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoTranslationIU {

    private LanguageType language;

    private String name;

    private String description;


}
