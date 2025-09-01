package com.mehmetsadullahguven.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity{

    @Column(name = "r2s_product_id")
    private String r2sProductId;

    @Column(name = "r2s_product_list_id")
    private String r2sProductListId;

    @Column(name = "client_product_id")
    private String clientProductId;

    @Column(name = "status")
    private String status;

    @Column(name = "serialized_data", columnDefinition = "TEXT")
    private String serializedData;

    @Column(name = "correlation_id")
    private String correlationId;
}
