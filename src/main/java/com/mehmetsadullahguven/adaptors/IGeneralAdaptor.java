package com.mehmetsadullahguven.adaptors;


import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;

public interface IGeneralAdaptor {

    public DtoProduct productCreate(DtoRestProductIU dtoRestProductIU);

    public DtoProduct productUpdate(DtoRestProductIU dtoRestProductIU);

    public DtoProduct productPartiallyUpdate(DtoRestProductIU dtoRestProductIU);

    public DtoProduct productDelete(DtoRestProductIU dtoRestProductIU);

    public DtoOrders orderList(DtoOrderIU dtoOrderIU);

    public DtoOrders orderDetail(DtoOrderIU dtoOrderIU);

    public void statusUpdate(DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU);

    public void fulfillment(DtoOrderFulfillmentIU dtoOrderFulfillmentIU);

    public void webhook(DtoWebhookIU webhookData);

}
