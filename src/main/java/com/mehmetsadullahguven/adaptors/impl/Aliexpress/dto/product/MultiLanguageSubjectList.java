package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.mehmetsadullahguven.adaptors.impl.Aliexpress.enums.Language;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiLanguageSubjectList {

    private Language language;

    private String subject;
}
