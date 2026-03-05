package com.mehmetsadullahguven.dto.product.front;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoFrontImage {

    private String path;

    private String originalPath;

    private String alt;

    private Boolean isMain;

}
