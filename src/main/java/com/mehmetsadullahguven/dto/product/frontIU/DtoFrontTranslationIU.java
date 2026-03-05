package com.mehmetsadullahguven.dto.product.frontIU;

import com.mehmetsadullahguven.enums.LanguageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoFrontTranslationIU {

    private LanguageType language;

    private String name;

    private String description;


}
