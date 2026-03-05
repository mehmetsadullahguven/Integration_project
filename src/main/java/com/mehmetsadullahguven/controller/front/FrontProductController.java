package com.mehmetsadullahguven.controller.front;

import com.mehmetsadullahguven.controller.rest.RestBaseController;
import com.mehmetsadullahguven.dto.product.frontIU.DtoFrontPartiallyProductIU;
import com.mehmetsadullahguven.dto.product.front.DtoFrontProduct;
import com.mehmetsadullahguven.dto.product.front.DtoFrontProductDetail;
import com.mehmetsadullahguven.dto.product.frontIU.DtoFrontTranslationIU;
import com.mehmetsadullahguven.service.front.FrontProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("front/products")
@CrossOrigin(origins = "http://localhost:5173")
public class FrontProductController extends RestBaseController {

    @Autowired
    private FrontProductService frontProductService;

    @GetMapping("/{merchantProductId}")
    public DtoFrontProductDetail getProduct(@PathVariable("merchantProductId") String merchantProductId)
    {
        return frontProductService.getProduct(merchantProductId);
    }

    @GetMapping("/")
    public List<DtoFrontProduct> getAllProducts()
    {
        return frontProductService.getAllProducts();
    }

    @GetMapping("/variants/{merchantProductId}")
    public List<DtoFrontProduct> getVariants(@PathVariable("merchantProductId") String merchantProductID)
    {
        return frontProductService.getVariants(merchantProductID);
    }

    @PatchMapping("/variants/{id}")
    public DtoFrontProduct productPartiallyUpdate(@PathVariable("id") Long id, @RequestBody DtoFrontPartiallyProductIU dtoFrontPartiallyProductIU)
    {
        return frontProductService.partiallyUpdate(id, dtoFrontPartiallyProductIU);
    }

    @DeleteMapping("/variants/{id}")
    public Long productDelete(@PathVariable("id") Long id)
    {
        return frontProductService.delete(id);
    }

    @PatchMapping("/translation/{productID}")
    public DtoFrontProduct productPartiallyUpdate(@PathVariable("productID") Long productID, @RequestBody DtoFrontTranslationIU dtoFrontTranslationIU)
    {
        return frontProductService.translationUpdate(productID, dtoFrontTranslationIU);
    }
}
