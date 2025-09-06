package com.mehmetsadullahguven.controller.rest;

import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;

import java.util.List;
import java.util.Map;

public interface IRestProductController {

    public RootEntity<DtoRestProduct> productCreateAndUpdate(DtoRestProductIU dtoRestProductIU);

    public <T> RootEntity<DtoRestProduct> productPartiallyUpdate(DtoRestProductIU dtoRestProductIU) throws Exception;

    public RootEntity<DtoRestProduct> productDelete(String merchantProductNo);

    public RootEntity<DtoRestProduct> productPriceUpdate(DtoRestProductIU dtoRestProductIU);

    public RootEntity<DtoRestProduct> productStockUpdate(DtoRestProductIU dtoRestProductIU);

}
