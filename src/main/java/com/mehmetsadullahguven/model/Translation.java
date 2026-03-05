package com.mehmetsadullahguven.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mehmetsadullahguven.enums.LanguageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "translation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Translation extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private LanguageType language;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonBackReference
    private Product product;
}
