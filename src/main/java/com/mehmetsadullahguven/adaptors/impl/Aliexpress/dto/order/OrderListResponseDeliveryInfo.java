package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListResponseDeliveryInfo {

    private String country;
    private String region;
    private String city;

    @JsonProperty("street_house")
    private String streetHouse;

    private String flat;
    private String index;
    private OrderListResponseReceiver receiver;

}
