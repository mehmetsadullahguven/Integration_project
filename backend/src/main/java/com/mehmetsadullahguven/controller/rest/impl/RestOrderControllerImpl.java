package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.IRestOrderController;
import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.dto.DtoOrderFulfillmentIU;
import com.mehmetsadullahguven.dto.DtoOrderIU;
import com.mehmetsadullahguven.dto.DtoOrderStatusUpdateIU;
import com.mehmetsadullahguven.dto.DtoOrders;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/order/{slug}")
@RestController
public class RestOrderControllerImpl extends RestBaseController implements IRestOrderController {


    @GetMapping("/list")
    @Override
    public RootEntity<DtoOrders> orderList(@RequestBody DtoOrderIU dtoOrderIU, @PathVariable String slug) {
        return ok(null, "Order List");
    }

    @GetMapping("/detail")
    @Override
    public RootEntity<DtoOrders> orderDetail(@RequestBody DtoOrderIU dtoOrderIU, @PathVariable String slug) {
        return ok(null, "Order Detail");
    }

    @PostMapping("/fulfillment")
    @Override
    public void fulfillment(@RequestBody DtoOrderFulfillmentIU dtoOrderFulfillmentIU, @PathVariable String slug)
    {
    }

    @PostMapping("/orderStatusUpdate")
    @Override
    public void statusUpdate(@RequestBody DtoOrderStatusUpdateIU dtoOrderStatusUpdateIU, @PathVariable String slug)
    {
    }
}
