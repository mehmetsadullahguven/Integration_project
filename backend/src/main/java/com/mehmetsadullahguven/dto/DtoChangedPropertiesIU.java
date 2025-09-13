package com.mehmetsadullahguven.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DtoChangedPropertiesIU {

    private Integer quantity;

    private Map<String, DtoChangedCombineIU> combines;
}
