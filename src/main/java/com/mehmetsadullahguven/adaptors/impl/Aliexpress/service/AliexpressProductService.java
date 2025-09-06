package com.mehmetsadullahguven.adaptors.impl.Aliexpress.service;

import com.mehmetsadullahguven.adaptors.impl.Aliexpress.AbstractAliexpress;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product.*;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.enums.Language;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class AliexpressProductService extends AbstractAliexpress {

    public DtoProduct create(DtoRestProductIU dtoRestProductIU) {
        return upsert(dtoRestProductIU);
    }

    public DtoProduct update(DtoRestProductIU dtoRestProductIU) {
        return upsert(dtoRestProductIU);
    }

    public DtoProduct delete(DtoRestProductIU dtoRestProductIU) {
        Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dtoRestProductIU.getMerchantProductId(), dtoRestProductIU.getMerchantParentProductId());
        if (optionalProduct.isPresent()) {
            OfflineProduct offlineProduct = offlineProductStatus(optionalProduct.get().getClientProductId(), dtoRestProductIU);
            if (Objects.requireNonNull(offlineProduct).getResults().get(0).getOk()) {
                productRepository.delete(optionalProduct.get());
                return getDtoProducts(optionalProduct.get(), "delete");
            }
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Product not found"));
    }

    private DtoProduct upsert(DtoRestProductIU dtoRestProductIU) {

        Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dtoRestProductIU.getMerchantProductId(), dtoRestProductIU.getMerchantParentProductId());
        return optionalProduct.map(product -> updateProduct(dtoRestProductIU, product)).orElseGet(() -> createProduct(dtoRestProductIU));
    }

    private DtoProduct createProduct(DtoRestProductIU dtoRestProductIU) {
        if (dtoRestProductIU.getActive() == 0) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "product not active"));
        }
        ProductObject productObject = handleProduct(dtoRestProductIU);

        Product savedproduct = saveProduct(dtoRestProductIU, super.setSerializedObject(productObject), "create_bulk_waiting");

        return getDtoProducts(savedproduct, "create");
    }

    private DtoProduct updateProduct(DtoRestProductIU dtoRestProductIU, Product product) {

        if (Objects.equals(product.getClientProductId(), null)) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "clientProductID not found"));
        }

        if (dtoRestProductIU.getActive() == 0) {
            OfflineProduct offlineProduct = offlineProductStatus(product.getClientProductId(), dtoRestProductIU);
            Objects.requireNonNull(offlineProduct).getResults().forEach(offlineProductResult -> {
                if (offlineProductResult.getOk()) {
                    throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "product not active"));
                }
            });
        }

        ProductObject productObject = handleProduct(dtoRestProductIU);
        Product savedproduct = saveProduct(dtoRestProductIU, this.setSerializedObject(productObject), "update_bulk_waiting");

        return getDtoProducts(savedproduct, "update");

    }

    private ProductObject handleProduct(DtoRestProductIU dtoRestProductIU) {
        DtoTranslationIU dtoTranslationIU = getTranslation(dtoRestProductIU.getTranslations());
        Integer remoteCategoryId = getRemoteCategoryId(dtoRestProductIU.getCategory());
        List<String> mediaList = getImages(dtoTranslationIU.getImages());

        List<MultiLanguageDescriptionList> multiLanguageDescriptionList = new ArrayList<>();
        List<MultiLanguageSubjectList> multiLanguageSubjectList = new ArrayList<>();

        for (DtoTranslationIU dtoTranslation : dtoRestProductIU.getTranslations()) {

            if (Objects.isNull(dtoTranslation.getLanguage())) {
                continue;
            }
            MultiLanguageDescriptionList multiLanguageDescription = new MultiLanguageDescriptionList();
            multiLanguageDescription.setLanguage(Language.valueOf(dtoTranslation.getLanguage().name()));
            multiLanguageDescription.setMobile(dtoTranslation.getDescription());
            multiLanguageDescription.setWeb(dtoTranslation.getDescription());
            multiLanguageDescriptionList.add(multiLanguageDescription);

            MultiLanguageSubjectList multiLanguageSubject = new MultiLanguageSubjectList();
            multiLanguageSubject.setLanguage(Language.valueOf(dtoTranslation.getLanguage().name()));
            multiLanguageSubject.setSubject(dtoTranslation.getTitle());
            multiLanguageSubjectList.add(multiLanguageSubject);
        }

        ProductObject productObject = new ProductObject();
        productObject.setAliexpressCategoryId(remoteCategoryId);
        productObject.setExternalId(dtoRestProductIU.getR2sProductId());
        productObject.setFreightTemplateId(34039964002L);
        productObject.setLanguage(Language.valueOf(dtoTranslationIU.getLanguage().name()));
        productObject.setMainImageUrlsList(mediaList);
        productObject.setMultiLanguageDescriptionList(multiLanguageDescriptionList);
        productObject.setMultiLanguageSubjectList(multiLanguageSubjectList);
        productObject.setPackageHeight(dtoRestProductIU.getSize().getHeight());
        productObject.setPackageLength(dtoRestProductIU.getSize().getLength());
        productObject.setPackageWidth(dtoRestProductIU.getSize().getWidth());
        productObject.setWeight(dtoRestProductIU.getWeight().toString());
        productObject.setShippingLeadTime(30);
        productObject.setProductUnit(100000015);
        productObject.setBulkDiscount(99);
        productObject.setBulkOrder(99);

        if (dtoRestProductIU.getGtip().isEmpty()) {
            //throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "gtip not found"));
        }else {
            List<HsCodes> hsCodes = Stream.of(new HsCodes()).peek(hsCode -> hsCode.setCode(dtoRestProductIU.getGtip().get(0).getCodeDotted())).toList();
            productObject.setHsCodes(hsCodes);
        }



        List<SkuInfoList> skuInfoList;

        if (dtoRestProductIU.getCombines().isEmpty()) {
            skuInfoList = Stream.of(new SkuInfoList()).peek(skuInfo -> {
                skuInfo.setSkuCode(dtoRestProductIU.getR2sProductId());
                skuInfo.setInventory(dtoRestProductIU.getQuantity());
                skuInfo.setPrice(dtoRestProductIU.getPrice().toString());
            }).toList();
        } else {
            skuInfoList = skuAttributeMapping(dtoRestProductIU);
        }
        productObject.setSkuInfoList(skuInfoList);

        return productObject;
    }

    private List<SkuInfoList> skuAttributeMapping(DtoRestProductIU dtoRestProductIU) {
        List<SkuInfoList> skuInfoList = new ArrayList<>();

        dtoRestProductIU.getCombines().forEach(dtoCombineIU -> {

            List<SkuAttributesList> skuAttributeList = new ArrayList<>();
            dtoCombineIU.getVariant().forEach(dtoVariantIU -> {
                SkuAttributesList skuAttribute = new SkuAttributesList();
                DtoVariantTranslationIU variantTranslationIU = super.getTranslation(dtoVariantIU.getTranslations());
                DtoVariantParameterTranslationIU variantParameterTranslationIU = getTranslation(dtoVariantIU.getParameter().getTranslations());
                skuAttribute.setSkuAttributeNameId(variantTranslationIU.getTitle());
                skuAttribute.setSkuAttributeValueId(variantParameterTranslationIU.getTitle());
                skuAttributeList.add(skuAttribute);
            });
            SkuInfoList skuInfo = new SkuInfoList();
            skuInfo.setSkuCode(dtoCombineIU.getCombineId());
            skuInfo.setSkuAttributesList(skuAttributeList);
            skuInfo.setPrice(String.valueOf(dtoRestProductIU.getPrice() + dtoCombineIU.getPriceDiff()));
            skuInfo.setInventory(dtoCombineIU.getQuantity());
            skuInfo.setDiscountPrice("0");
            skuInfoList.add(skuInfo);
            if (skuInfoList.size() == 54) {
                System.out.println("geldi");
            }
        });
        return skuInfoList;
    }

    private List<String> getImages(List<DtoImageIU> images) {
        int size = 0;
        List<String> imageList = new ArrayList<>();

        for (DtoImageIU dtoImageIU : images) {
            if (dtoImageIU.getLanguage().equals(LanguageType.en) && size < 6) {
                imageList.add(dtoImageIU.getPath());
                size = size + 1;
            } else {
                break;
            }
        }
        return imageList;
    }

    private Integer getRemoteCategoryId(DtoCategoryIU dtoCategoryIU) {
        return dtoCategoryIU.getGoogle_id();
    }

    private DtoProduct getDtoProducts(Product product, String action) {
        DtoProduct dtoProduct = new DtoProduct();
        dtoProduct.setR2sProductId(product.getR2sProductId());
        dtoProduct.setStatus(action);

        return dtoProduct;
    }

    private OfflineProduct offlineProductStatus(String clientProductId, DtoRestProductIU dtoRestProductIU) {

        List<String> productIds = new ArrayList<>();
        productIds.add(clientProductId);
        OfflineProductIU offlineProductIU = new OfflineProductIU();
        offlineProductIU.setProductIds(productIds);

        ResponseEntity<OfflineProduct> responseEntity = super.sendRequest(offlineProductIU, dtoRestProductIU, OfflineProduct.class, null, "/api/v1/product/offline");
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        return null;
    }

}
