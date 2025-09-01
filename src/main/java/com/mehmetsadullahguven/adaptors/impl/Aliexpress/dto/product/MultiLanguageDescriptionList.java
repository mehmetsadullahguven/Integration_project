package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.mehmetsadullahguven.adaptors.impl.Aliexpress.enums.Language;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiLanguageDescriptionList {

    private Language language;

    private String mobile;

    private String web;
}
