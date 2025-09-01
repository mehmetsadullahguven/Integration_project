package com.mehmetsadullahguven.controller;

import com.mehmetsadullahguven.dto.DtoOrderFulfillmentIU;
import com.mehmetsadullahguven.dto.DtoOrderIU;
import com.mehmetsadullahguven.dto.DtoOrderStatusUpdateIU;
import com.mehmetsadullahguven.dto.DtoOrders;


public interface IRestOrderController {

    public RootEntity<DtoOrders> orderList(DtoOrderIU dtoOrderIU, String slug);

    public RootEntity<DtoOrders> orderDetail(DtoOrderIU dtoOrderIU, String slug);

    public void fulfillment(DtoOrderFulfillmentIU dtoOrderFulfillmentIU, String slug);

    public void statusUpdate(DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU, String slug);
}
