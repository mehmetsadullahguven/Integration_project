package com.mehmetsadullahguven.dto;

import com.mehmetsadullahguven.enums.LanguageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoBaseTranslation {

    @Enumerated(EnumType.STRING)
    private LanguageType language;

    private String title;

    private String slug;

    private String source;

    private String description;

    private List<DtoImageIU> images;

}
