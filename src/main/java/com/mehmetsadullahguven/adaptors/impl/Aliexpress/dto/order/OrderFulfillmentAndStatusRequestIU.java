package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFulfillmentAndStatusRequestIU {

    private String orderId;

    private String trackingUrl;

    private String trackingCode;
}
