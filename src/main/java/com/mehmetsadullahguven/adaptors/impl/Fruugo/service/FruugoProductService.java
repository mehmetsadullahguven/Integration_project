package com.mehmetsadullahguven.adaptors.impl.Fruugo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.AbstractFruugo;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product.*;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.CodeType;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.Currency;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.Language;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.StockStatus;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.dto.product.restIU.DtoRestProductIU;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FruugoProductService extends AbstractFruugo {


    public DtoProduct create(DtoRestProductIU dtoRestProductIU) {
        return upsert(dtoRestProductIU);
    }

    public DtoProduct update(DtoRestProductIU dtoRestProductIU) {
        return upsert(dtoRestProductIU);
    }

    public DtoProduct delete(DtoRestProductIU dtoRestProductIU) {
        ProductObject productObject = handleProduct(dtoRestProductIU, "delete");

        Product savedproduct = saveProduct(dtoRestProductIU, setSerializedObject(productObject), "delete_bulk_waiting");

        return getDtoProducts(savedproduct, "delete");
    }

    private DtoProduct upsert(DtoRestProductIU dtoRestProductIU) {

        Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dtoRestProductIU.getR2sProductId(), dtoRestProductIU.getR2sProductListId());
        return optionalProduct.map(product -> updateProduct(dtoRestProductIU, product)).orElseGet(() -> createProduct(dtoRestProductIU));
    }

    private DtoProduct createProduct(DtoRestProductIU dtoRestProductIU) {
        ProductObject productObject = handleProduct(dtoRestProductIU, "create");

        Product savedproduct = saveProduct(dtoRestProductIU, setSerializedObject(productObject), "create_bulk_waiting");

        return getDtoProducts(savedproduct, "create");
    }

    private DtoProduct updateProduct(DtoRestProductIU dtoRestProductIU, Product product) {
        if (Objects.equals(product.getClientProductId(), null)) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "clientProductID not found"));
        }

        if (dtoRestProductIU.getChangedProperties().getQuantity() > 0) {
            Map<String, DtoChangedCombineIU> combines = dtoRestProductIU.getChangedProperties().getCombines();
            if (!combines.isEmpty()) {
                List<DtoCombineIU> dtoCombineIUList = new ArrayList<>();
                combines.forEach((key, value) -> {
                    DtoCombineIU dtoCombineIU = dtoRestProductIU.getCombines().get(Integer.parseInt(key));
                    dtoCombineIU.setQuantity(value.getQuantity());
                    dtoCombineIUList.add(dtoCombineIU);
                });
                dtoRestProductIU.setCombines(dtoCombineIUList);
            }
            ProductObject productObject = handleProduct(dtoRestProductIU, "patch");
            Product savedproduct = saveProduct(dtoRestProductIU, setSerializedObject(productObject), "patch_bulk_waiting");

            return getDtoProducts(savedproduct, "patch");
        } else {
            ProductObject productObject = handleProduct(dtoRestProductIU, "update");
            Product savedproduct = saveProduct(dtoRestProductIU, setSerializedObject(productObject), "update_bulk_waiting");

            return getDtoProducts(savedproduct, "update");
        }
    }

    private ProductObject handleProduct(DtoRestProductIU dtoRestProductIU, String operation) {

        ProductType productType = new ProductType();
        productType.setProductId(dtoRestProductIU.getR2sProductId());
        List<Sku> skus = new ArrayList<>();

        if (!"delete".equals(operation) && !"patch".equals(operation)) {
            DtoCategoryTranslationIU dtoCategoryTranslationIU = getTranslation(dtoRestProductIU.getCategory().getTranslations());

            productType.setBrand(dtoRestProductIU.getBrand());
            productType.setCategory(dtoCategoryTranslationIU.getSource());
            productType.setManufacturer(dtoRestProductIU.getManufacture());
            if (!dtoRestProductIU.getCombines().isEmpty()) {
                dtoRestProductIU.getCombines().forEach(dtoCombineIU -> skus.add(combineHandle(dtoCombineIU, dtoRestProductIU)));
            } else {
                skus.add(notNullCombineHandle(dtoRestProductIU));
            }
        } else {
            if (!dtoRestProductIU.getCombines().isEmpty()) {
                dtoRestProductIU.getCombines().forEach(dtoCombineIU -> skus.add(patchCombineHandle(dtoCombineIU, dtoRestProductIU, operation)));
            } else {
                skus.add(notNullPatchCombineHandle(dtoRestProductIU, operation));
            }
        }
        ProductObject productObject = new ProductObject();
        productObject.setProduct(productType);
        productObject.setSkus(skus);

        return productObject;
    }

    private Sku combineHandle(DtoCombineIU dtoCombineIU, DtoRestProductIU dtoRestProductIU) {

        DtoTranslationIU dtoTranslationIU = getTranslation(dtoRestProductIU.getTranslations());
        List<Media> mediaList = getImages(dtoTranslationIU.getImages());

        List<Gtin> gtinList = new ArrayList<>();
        Gtin gtin = new Gtin(CodeType.OTHER, dtoCombineIU.getCombineId() + "1");
        gtinList.add(gtin);

        List<Attribute> attributeList = new ArrayList<>();
        dtoCombineIU.getVariant().forEach(dtoVariantIU -> {
            DtoVariantTranslationIU dtoVariantTranslationIU = getTranslation(dtoVariantIU.getTranslations());

            DtoVariantParameterTranslationIU dtoVariantParameterTranslationIU = getTranslation(dtoVariantIU.getParameter().getTranslations());

            Attribute attribute = new Attribute();
            attribute.setName(dtoVariantTranslationIU.getTitle());
            attribute.setValue(dtoVariantParameterTranslationIU.getTitle());
            attributeList.add(attribute);
        });

        SkuDescription skuDescription = new SkuDescription();
        skuDescription.setLanguage(Language.en);
        skuDescription.setTitle(dtoTranslationIU.getTitle());
        skuDescription.setText(dtoTranslationIU.getDescription());
        skuDescription.setAttributes(attributeList);

        Detail detail = new Detail();
        List<SkuDescription> skuDescriptionList = new ArrayList<>();
        skuDescriptionList.add(skuDescription);
        detail.setSkuDescriptions(skuDescriptionList);
        detail.setMedia(mediaList);

        SupplyInfo supplyInfo = new SupplyInfo();
        supplyInfo.setStockQuantity(dtoCombineIU.getQuantity());
        supplyInfo.setStockStatus(dtoCombineIU.getQuantity() > 0 && dtoRestProductIU.getActive().equals(1) ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);

        Sku sku = new Sku();
        sku.setSkuId(dtoCombineIU.getCombineId());
        sku.setGtins(gtinList);
        sku.setDetails(detail);
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoRestProductIU, dtoCombineIU));
        sku.setPackageWeight(dtoRestProductIU.getWeight());
        sku.setVolume(getVolume(dtoRestProductIU.getSize()));
        return sku;
    }

    private Sku notNullCombineHandle(DtoRestProductIU dtoRestProductIU) {

        int i = 0;
        DtoTranslationIU dtoTranslationIU = getTranslation(dtoRestProductIU.getTranslations());

        List<Media> mediaList = getImages(dtoTranslationIU.getImages());

        List<Gtin> gtinList = new ArrayList<>();
        Gtin gtin = new Gtin(CodeType.OTHER, dtoRestProductIU.getR2sProductId() + "_" + i);
        gtinList.add(gtin);

        List<Attribute> attributeList = new ArrayList<>();
        Attribute attribute = new Attribute();
        attribute.setName("Type");
        attribute.setValue("Standard");
        attributeList.add(attribute);

        SkuDescription skuDescription = new SkuDescription();
        skuDescription.setLanguage(Language.en);
        skuDescription.setTitle(dtoTranslationIU.getTitle());
        skuDescription.setText(dtoTranslationIU.getDescription());
        skuDescription.setAttributes(attributeList);

        Detail detail = new Detail();
        List<SkuDescription> skuDescriptionList = new ArrayList<>();
        skuDescriptionList.add(skuDescription);
        detail.setSkuDescriptions(skuDescriptionList);
        detail.setMedia(mediaList);

        SupplyInfo supplyInfo = new SupplyInfo();
        supplyInfo.setStockQuantity(dtoRestProductIU.getQuantity());
        supplyInfo.setStockStatus(dtoRestProductIU.getQuantity() > 0 && dtoRestProductIU.getActive().equals(1) ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);


        Sku sku = new Sku();
        sku.setSkuId(dtoRestProductIU.getR2sProductId() + "_" + 0);
        sku.setGtins(gtinList);
        sku.setDetails(detail);
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoRestProductIU, null));
        sku.setPackageWeight(dtoRestProductIU.getWeight());
        sku.setVolume(getVolume(dtoRestProductIU.getSize()));

        return sku;

    }

    private Sku patchCombineHandle(DtoCombineIU dtoCombineIU, DtoRestProductIU dtoRestProductIU, String operation) {
        SupplyInfo supplyInfo = new SupplyInfo();
        if (operation.equals("delete")) {
            supplyInfo.setStockStatus(StockStatus.NOTAVAILABLE);
            supplyInfo.setStockQuantity(0);
        } else {
            supplyInfo.setStockStatus(dtoCombineIU.getQuantity() > 0 || dtoRestProductIU.getActive() == 1 ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);
            supplyInfo.setStockQuantity(dtoCombineIU.getQuantity());
        }

        Sku sku = new Sku();
        sku.setSkuId(dtoCombineIU.getCombineId());
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoRestProductIU, dtoCombineIU));
        return sku;
    }

    private Sku notNullPatchCombineHandle(DtoRestProductIU dtoRestProductIU, String operation) {
        SupplyInfo supplyInfo = new SupplyInfo();
        if (operation.equals("delete")) {
            supplyInfo.setStockStatus(StockStatus.NOTAVAILABLE);
            supplyInfo.setStockQuantity(0);
        } else {
            supplyInfo.setStockStatus(dtoRestProductIU.getQuantity() > 0 || dtoRestProductIU.getActive() == 1 ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);
            supplyInfo.setStockQuantity(dtoRestProductIU.getQuantity());
        }

        Sku sku = new Sku();
        sku.setSkuId(dtoRestProductIU.getR2sProductId() + "_" + 0);
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoRestProductIU, null));
        return sku;
    }

    private List<Media> getImages(List<DtoImageIU> images) {
        int size = 0;
        List<Media> mediaList = new ArrayList<>();
        for (DtoImageIU dtoImageIU : images) {
            if (dtoImageIU.getLanguage().equals(LanguageType.en) && size < 5) {
                Media media = new Media();
                media.setUrl(dtoImageIU.getPath());
                mediaList.add(media);
                size = size + 1;
            } else {
                break;
            }
        }
        return mediaList;
    }

    private Integer getVolume(DtoSizeIU dtoSizeIU) {
        return dtoSizeIU.getHeight() * dtoSizeIU.getLength() * dtoSizeIU.getWidth();
    }

    private List<PriceInfo> getPriceList(DtoRestProductIU dtoRestProductIU, DtoCombineIU dtoCombineIU) {
        List<PriceInfo> priceInfoList = new ArrayList<>();
        if (dtoRestProductIU != null) {
            if (dtoCombineIU.getPriceDiff() > 0) {
                dtoRestProductIU.setPrice(dtoCombineIU.getPriceDiff() + dtoRestProductIU.getPrice());
            }
        }

        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setCurrency(Currency.valueOf(dtoRestProductIU != null ? dtoRestProductIU.getCurrencyCode().name() : null));
        priceInfo.setVatRate(dtoRestProductIU != null ? dtoRestProductIU.getTaxRate().floatValue() : 0);
        priceInfo.setNormalPrice(new NormalPrice(dtoRestProductIU != null ? dtoRestProductIU.getPrice() : null, true));
        priceInfo.setDiscountPrice(new DiscountPrice(dtoRestProductIU != null ? dtoRestProductIU.getPrice() : null, true));

        priceInfoList.add(priceInfo);
        return priceInfoList;
    }

    private DtoProduct getDtoProducts(Product product, String action) {
        DtoProduct dtoProduct = new DtoProduct();
        dtoProduct.setR2sProductId(product.getR2sProductId());
        dtoProduct.setStatus(action);

        return dtoProduct;
    }

    private String setSerializedObject(ProductObject productObject) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(productObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void webhook(DtoWebhookIU webhookData) {
        if (Objects.isNull(webhookData.getError()) && webhookData.getType().equals("SaveProductResponse")) {
            List<Product> productList = productRepository.findByR2sProductIdAndCorrelationId(webhookData.getWebhookPayload().getMerchantProductId(), webhookData.getCorrelationId());
            if (!productList.isEmpty()) {
                productList.forEach(product -> product.setStatus("success"));
                productRepository.saveAll(productList);
            }
        } else {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "webhook not found"));
        }
    }
}