package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListResponse {

    private OrderListResponseData data;
    private Object error;

}
