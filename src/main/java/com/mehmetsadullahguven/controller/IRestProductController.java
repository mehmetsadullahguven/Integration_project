package com.mehmetsadullahguven.controller;

import com.mehmetsadullahguven.dto.DtoProduct;
import com.mehmetsadullahguven.dto.DtoProductIU;
import com.mehmetsadullahguven.dto.DtoWebhookIU;

public interface IRestProductController {

    public RootEntity<DtoProduct> productCreate(DtoProductIU dtoProductIU, String slug);

    public RootEntity<DtoProduct> productUpdate(DtoProductIU dtoProductIU, String slug);

    public RootEntity<DtoProduct> productDelete(DtoProductIU dtoProductIU, String slug);
}
