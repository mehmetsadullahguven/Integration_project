package com.mehmetsadullahguven.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DtoBase {

    private Long id;
    private Date createdAt;
}
