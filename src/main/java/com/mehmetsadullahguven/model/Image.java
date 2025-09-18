package com.mehmetsadullahguven.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseEntity{

    private String path;

    private String originalPath;

    private String alt;

    private Boolean isMain;

    @ManyToOne
    @JoinColumn(name = "merchant_product_id")
    private Product product;
}
