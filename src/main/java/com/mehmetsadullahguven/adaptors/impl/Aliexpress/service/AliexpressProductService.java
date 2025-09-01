package com.mehmetsadullahguven.adaptors.impl.Aliexpress.service;

import com.mehmetsadullahguven.adaptors.impl.Aliexpress.AbstractAliexpress;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product.*;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.enums.Language;
import com.mehmetsadullahguven.dto.*;
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

    public DtoProduct create(DtoProductIU dtoProductIU) {
        return upsert(dtoProductIU);
    }

    public DtoProduct update(DtoProductIU dtoProductIU) {
        return upsert(dtoProductIU);
    }

    public DtoProduct delete(DtoProductIU dtoProductIU) {
        Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dtoProductIU.getR2sProductId(), dtoProductIU.getR2sProductListId());
        if (optionalProduct.isPresent()) {
            OfflineProduct offlineProduct = offlineProductStatus(optionalProduct.get().getClientProductId(), dtoProductIU);
            if (Objects.requireNonNull(offlineProduct).getResults().get(0).getOk()) {
                productRepository.delete(optionalProduct.get());
                return getDtoProducts(optionalProduct.get(), "delete");
            }
        }
        throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "Product not found"));
    }

    private DtoProduct upsert(DtoProductIU dtoProductIU) {

        Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dtoProductIU.getR2sProductId(), dtoProductIU.getR2sProductListId());
        return optionalProduct.map(product -> updateProduct(dtoProductIU, product)).orElseGet(() -> createProduct(dtoProductIU));
    }

    private DtoProduct createProduct(DtoProductIU dtoProductIU) {
        if (dtoProductIU.getActive() == 0) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "product not active"));
        }
        ProductObject productObject = handleProduct(dtoProductIU);

        Product savedproduct = saveProduct(dtoProductIU, super.setSerializedObject(productObject), "create_bulk_waiting");

        return getDtoProducts(savedproduct, "create");
    }

    private DtoProduct updateProduct(DtoProductIU dtoProductIU, Product product) {

        if (Objects.equals(product.getClientProductId(), null)) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "clientProductID not found"));
        }

        if (dtoProductIU.getActive() == 0) {
            OfflineProduct offlineProduct = offlineProductStatus(product.getClientProductId(), dtoProductIU);
            Objects.requireNonNull(offlineProduct).getResults().forEach(offlineProductResult -> {
                if (offlineProductResult.getOk()) {
                    throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "product not active"));
                }
            });
        }

        ProductObject productObject = handleProduct(dtoProductIU);
        Product savedproduct = saveProduct(dtoProductIU, this.setSerializedObject(productObject), "update_bulk_waiting");

        return getDtoProducts(savedproduct, "update");

    }

    private ProductObject handleProduct(DtoProductIU dtoProductIU) {
        DtoTranslationIU dtoTranslationIU = getTranslation(dtoProductIU.getTranslations());
        Integer remoteCategoryId = getRemoteCategoryId(dtoProductIU.getCategory());
        List<String> mediaList = getImages(dtoTranslationIU.getImages());

        List<MultiLanguageDescriptionList> multiLanguageDescriptionList = new ArrayList<>();
        List<MultiLanguageSubjectList> multiLanguageSubjectList = new ArrayList<>();

        for (DtoTranslationIU dtoTranslation : dtoProductIU.getTranslations()) {

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
        productObject.setExternalId(dtoProductIU.getR2sProductId());
        productObject.setFreightTemplateId(34039964002L);
        productObject.setLanguage(Language.valueOf(dtoTranslationIU.getLanguage().name()));
        productObject.setMainImageUrlsList(mediaList);
        productObject.setMultiLanguageDescriptionList(multiLanguageDescriptionList);
        productObject.setMultiLanguageSubjectList(multiLanguageSubjectList);
        productObject.setPackageHeight(dtoProductIU.getSize().getHeight());
        productObject.setPackageLength(dtoProductIU.getSize().getLength());
        productObject.setPackageWidth(dtoProductIU.getSize().getWidth());
        productObject.setWeight(dtoProductIU.getWeight().toString());
        productObject.setShippingLeadTime(30);
        productObject.setProductUnit(100000015);
        productObject.setBulkDiscount(99);
        productObject.setBulkOrder(99);

        if (dtoProductIU.getGtip().isEmpty()) {
            //throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "gtip not found"));
        }else {
            List<HsCodes> hsCodes = Stream.of(new HsCodes()).peek(hsCode -> hsCode.setCode(dtoProductIU.getGtip().get(0).getCodeDotted())).toList();
            productObject.setHsCodes(hsCodes);
        }



        List<SkuInfoList> skuInfoList;

        if (dtoProductIU.getCombines().isEmpty()) {
            skuInfoList = Stream.of(new SkuInfoList()).peek(skuInfo -> {
                skuInfo.setSkuCode(dtoProductIU.getR2sProductId());
                skuInfo.setInventory(dtoProductIU.getQuantity());
                skuInfo.setPrice(dtoProductIU.getPrice().toString());
            }).toList();
        } else {
            skuInfoList = skuAttributeMapping(dtoProductIU);
        }
        productObject.setSkuInfoList(skuInfoList);

        return productObject;
    }

    private List<SkuInfoList> skuAttributeMapping(DtoProductIU dtoProductIU) {
        List<SkuInfoList> skuInfoList = new ArrayList<>();

        dtoProductIU.getCombines().forEach(dtoCombineIU -> {

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
            skuInfo.setPrice(String.valueOf(dtoProductIU.getPrice() + dtoCombineIU.getPriceDiff()));
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

    private OfflineProduct offlineProductStatus(String clientProductId, DtoProductIU dtoProductIU) {

        List<String> productIds = new ArrayList<>();
        productIds.add(clientProductId);
        OfflineProductIU offlineProductIU = new OfflineProductIU();
        offlineProductIU.setProductIds(productIds);

        ResponseEntity<OfflineProduct> responseEntity = super.sendRequest(offlineProductIU, dtoProductIU, OfflineProduct.class, null, "/api/v1/product/offline");
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        }
        return null;
    }

}
