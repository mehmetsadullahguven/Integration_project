package com.mehmetsadullahguven.service;

import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;

import java.util.List;

public interface IProductService {

    public DtoRestProduct createAndUpdate(DtoRestProductIU dtoRestProductIU);

    public <T> DtoRestProduct partiallyUpdate(DtoRestProductIU dtoRestProductIU) throws Exception;

    public DtoRestProduct delete(String merchantProductNo);

    public DtoRestProduct priceUpdate(DtoRestProductIU dtoRestProductIU);

    public DtoRestProduct stockUpdate(DtoRestProductIU dtoRestProductIU);
}
