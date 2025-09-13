package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.IRestProductController;
import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.*;
import com.mehmetsadullahguven.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class RestProductControllerImpl extends RestBaseController implements IRestProductController {

    @Autowired
    private IProductService productService;

    @PostMapping("/")
    @Override
    public RootEntity<DtoRestProduct> productCreateAndUpdate(@Valid @RequestBody DtoRestProductIU dtoRestProductIU)
    {
        return ok(productService.createAndUpdate(dtoRestProductIU), "Product Created or Updated");
    }

    @PatchMapping("/")
    @Override
    public RootEntity<DtoRestProduct> productPartiallyUpdate(@RequestBody DtoRestPartiallyProductIU dtoRestPartiallyProductIU) throws Exception
    {
        return ok(productService.partiallyUpdate(dtoRestPartiallyProductIU), "Product Updated");
    }

    @DeleteMapping("/delete/{merchantProductNo}")
    @Override
    public RootEntity<DtoRestProduct> productDelete(@PathVariable String merchantProductNo)
    {
        return ok(productService.delete(merchantProductNo), "Product Deleted");
    }

    @PutMapping("/offer/price")
    @Override
    public RootEntity<DtoRestProduct> productStockAndPriceUpdate(@Valid @RequestBody DtoRestStockAndPriceIU dtoRestStockAndPriceIU)
    {
        return ok(productService.stockAndPriceUpdate(dtoRestStockAndPriceIU), "Product Price Updated");
    }

    @PutMapping("/offer/stock")
    @Override
    public RootEntity<DtoRestProduct> productStockUpdate(@Valid @RequestBody DtoRestStockIU dtoRestStockIU)
    {
        return ok(productService.stockUpdate(dtoRestStockIU), "Product Stock Updated");
    }

    @PutMapping("/offer/status")
    @Override
    public RootEntity<DtoRestProduct> productStatusUpdate(DtoRestStatusIU dtoRestStatusIU) {
        return ok(productService.statusUpdate(dtoRestStatusIU), "Product Stock And Price Updated");
    }

    @GetMapping("/")
    @Override
    public RootEntity<List<DtoRestProduct>> getAllProducts()
    {
        return ok(productService.getAllProducts(), "Get Product List");
    }

    @GetMapping("/{merchantProductId}")
    @Override
    public RootEntity<DtoRestProduct> getProduct(@PathVariable("merchantProductId") String merchantProductId)
    {
        return ok(productService.getProduct(merchantProductId), "Get Product");
    }
}
