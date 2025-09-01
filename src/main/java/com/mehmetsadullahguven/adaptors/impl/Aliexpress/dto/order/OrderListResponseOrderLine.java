package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListResponseOrderLine {

    private Long id;

    @JsonProperty("item_id")
    private String itemId;

    @JsonProperty("sku_id")
    private String skuId;

    @JsonProperty("sku_code")
    private String skuCode;

    private String name;

    @JsonProperty("img_url")
    private String imgUrl;

    @JsonProperty("item_price")
    private Integer itemPrice;

    private Double quantity;

    @JsonProperty("total_amount")
    private Integer totalAmount;

    private List<String> properties;

    @JsonProperty("buyer_comment")
    private String buyerComment;

    private Double height;
    private Double weight;
    private Double width;
    private Double length;

    @JsonProperty("issue_status")
    private String issueStatus;

    private List<Object> promotions;

    @JsonProperty("order_line_fees")
    private Object orderLineFees;

    @JsonProperty("hs_code")
    private Object hsCode;

    @JsonProperty("logistic_name")
    private String logisticName;

    @JsonProperty("logistic_storage_type")
    private String logisticStorageType;
}
