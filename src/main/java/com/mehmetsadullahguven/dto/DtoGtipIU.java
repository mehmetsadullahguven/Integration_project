package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoGtipIU {

    @JsonProperty("code_dotted")
    private String codeDotted;
}
