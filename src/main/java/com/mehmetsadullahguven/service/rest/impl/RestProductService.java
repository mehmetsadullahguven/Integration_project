package com.mehmetsadullahguven.service.rest.impl;

import com.mehmetsadullahguven.dto.product.rest.DtoRestProduct;
import com.mehmetsadullahguven.dto.product.restIU.*;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Image;
import com.mehmetsadullahguven.model.Option;
import com.mehmetsadullahguven.model.Product;
import com.mehmetsadullahguven.model.Translation;
import com.mehmetsadullahguven.repository.IProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class RestProductService {

    @Autowired
    private IProductRepository productRepository;

    public DtoRestProduct createAndUpdate(DtoRestProductIU dtoRestProductIU)
    {
        return upsertProduct(dtoRestProductIU);
    }


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


    public DtoRestProduct delete(String merchantProductNo)
    {

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


    public DtoRestProduct stockAndPriceUpdate(DtoRestStockAndPriceIU dtoRestStockAndPriceIU)
    {
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


    public DtoRestProduct stockUpdate(DtoRestStockIU dtoRestStockIU)
    {
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


    public DtoRestProduct statusUpdate(DtoRestStatusIU dtoRestStatusIU)
    {
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


    public DtoRestProduct getProduct(String merchantProductId)
    {

        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(merchantProductId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(product, dtoRestProduct);
            dtoRestProduct.setAction("getDetail");
            dtoRestProduct.setImage(product.getImages().get(0).getPath());
            return dtoRestProduct;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + merchantProductId));
    }


    // Bulk Methods

    public List<DtoRestProduct> bulkProductCreateAndUpdate(List<DtoRestProductIU> dtoRestProductIUList)
    {
        ArrayList<DtoRestProduct> dtoRestProductList = new ArrayList<>();

        for (DtoRestProductIU dtoRestProductIU: dtoRestProductIUList) {
            DtoRestProduct dtoRestProduct = upsertProduct(dtoRestProductIU);
            dtoRestProductList.add(dtoRestProduct);
        }

        return dtoRestProductList;
    }

    public List<DtoRestProduct> getAllProducts()
    {

        List<Product> productList = productRepository.findAll();
        ArrayList<DtoRestProduct> dtoRestProductList= new ArrayList<>();

        if (productList.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, ""));
        }

        for (Product product: productList) {
            DtoRestProduct dtoRestProduct = new DtoRestProduct();
            BeanUtils.copyProperties(product, dtoRestProduct);
            dtoRestProduct.setImage(product.getImages().get(0).getPath());
            dtoRestProductList.add(dtoRestProduct);
        }

        return dtoRestProductList;
    }


    // Common Methods

    protected DtoRestProduct upsertProduct(DtoRestProductIU dtoRestProductIU)
    {
        DtoRestProduct dtoRestProduct = new DtoRestProduct();

        try {
            Optional<Product> optionalProduct = productRepository.findByMerchantProductId(dtoRestProductIU.getMerchantProductId());
            Product product;

            if (optionalProduct.isEmpty()) {
                product = new Product();
                dtoRestProduct.setAction("create");
            }else {
                product = optionalProduct.get();
                dtoRestProduct.setAction("update");
            }
            Long id = product.getId();

            BeanUtils.copyProperties(dtoRestProductIU, product);

            product.setId(id);

            if (dtoRestProductIU.getTranslations() != null) {
                List<Translation> translationList = upsertTranslations(dtoRestProductIU.getTranslations(), product);
                product.setTranslations(translationList);
            }
            if (dtoRestProductIU.getOptions() != null) {
                List<Option> optionList = upsertOptions(dtoRestProductIU.getOptions(), product);
                product.setOptions(optionList);
            }
            if (dtoRestProductIU.getImages() != null) {
                List<Image> imageList = upsertImages(dtoRestProductIU.getImages(), product);
                product.getImages().addAll(imageList);
            }

            Product newProduct = productRepository.save(product);

            BeanUtils.copyProperties(newProduct, dtoRestProduct);

            return dtoRestProduct;
        }catch (RuntimeException e) {
            dtoRestProduct.setMerchantProductId(dtoRestProductIU.getMerchantProductId());
            dtoRestProduct.setError(e.getMessage());
            return dtoRestProduct;
        }
    }

    protected Product mergeFromDto(DtoRestPartiallyProductIU dto, Product product)
    {

        if (dto.getStatus() != null) product.setStatus(dto.getStatus());
        if (dto.getBrand() != null) product.setBrand(dto.getBrand());
        if (dto.getEan() != null) product.setEan(dto.getEan());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getPurchasePrice() != null) product.setPurchasePrice(dto.getPurchasePrice());
        if (dto.getVatRateType() != null) product.setVatRateType(dto.getVatRateType());
        if (dto.getCurrency() != null) product.setCurrency(dto.getCurrency());
        if (dto.getGoogleCategoryId() != null) product.setGoogleCategoryId(dto.getGoogleCategoryId());
        if (dto.getStock() != null) product.setStock(dto.getStock());

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

    protected List<Image> upsertImages(List<DtoImageIU> dtoImageIUList, Product product)
    {
        List<Image> imageList = new ArrayList<>();

        for (DtoImageIU dtoImageIU: dtoImageIUList) {
            Image existingImage = product.getImages().stream()
                    .filter(i -> i.getPath() == dtoImageIU.getPath())
                    .findFirst()
                    .orElse(null);

            if (existingImage != null) {
                imageList.add(existingImage);
            }else {
                Image image = new Image();
                BeanUtils.copyProperties(dtoImageIU, image);
                image.setProduct(product);
                imageList.add(image);
            }
        }

        return imageList;
    }

    protected List<Translation> upsertTranslations(List<DtoTranslationIU> dtoTranslationIUList, Product product)
    {
        List<Translation> translationList = new ArrayList<>();

        for (DtoTranslationIU dtoTranslationIU : dtoTranslationIUList) {

            Translation existingTranslation = product.getTranslations().stream()
                    .filter(t -> t.getLanguage() == dtoTranslationIU.getLanguage())
                    .findFirst()
                    .orElse(null);

            if (existingTranslation != null) {
                if (!existingTranslation.getName().equals(dtoTranslationIU.getName())
                        || !existingTranslation.getDescription().equals(dtoTranslationIU.getDescription())) {
                    existingTranslation.setName(dtoTranslationIU.getName());
                    existingTranslation.setDescription(dtoTranslationIU.getDescription());
                }
                translationList.add(existingTranslation);
            } else {
                Translation newTranslation = new Translation();
                BeanUtils.copyProperties(dtoTranslationIU, newTranslation);
                newTranslation.setProduct(product);
                translationList.add(newTranslation);
            }
        }

        return translationList;
    }

    protected List<Option> upsertOptions(List<DtoOptionIU> dtoOptionIUList, Product product)
    {
        List<Option> optionList = new ArrayList<>();

        for (DtoOptionIU dtoOptionIU : dtoOptionIUList) {

            Option existingOption = product.getOptions().stream()
                    .filter(t -> Objects.equals(t.getName(), dtoOptionIU.getName()))
                    .findFirst()
                    .orElse(null);

            if (existingOption != null) {
                if (!existingOption.getName().equals(existingOption.getName())
                        || !existingOption.getValue().equals(dtoOptionIU.getValue())) {
                    existingOption.setName(dtoOptionIU.getName());
                    existingOption.setValue(dtoOptionIU.getValue());
                }
                optionList.add(existingOption);
            } else {
                Option newOption = new Option();
                BeanUtils.copyProperties(dtoOptionIU, newOption);
                newOption.setProduct(product);
                optionList.add(newOption);
            }
        }

        return optionList;
    }
}
