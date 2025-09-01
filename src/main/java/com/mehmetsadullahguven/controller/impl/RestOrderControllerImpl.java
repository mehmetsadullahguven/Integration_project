package com.mehmetsadullahguven.controller.impl;

import com.mehmetsadullahguven.adaptors.IGeneralAdaptor;
import com.mehmetsadullahguven.adaptors.GeneralAdaptorFactory;
import com.mehmetsadullahguven.controller.IRestOrderController;
import com.mehmetsadullahguven.controller.RestBaseController;
import com.mehmetsadullahguven.controller.RootEntity;
import com.mehmetsadullahguven.dto.DtoOrderFulfillmentIU;
import com.mehmetsadullahguven.dto.DtoOrderIU;
import com.mehmetsadullahguven.dto.DtoOrderStatusUpdateIU;
import com.mehmetsadullahguven.dto.DtoOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/order/{slug}")
@RestController
public class RestOrderControllerImpl extends RestBaseController implements IRestOrderController {

    @Autowired
    private GeneralAdaptorFactory generalAdaptorFactory;

    @GetMapping("/list")
    @Override
    public RootEntity<DtoOrders> orderList(@RequestBody DtoOrderIU dtoOrderIU, @PathVariable String slug) {
        IGeneralAdaptor generalAdaptor = generalAdaptorFactory.getAdaptor(slug);
        return ok(generalAdaptor.orderList(dtoOrderIU), "Order List");
    }

    @GetMapping("/detail")
    @Override
    public RootEntity<DtoOrders> orderDetail(@RequestBody DtoOrderIU dtoOrderIU, @PathVariable String slug) {
        IGeneralAdaptor generalAdaptor = generalAdaptorFactory.getAdaptor(slug);
        return ok(generalAdaptor.orderDetail(dtoOrderIU), "Order Detail");
    }

    @PostMapping("/fulfillment")
    @Override
    public void fulfillment(@RequestBody DtoOrderFulfillmentIU dtoOrderFulfillmentIU, @PathVariable String slug)
    {
        IGeneralAdaptor generalAdaptor = generalAdaptorFactory.getAdaptor(slug);
        generalAdaptor.fulfillment(dtoOrderFulfillmentIU);
    }

    @PostMapping("/orderStatusUpdate")
    @Override
    public void statusUpdate(@RequestBody DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU, @PathVariable String slug)
    {
        IGeneralAdaptor generalAdaptor = generalAdaptorFactory.getAdaptor(slug);
        generalAdaptor.statusUpdate(dtoOrderStatusUpdateIU);
    }
}
