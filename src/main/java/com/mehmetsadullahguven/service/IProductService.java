package com.mehmetsadullahguven.service;

import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.*;

import java.util.List;

public interface IProductService {

    public DtoRestProduct createAndUpdate(DtoRestProductIU dtoRestProductIU);

    public <T> DtoRestProduct partiallyUpdate(DtoRestPartiallyProductIU dtoRestPartiallyProductIU) throws Exception;

    public DtoRestProduct delete(String merchantProductNo);

    public DtoRestProduct stockAndPriceUpdate(DtoRestStockAndPriceIU dtoRestStockAndPriceIU);

    public DtoRestProduct stockUpdate(DtoRestStockIU dtoRestStockIU);

    public DtoRestProduct statusUpdate(DtoRestStatusIU dtoRestStatusIU);

    public List<DtoRestProduct> getAllProducts();

    public DtoRestProduct getProduct(String merchantProductId);
    }
