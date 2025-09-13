package com.mehmetsadullahguven.controller.rest;

import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.*;

import java.util.List;
import java.util.Map;

public interface IRestProductController {

    public RootEntity<DtoRestProduct> productCreateAndUpdate(DtoRestProductIU dtoRestProductIU);

    public <T> RootEntity<DtoRestProduct> productPartiallyUpdate(DtoRestPartiallyProductIU dtoRestPartiallyProductIU) throws Exception;

    public RootEntity<DtoRestProduct> productDelete(String merchantProductNo);

    public RootEntity<DtoRestProduct> productStockAndPriceUpdate(DtoRestStockAndPriceIU dtoRestStockAndPriceIU);

    public RootEntity<DtoRestProduct> productStockUpdate(DtoRestStockIU dtoRestStockIU);

    public RootEntity<DtoRestProduct> productStatusUpdate(DtoRestStatusIU dtoRestStatusIU);

    public RootEntity<List<DtoRestProduct>> getAllProducts();

    public RootEntity<DtoRestProduct> getProduct(String merchantProductId);

}
