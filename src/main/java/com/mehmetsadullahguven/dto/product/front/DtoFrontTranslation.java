package com.mehmetsadullahguven.dto.product.front;

import com.mehmetsadullahguven.enums.LanguageType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoFrontTranslation {

    private LanguageType language;

    private String name;

    private String description;


}
