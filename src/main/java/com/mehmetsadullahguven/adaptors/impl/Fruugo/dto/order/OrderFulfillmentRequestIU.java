package com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFulfillmentRequestIU {

    private String orderId;

    private String shipmentId;

    private String trackingUrl;

    private String trackingCode;
}
