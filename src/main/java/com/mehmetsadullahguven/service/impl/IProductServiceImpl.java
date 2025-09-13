package com.mehmetsadullahguven.service.impl;

import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.*;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Image;
import com.mehmetsadullahguven.model.Product;
import com.mehmetsadullahguven.repository.IProductRepository;
import com.mehmetsadullahguven.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public DtoRestProduct createAndUpdate(DtoRestProductIU dtoRestProductIU) {

        try {
            Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoRestProductIU.getMerchantProductId());
            Product product;
            product = optionalProduct.orElseGet(Product::new);

            BeanUtils.copyProperties(dtoRestProductIU, product);

            Product newProduct = productRepository.save(product);
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(newProduct, dtoRestProduct);

            return dtoRestProduct;
        } catch (RuntimeException e) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, e.getMessage()));
        }

    }

    @Override
    public DtoRestProduct partiallyUpdate(DtoRestPartiallyProductIU dtoRestPartiallyProductIU)
    {

        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoRestPartiallyProductIU.getMerchantProductId());

        Product product;
        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();

            Product mergedProduct = this.mergeFromDto(dtoRestPartiallyProductIU, product);
            Product savedProduct = productRepository.save(mergedProduct);

            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(savedProduct, dtoRestProduct);

            return dtoRestProduct;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + dtoRestPartiallyProductIU.getMerchantProductId()));
    }

    @Override
    public DtoRestProduct delete(String merchantProductNo) {

        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(merchantProductNo);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(product, dtoRestProduct);
            productRepository.delete(product);
            return dtoRestProduct;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + merchantProductNo));

    }

    @Override
    public DtoRestProduct stockAndPriceUpdate(DtoRestStockAndPriceIU dtoRestStockAndPriceIU) {
        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoRestStockAndPriceIU.getMerchantProductId());
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setStock(dtoRestStockAndPriceIU.getStock());
            product.setPrice(dtoRestStockAndPriceIU.getPrice());
            Product savedProduct = productRepository.save(product);

            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(savedProduct, dtoRestProduct);

            return dtoRestProduct;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + dtoRestStockAndPriceIU.getMerchantProductId()));
    }

    @Override
    public DtoRestProduct stockUpdate(DtoRestStockIU dtoRestStockIU) {
        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoRestStockIU.getMerchantProductId());
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setStock(dtoRestStockIU.getStock());
            Product savedProduct = productRepository.save(product);

            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(savedProduct, dtoRestProduct);

            return dtoRestProduct;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + dtoRestStockIU.getMerchantProductId()));

    }

    @Override
    public DtoRestProduct statusUpdate(DtoRestStatusIU dtoRestStatusIU) {
        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoRestStatusIU.getMerchantProductId());
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            product.setStatus(dtoRestStatusIU.getStatus());
            Product savedProduct = productRepository.save(product);

            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(savedProduct, dtoRestProduct);

            return dtoRestProduct;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + dtoRestStatusIU.getMerchantProductId()));

    }

    @Override
    public DtoRestProduct getProduct(String merchantProductId) {

        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(merchantProductId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(product, dtoRestProduct);

            return dtoRestProduct;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + merchantProductId));
    }

    @Override
    public List<DtoRestProduct> getAllProducts() {

        List<Product> productList = productRepository.findAll();
        ArrayList<DtoRestProduct> dtoRestProductList= new ArrayList<>();

        if (productList.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, ""));
        }

        for (Product product: productList) {
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(product, dtoRestProduct);
            dtoRestProductList.add(dtoRestProduct);
        }

        return dtoRestProductList;
    }

    protected Product mergeFromDto(DtoRestPartiallyProductIU dto, Product product) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getStatus() != null) product.setStatus(dto.getStatus());
        if (dto.getBrand() != null) product.setBrand(dto.getBrand());
        if (dto.getSize() != null) product.setSize(dto.getSize());
        if (dto.getColor() != null) product.setColor(dto.getColor());
        if (dto.getEan() != null) product.setEan(dto.getEan());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getPurchasePrice() != null) product.setPurchasePrice(dto.getPurchasePrice());
        if (dto.getVatRateType() != null) product.setVatRateType(dto.getVatRateType());
        if (dto.getCurrency() != null) product.setCurrency(dto.getCurrency());
        if (dto.getLanguage() != null) product.setLanguage(dto.getLanguage());
        if (dto.getGoogleCategoryId() != null) product.setGoogleCategoryId(dto.getGoogleCategoryId());

        List<Image> images = product.getImages();
        boolean hasImage = false;
        if (!dto.getImages().isEmpty()) {
            for (DtoImageIU dtoImageIU: dto.getImages()) {
                Image newImage = new Image();
                BeanUtils.copyProperties(dtoImageIU, newImage);
                for (Image productImage: images) {
                    if (Objects.equals(productImage.getPath(), dtoImageIU.getPath())) {
                        hasImage = true;
                        break;
                    }
                }
                if (!hasImage) {
                    images.add(newImage);
                }
            }
            product.setImages(images);
        }

        return product;
    }
}
