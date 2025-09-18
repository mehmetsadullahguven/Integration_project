package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mehmetsadullahguven.enums.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DtoOrder {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("r2s_product_list_id")
    private String r2sProductListId;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("shipping_type")
    private String shippingType;

    @JsonProperty("order_date")
    private String orderDate;

    @JsonProperty("tax_authority")
    private String taxAuthority;

    @JsonProperty("tax_number")
    private String taxNumber;

    private DtoOrderUser user;

    @JsonProperty("shipping_address")
    private DtoOrderAddress shippingAddress;

    @JsonProperty("billing_address")
    private DtoOrderAddress billingAddress;

    @JsonProperty("sub_total")
    private Integer subTotal;

    @JsonProperty("shipping_price")
    private Float shippingPrice;

    private Integer total;

    @JsonProperty("coupon_total")
    private String couponTotal;

    @JsonProperty("coupon_codes")
    private String couponCodes;

    @JsonProperty("total_discounts")
    private Integer totalDiscounts;

    private List<DtoOrderProduct> products;

    private CurrencyType currency;
}
