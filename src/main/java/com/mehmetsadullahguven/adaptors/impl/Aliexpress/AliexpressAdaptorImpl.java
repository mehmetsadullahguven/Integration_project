package com.mehmetsadullahguven.adaptors.impl.Aliexpress;

import com.mehmetsadullahguven.adaptors.IGeneralAdaptor;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.service.AliexpressOrderService;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.service.AliexpressProductService;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("aliexpress")
public class AliexpressAdaptorImpl implements IGeneralAdaptor {

    @Autowired
    private AliexpressProductService productService;

    @Autowired
    private AliexpressOrderService orderService;

    @Override
    public DtoProduct productCreate(DtoRestProductIU dtoRestProductIU) {
        return productService.create(dtoRestProductIU);
    }

    @Override
    public DtoProduct productUpdate(DtoRestProductIU dtoRestProductIU) {
        return productService.update(dtoRestProductIU);
    }

    @Override
    public DtoProduct productPartiallyUpdate(DtoRestProductIU dtoRestProductIU) {
        return null;
    }

    @Override
    public DtoProduct productDelete(DtoRestProductIU dtoRestProductIU) {
        return productService.delete(dtoRestProductIU);
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
