package com.mehmetsadullahguven.adaptors.impl.Fruugo;

import com.mehmetsadullahguven.adaptors.IGeneralAdaptor;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.service.FruugoOrderService;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.service.FruugoProductService;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("fruugo")
public class FruugoAdaptorImpl implements IGeneralAdaptor {

    @Autowired
    private FruugoProductService productService;

    @Autowired
    private FruugoOrderService orderService;

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
        if (Objects.equals(webhookData.getType(), "OrderResponseList")) {
            orderService.webhook(webhookData);
        }
        productService.webhook(webhookData);
    }
}
