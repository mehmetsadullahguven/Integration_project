package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.MediaType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Media {

    @Max(value = 50, message = "description en fazla 50 karakter olmalıdır")
    private String description;

    @Min(value = 150, message = "url en fazla 150 karakter olmalıdır")
    private String url;

    private MediaType type;
}
