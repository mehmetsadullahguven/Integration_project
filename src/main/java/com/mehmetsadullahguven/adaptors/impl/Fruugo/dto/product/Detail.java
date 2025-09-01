package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Detail {

    @NotEmpty
    private List<SkuDescription> skuDescriptions;

    @Size(max = 5, message = "Media listesi en fazla 5 öğe içerebilir")
    private List<Media> media;
}
