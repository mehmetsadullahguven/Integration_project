package com.mehmetsadullahguven.controller.impl;

import com.mehmetsadullahguven.adaptors.GeneralAdaptorFactory;
import com.mehmetsadullahguven.adaptors.IGeneralAdaptor;
import com.mehmetsadullahguven.controller.IRestProductController;
import com.mehmetsadullahguven.controller.RestBaseController;
import com.mehmetsadullahguven.controller.RootEntity;
import com.mehmetsadullahguven.dto.DtoProduct;
import com.mehmetsadullahguven.dto.DtoProductIU;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/{slug}")
public class RestProductControllerImpl extends RestBaseController implements IRestProductController {

    @Autowired
    private GeneralAdaptorFactory generalAdaptorFactory;

    @PostMapping("/create")
    @Override
    public RootEntity<DtoProduct> productCreate(@Valid @RequestBody DtoProductIU dtoProductIU, @PathVariable String slug) {
        IGeneralAdaptor adaptor = generalAdaptorFactory.getAdaptor(slug);
        return ok(adaptor.productCreate(dtoProductIU), "Product Created");
    }

    @PostMapping("/update")
    @Override
    public RootEntity<DtoProduct> productUpdate(@Valid @RequestBody DtoProductIU dtoProductsIU, @PathVariable String slug) {
        IGeneralAdaptor adaptor = generalAdaptorFactory.getAdaptor(slug);
        return ok(adaptor.productUpdate(dtoProductsIU), "Product Created");
    }

    @PostMapping("/delete")
    @Override
    public RootEntity<DtoProduct> productDelete(@Valid @RequestBody DtoProductIU dtoProductsIU, @PathVariable String slug) {
        IGeneralAdaptor adaptor = generalAdaptorFactory.getAdaptor(slug);
        return ok(adaptor.productDelete(dtoProductsIU), "Product Deleted");
    }
}
