package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListResponseOrder {

    private Long id;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("paid_at")
    private String paidAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    private String status;

    @JsonProperty("payment_status")
    private String paymentStatus;

    @JsonProperty("delivery_status")
    private String deliveryStatus;

    @JsonProperty("delivery_address")
    private String deliveryAddress;

    @JsonProperty("delivery_info")
    private OrderListResponseDeliveryInfo deliveryInfo;

    @JsonProperty("antifraud_status")
    private String antifraudStatus;

    @JsonProperty("buyer_country_code")
    private String buyerCountryCode;

    @JsonProperty("buyer_name")
    private String buyerName;

    @JsonProperty("order_display_status")
    private String orderDisplayStatus;

    @JsonProperty("buyer_phone")
    private String buyerPhone;

    @JsonProperty("order_lines")
    private List<OrderListResponseOrderLine> orderLines;

    @JsonProperty("total_amount")
    private Integer totalAmount;

}
