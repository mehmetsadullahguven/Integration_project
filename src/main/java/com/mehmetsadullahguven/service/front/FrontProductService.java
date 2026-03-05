package com.mehmetsadullahguven.service.front;

import com.mehmetsadullahguven.dto.product.front.*;
import com.mehmetsadullahguven.dto.product.frontIU.DtoFrontPartiallyProductIU;
import com.mehmetsadullahguven.dto.product.frontIU.DtoFrontTranslationIU;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class FrontProductService {

    @Autowired
    private IProductRepository productRepository;

    public DtoFrontProductDetail getProduct(String merchantProductId) {
        Optional<Product> optionalProduct = productRepository.findByMerchantProductId(merchantProductId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            DtoFrontProductDetail dtoFrontProductDetail = new DtoFrontProductDetail();

            BeanUtils.copyProperties(product, dtoFrontProductDetail);

            List<DtoFrontTranslation> dtoFrontTranslationList = new ArrayList<>();
            List<Translation> productTranslationList = product.getTranslations();
            for (Translation translation: productTranslationList) {
                DtoFrontTranslation dtoFrontTranslation = new DtoFrontTranslation();
                BeanUtils.copyProperties(translation, dtoFrontTranslation);
                dtoFrontTranslationList.add(dtoFrontTranslation);
            }
            dtoFrontProductDetail.setTranslations(dtoFrontTranslationList);

            List<DtoFrontOption> dtoFrontOptionList = new ArrayList<>();
            List<Option>  productOptionList = product.getOptions();
            for (Option option: productOptionList) {
                DtoFrontOption dtoFrontOption = new DtoFrontOption();
                BeanUtils.copyProperties(option, dtoFrontOption);
                dtoFrontOptionList.add(dtoFrontOption);
            }
            dtoFrontProductDetail.setOptions(dtoFrontOptionList);

            List<DtoFrontImage> dtoFrontImageList = new ArrayList<>();
            List<Image>  productImageList = product.getImages();
            for (Image image: productImageList) {
                DtoFrontImage dtoFrontImage = new DtoFrontImage();
                BeanUtils.copyProperties(image, dtoFrontImage);
                dtoFrontImageList.add(dtoFrontImage);
            }
            dtoFrontProductDetail.setImages(dtoFrontImageList);

            dtoFrontProductDetail.setMainImage(product.getImages().get(0).getPath());
            return dtoFrontProductDetail;
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "merchantProductNo: " + merchantProductId));
    }

    public List<DtoFrontProduct> getAllProducts() {
        List<Product> productList = productRepository.findByMerchantParentProductIdIsNull();

        if (productList.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, ""));
        }

        return productList.stream()
                .map(product -> {
                    DtoFrontProduct dto = new DtoFrontProduct();
                    BeanUtils.copyProperties(product, dto);
                    if (!product.getImages().isEmpty()) {
                        dto.setImagePath(product.getImages().get(0).getPath());
                    }

                    List<DtoFrontTranslation> dtoFrontTranslationList = new ArrayList<>();
                    for (Translation translation: product.getTranslations()) {
                        DtoFrontTranslation dtoFrontTranslation = new DtoFrontTranslation();
                        BeanUtils.copyProperties(translation, dtoFrontTranslation);
                        dtoFrontTranslationList.add(dtoFrontTranslation);
                    }
                    dto.setTranslations(dtoFrontTranslationList);

                    return dto;
                })
                .collect(Collectors.toList());

    }


    public List<DtoFrontProduct> getVariants(String merchantProductId) {

        List<Product> variantList = productRepository.findByMerchantParentProductId(merchantProductId);
        List<DtoFrontProduct> dtoFrontProductList = new ArrayList<>();

        for (Product variant : variantList) {
            DtoFrontProduct dtoFrontProduct = new DtoFrontProduct();
            BeanUtils.copyProperties(variant, dtoFrontProduct);
            dtoFrontProductList.add(dtoFrontProduct);

            List<DtoFrontOption> dtoFrontOptionList = new ArrayList<>();
            List<Option>  productOptionList = variant.getOptions();
            for (Option option: productOptionList) {
                DtoFrontOption dtoFrontOption = new DtoFrontOption();
                BeanUtils.copyProperties(option, dtoFrontOption);
                dtoFrontOptionList.add(dtoFrontOption);
            }
            dtoFrontProduct.setOptions(dtoFrontOptionList);
        }

        return dtoFrontProductList;
    }

    public DtoFrontProduct partiallyUpdate(Long id, DtoFrontPartiallyProductIU dtoFrontPartiallyProductIU) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, ""));
        }

        Product product = optionalProduct.get();
        product.setPrice(dtoFrontPartiallyProductIU.getPrice());
        product.setStock(dtoFrontPartiallyProductIU.getStock());
        Product savedProduct = productRepository.save(product);

        return returnDtoFrontProduct(savedProduct);
    }

    public Long delete(Long id) {
        try {
            productRepository.deleteById(id);
            return id;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DtoFrontProduct translationUpdate(Long productID, DtoFrontTranslationIU dtoFrontTranslationIU)
    {
        Optional<Product> optionalProduct = productRepository.findById(productID);
        if (optionalProduct.isEmpty()) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.PRODUCT_EXIST, "productID: " + productID));
        }

        Product product = optionalProduct.get();

        AtomicReference<Boolean> updateTranslation = new AtomicReference<>(false);
        product.getTranslations().forEach(translation ->{
            if (translation.getLanguage() == dtoFrontTranslationIU.getLanguage()) {
                translation.setName(dtoFrontTranslationIU.getName());
                translation.setDescription(dtoFrontTranslationIU.getDescription());
                updateTranslation.set(true);
            }
        });
        if (updateTranslation.get() == false) {
            Translation translation = new Translation();
            BeanUtils.copyProperties(dtoFrontTranslationIU, translation);
            translation.setProduct(product);
            product.getTranslations().add(translation);
        }
        Product savedProduct = productRepository.save(product);

        return returnDtoFrontProduct(savedProduct);
    }

    protected DtoFrontProduct returnDtoFrontProduct(Product product)
    {

        DtoFrontProduct dtoFrontProduct = new DtoFrontProduct();
        BeanUtils.copyProperties(product, dtoFrontProduct);

        List<DtoFrontTranslation> dtoFrontTranslationList = new ArrayList<>();
        List<Translation> productTranslationList = product.getTranslations();
        for (Translation translation: productTranslationList) {
            DtoFrontTranslation dtoFrontTranslation = new DtoFrontTranslation();
            BeanUtils.copyProperties(translation, dtoFrontTranslation);
            dtoFrontTranslationList.add(dtoFrontTranslation);
        }
        dtoFrontProduct.setTranslations(dtoFrontTranslationList);

        List<DtoFrontOption> dtoFrontOptionList = new ArrayList<>();
        List<Option>  productOptionList = product.getOptions();
        for (Option option: productOptionList) {
            DtoFrontOption dtoFrontOption = new DtoFrontOption();
            BeanUtils.copyProperties(option, dtoFrontOption);
            dtoFrontOptionList.add(dtoFrontOption);
        }
        dtoFrontProduct.setOptions(dtoFrontOptionList);


        return dtoFrontProduct;
    }
}
