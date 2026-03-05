package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.mehmetsadullahguven.service.rest.impl.RestProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class RestProductController extends RestBaseController  {

    @Autowired
    private RestProductService restProductService;

    @PostMapping("/")
    public RootEntity<DtoRestProduct> productCreateAndUpdate(@Valid @RequestBody DtoRestProductIU dtoRestProductIU)
    {
        return ok(restProductService.createAndUpdate(dtoRestProductIU), "Product Created or Updated");
    }

    @PatchMapping("/")
    public RootEntity<DtoRestProduct> productPartiallyUpdate(@RequestBody DtoRestPartiallyProductIU dtoRestPartiallyProductIU)
    {
        return ok(restProductService.partiallyUpdate(dtoRestPartiallyProductIU), "Product Updated");
    }

    @DeleteMapping("/delete/{merchantProductNo}")
    public RootEntity<DtoRestProduct> productDelete(@PathVariable String merchantProductNo)
    {
        return ok(restProductService.delete(merchantProductNo), "Product Deleted");
    }

    @PutMapping("/offer/price")
    public RootEntity<DtoRestProduct> productStockAndPriceUpdate(@Valid @RequestBody DtoRestStockAndPriceIU dtoRestStockAndPriceIU)
    {
        return ok(restProductService.stockAndPriceUpdate(dtoRestStockAndPriceIU), "Product Price Updated");
    }

    @PutMapping("/offer/stock")
    public RootEntity<DtoRestProduct> productStockUpdate(@Valid @RequestBody DtoRestStockIU dtoRestStockIU)
    {
        return ok(restProductService.stockUpdate(dtoRestStockIU), "Product Stock Updated");
    }

    @PutMapping("/offer/status")
    public RootEntity<DtoRestProduct> productStatusUpdate(DtoRestStatusIU dtoRestStatusIU) {
        return ok(restProductService.statusUpdate(dtoRestStatusIU), "Product Stock And Price Updated");
    }


    @GetMapping("/{merchantProductId}")
    public RootEntity<DtoRestProduct> getProduct(@PathVariable("merchantProductId") String merchantProductId)
    {
        return ok(restProductService.getProduct(merchantProductId), "Get Product");
    }


    // Bulk Methods

    @PostMapping("/bulk")
    public RootEntity<List<DtoRestProduct>> bulkProductCreateAndUpdate(@Valid @RequestBody List<DtoRestProductIU> dtoRestProductIUList)
    {
        return ok(restProductService.bulkProductCreateAndUpdate(dtoRestProductIUList), "Product List Create Or Update Success");
    }

    @GetMapping("/bulk")
    public RootEntity<List<DtoRestProduct>> getAllProducts()
    {
        return ok(restProductService.getAllProducts(), "Get Product List");
    }
}
