package com.mehmetsadullahguven.controller.rest.impl;

import com.mehmetsadullahguven.controller.rest.IRestProductController;
import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.controller.rest.RootEntity;
import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;
import com.mehmetsadullahguven.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class RestProductControllerImpl extends RestBaseController implements IRestProductController {

    @Autowired
    private IProductService productService;

    @PostMapping()
    @Override
    public RootEntity<DtoRestProduct> productCreateAndUpdate(@Valid @RequestBody DtoRestProductIU dtoRestProductIU) {
        return ok(productService.createAndUpdate(dtoRestProductIU), "Product Created or Updated");
    }

    @PatchMapping()
    @Override
    public RootEntity<DtoRestProduct> productPartiallyUpdate(@RequestBody DtoRestProductIU dtoRestProductIU) throws Exception {
        return ok(productService.partiallyUpdate(dtoRestProductIU), "Product Updated");
    }

    @DeleteMapping("/delete/{merchantProductNo}")
    @Override
    public RootEntity<DtoRestProduct> productDelete(@PathVariable String merchantProductNo) {
        return ok(productService.delete(merchantProductNo), "Product Deleted");
    }

    @PutMapping("/offer/price")
    @Override
    public RootEntity<DtoRestProduct> productPriceUpdate(@Valid @RequestBody DtoRestProductIU dtoProductPriceIU) {
        return ok(productService.priceUpdate(dtoProductPriceIU), "Product Price Updated");
    }

    @PutMapping("/offer/stock")
    @Override
    public RootEntity<DtoRestProduct> productStockUpdate(@Valid @RequestBody DtoRestProductIU dtoProductStockIU) {
        return ok(productService.stockUpdate(dtoProductStockIU), "Product Price Updated");
    }
}
