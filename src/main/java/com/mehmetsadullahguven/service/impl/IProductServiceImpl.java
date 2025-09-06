package com.mehmetsadullahguven.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;
import com.mehmetsadullahguven.enums.StatusType;
import com.mehmetsadullahguven.model.Product;
import com.mehmetsadullahguven.repository.IProductRepository;
import com.mehmetsadullahguven.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class IProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public DtoRestProduct createAndUpdate(DtoRestProductIU dtoRestProductIU) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String data = objectMapper.writeValueAsString(dtoRestProductIU);

            Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoRestProductIU.getMerchantProductId());
            Product product;
            product = optionalProduct.orElseGet(Product::new);

            BeanUtils.copyProperties(dtoRestProductIU, product);

            Product newProduct = productRepository.save(product);
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(newProduct, dtoRestProduct);

            return dtoRestProduct;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DtoRestProduct partiallyUpdate(DtoRestProductIU dtoPartiallyProductsIU) throws Exception {

        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoPartiallyProductsIU.getMerchantProductId());

        Product product;
        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();

            Product newProduct = productRepository.save(product);
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(newProduct, dtoRestProduct);
            return dtoRestProduct;
        }
        throw new Exception("merchant ID boş olamaz");
    }

    @Override
    public DtoRestProduct delete(String merchantProductNo) {
        return null;
    }

    @Override
    public DtoRestProduct priceUpdate(DtoRestProductIU dtoRestProductIU) {
        return null;
    }

    @Override
    public DtoRestProduct stockUpdate(DtoRestProductIU dtoRestProductIU) {
        return null;
    }

    protected Product mergeFromDto(DtoRestProductIU dto, Product product) {
        if (dto.getStatus() != null) product.setStatus(dto.getStatus().getStatus());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getStatus() != null) product.setStatus(dto.getStatus().getStatus());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getName() != null) product.setName(dto.getName());

        return product;
    }
}
