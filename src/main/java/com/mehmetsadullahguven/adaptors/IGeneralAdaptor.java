package com.mehmetsadullahguven.adaptors;


import com.mehmetsadullahguven.dto.*;

import java.util.List;

public interface IGeneralAdaptor {

    public DtoProduct productCreate(DtoProductIU dtoProductIU);

    public DtoProduct productUpdate(DtoProductIU dtoProductIU);

    public DtoProduct productDelete(DtoProductIU dtoProductIU);

    public DtoOrders orderList(DtoOrderIU dtoOrderIU);

    public DtoOrders orderDetail(DtoOrderIU dtoOrderIU);

    public void statusUpdate(DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU);

    public void fulfillment(DtoOrderFulfillmentIU dtoOrderFulfillmentIU);

    public void webhook(DtoWebhookIU webhookData);

}
