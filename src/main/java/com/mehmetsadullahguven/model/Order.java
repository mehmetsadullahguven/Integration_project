package com.mehmetsadullahguven.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "order")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity {

    @JsonProperty("client_order_id")
    private String clientOrderId;

    @JsonProperty("r2s_product_list_id")
    private String r2sProductListId;

    @JsonProperty("status")
    private String status;

    @Column(name = "correlation_id")
    private String correlationId;

    @Column(name = "serialized_data", columnDefinition = "TEXT")
    private String serializedData;

}
