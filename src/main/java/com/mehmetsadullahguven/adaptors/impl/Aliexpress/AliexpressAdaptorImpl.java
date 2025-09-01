package com.mehmetsadullahguven.adaptors.impl.Aliexpress;

import com.mehmetsadullahguven.adaptors.IGeneralAdaptor;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.service.AliexpressOrderService;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.service.AliexpressProductService;
import com.mehmetsadullahguven.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("aliexpress")
public class AliexpressAdaptorImpl implements IGeneralAdaptor {

    @Autowired
    private AliexpressProductService productService;

    @Autowired
    private AliexpressOrderService orderService;

    @Override
    public DtoProduct productCreate(DtoProductIU dtoProductIU) {
        return productService.create(dtoProductIU);
    }

    @Override
    public DtoProduct productUpdate(DtoProductIU dtoProductIU) {
        return productService.update(dtoProductIU);
    }

    @Override
    public DtoProduct productDelete(DtoProductIU dtoProductIU) {
        return productService.delete(dtoProductIU);
    }

    @Override
    public DtoOrders orderList(DtoOrderIU dtoOrderIU) {
        return orderService.list(dtoOrderIU);
    }

    @Override
    public DtoOrders orderDetail(DtoOrderIU dtoOrderIU) {
        return orderService.detail(dtoOrderIU);
    }

    @Override
    public void statusUpdate(DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU) {
        orderService.statusUpdate(dtoOrderStatusUpdateIU);
    }

    @Override
    public void fulfillment(DtoOrderFulfillmentIU dtoOrderFulfillmentIU) {
        orderService.fulfillment(dtoOrderFulfillmentIU);
    }

    @Override
    public void webhook(DtoWebhookIU webhookData) {

    }
}
