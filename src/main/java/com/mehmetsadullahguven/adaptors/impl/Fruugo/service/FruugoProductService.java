package com.mehmetsadullahguven.adaptors.impl.Fruugo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehmetsadullahguven.adaptors.impl.AbstractAdaptor;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.AbstractFruugo;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.dto.product.*;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.CodeType;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.Currency;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.Language;
import com.mehmetsadullahguven.adaptors.impl.Fruugo.enums.StockStatus;
import com.mehmetsadullahguven.dto.*;
import com.mehmetsadullahguven.enums.LanguageType;
import com.mehmetsadullahguven.exception.BaseException;
import com.mehmetsadullahguven.exception.ErrorMessage;
import com.mehmetsadullahguven.exception.ErrorMessageType;
import com.mehmetsadullahguven.model.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FruugoProductService extends AbstractFruugo {


    public DtoProduct create(DtoProductIU dtoProductIU) {
        return upsert(dtoProductIU);
    }

    public DtoProduct update(DtoProductIU dtoProductIU) {
        return upsert(dtoProductIU);
    }

    public DtoProduct delete(DtoProductIU dtoProductIU) {
        ProductObject productObject = handleProduct(dtoProductIU, "delete");

        Product savedproduct = saveProduct(dtoProductIU, setSerializedObject(productObject), "delete_bulk_waiting");

        return getDtoProducts(savedproduct, "delete");
    }

    private DtoProduct upsert(DtoProductIU dtoProductIU) {

        Optional<Product> optionalProduct = productRepository.findByR2sProductIdAndR2sProductListId(dtoProductIU.getR2sProductId(), dtoProductIU.getR2sProductListId());
        return optionalProduct.map(product -> updateProduct(dtoProductIU, product)).orElseGet(() -> createProduct(dtoProductIU));
    }

    private DtoProduct createProduct(DtoProductIU dtoProductIU) {
        ProductObject productObject = handleProduct(dtoProductIU, "create");

        Product savedproduct = saveProduct(dtoProductIU, setSerializedObject(productObject), "create_bulk_waiting");

        return getDtoProducts(savedproduct, "create");
    }

    private DtoProduct updateProduct(DtoProductIU dtoProductIU, Product product) {
        if (Objects.equals(product.getClientProductId(), null)) {
            throw new BaseException(new ErrorMessage(ErrorMessageType.GENERAL_EXIST, "clientProductID not found"));
        }

        if (dtoProductIU.getChangedProperties().getQuantity() > 0) {
            Map<String, DtoChangedCombineIU> combines = dtoProductIU.getChangedProperties().getCombines();
            if (!combines.isEmpty()) {
                List<DtoCombineIU> dtoCombineIUList = new ArrayList<>();
                combines.forEach((key, value) -> {
                    DtoCombineIU dtoCombineIU = dtoProductIU.getCombines().get(Integer.parseInt(key));
                    dtoCombineIU.setQuantity(value.getQuantity());
                    dtoCombineIUList.add(dtoCombineIU);
                });
                dtoProductIU.setCombines(dtoCombineIUList);
            }
            ProductObject productObject = handleProduct(dtoProductIU, "patch");
            Product savedproduct = saveProduct(dtoProductIU, setSerializedObject(productObject), "patch_bulk_waiting");

            return getDtoProducts(savedproduct, "patch");
        } else {
            ProductObject productObject = handleProduct(dtoProductIU, "update");
            Product savedproduct = saveProduct(dtoProductIU, setSerializedObject(productObject), "update_bulk_waiting");

            return getDtoProducts(savedproduct, "update");
        }
    }

    private ProductObject handleProduct(DtoProductIU dtoProductIU, String operation) {

        ProductType productType = new ProductType();
        productType.setProductId(dtoProductIU.getR2sProductId());
        List<Sku> skus = new ArrayList<>();

        if (!"delete".equals(operation) && !"patch".equals(operation)) {
            DtoCategoryTranslationIU dtoCategoryTranslationIU = getTranslation(dtoProductIU.getCategory().getTranslations());

            productType.setBrand(dtoProductIU.getBrand());
            productType.setCategory(dtoCategoryTranslationIU.getSource());
            productType.setManufacturer(dtoProductIU.getManufacture());
            if (!dtoProductIU.getCombines().isEmpty()) {
                dtoProductIU.getCombines().forEach(dtoCombineIU -> skus.add(combineHandle(dtoCombineIU, dtoProductIU)));
            } else {
                skus.add(notNullCombineHandle(dtoProductIU));
            }
        } else {
            if (!dtoProductIU.getCombines().isEmpty()) {
                dtoProductIU.getCombines().forEach(dtoCombineIU -> skus.add(patchCombineHandle(dtoCombineIU, dtoProductIU, operation)));
            } else {
                skus.add(notNullPatchCombineHandle(dtoProductIU, operation));
            }
        }
        ProductObject productObject = new ProductObject();
        productObject.setProduct(productType);
        productObject.setSkus(skus);

        return productObject;
    }

    private Sku combineHandle(DtoCombineIU dtoCombineIU, DtoProductIU dtoProductIU) {

        DtoTranslationIU dtoTranslationIU = getTranslation(dtoProductIU.getTranslations());
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
        supplyInfo.setStockStatus(dtoCombineIU.getQuantity() > 0 && dtoProductIU.getActive().equals(1) ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);

        Sku sku = new Sku();
        sku.setSkuId(dtoCombineIU.getCombineId());
        sku.setGtins(gtinList);
        sku.setDetails(detail);
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoProductIU, dtoCombineIU));
        sku.setPackageWeight(dtoProductIU.getWeight());
        sku.setVolume(getVolume(dtoProductIU.getSize()));
        return sku;
    }

    private Sku notNullCombineHandle(DtoProductIU dtoProductIU) {

        int i = 0;
        DtoTranslationIU dtoTranslationIU = getTranslation(dtoProductIU.getTranslations());

        List<Media> mediaList = getImages(dtoTranslationIU.getImages());

        List<Gtin> gtinList = new ArrayList<>();
        Gtin gtin = new Gtin(CodeType.OTHER, dtoProductIU.getR2sProductId() + "_" + i);
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
        supplyInfo.setStockQuantity(dtoProductIU.getQuantity());
        supplyInfo.setStockStatus(dtoProductIU.getQuantity() > 0 && dtoProductIU.getActive().equals(1) ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);


        Sku sku = new Sku();
        sku.setSkuId(dtoProductIU.getR2sProductId() + "_" + 0);
        sku.setGtins(gtinList);
        sku.setDetails(detail);
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoProductIU, null));
        sku.setPackageWeight(dtoProductIU.getWeight());
        sku.setVolume(getVolume(dtoProductIU.getSize()));

        return sku;

    }

    private Sku patchCombineHandle(DtoCombineIU dtoCombineIU, DtoProductIU dtoProductIU, String operation) {
        SupplyInfo supplyInfo = new SupplyInfo();
        if (operation.equals("delete")) {
            supplyInfo.setStockStatus(StockStatus.NOTAVAILABLE);
            supplyInfo.setStockQuantity(0);
        } else {
            supplyInfo.setStockStatus(dtoCombineIU.getQuantity() > 0 || dtoProductIU.getActive() == 1 ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);
            supplyInfo.setStockQuantity(dtoCombineIU.getQuantity());
        }

        Sku sku = new Sku();
        sku.setSkuId(dtoCombineIU.getCombineId());
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoProductIU, dtoCombineIU));
        return sku;
    }

    private Sku notNullPatchCombineHandle(DtoProductIU dtoProductIU, String operation) {
        SupplyInfo supplyInfo = new SupplyInfo();
        if (operation.equals("delete")) {
            supplyInfo.setStockStatus(StockStatus.NOTAVAILABLE);
            supplyInfo.setStockQuantity(0);
        } else {
            supplyInfo.setStockStatus(dtoProductIU.getQuantity() > 0 || dtoProductIU.getActive() == 1 ? StockStatus.INSTOCK : StockStatus.OUTOFSTOCK);
            supplyInfo.setStockQuantity(dtoProductIU.getQuantity());
        }

        Sku sku = new Sku();
        sku.setSkuId(dtoProductIU.getR2sProductId() + "_" + 0);
        sku.setSupplyInfo(supplyInfo);
        sku.setPricingInfo(getPriceList(dtoProductIU, null));
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

    private List<PriceInfo> getPriceList(DtoProductIU dtoProductIU, DtoCombineIU dtoCombineIU) {
        List<PriceInfo> priceInfoList = new ArrayList<>();
        if (dtoProductIU != null) {
            if (dtoCombineIU.getPriceDiff() > 0) {
                dtoProductIU.setPrice(dtoCombineIU.getPriceDiff() + dtoProductIU.getPrice());
            }
        }

        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setCurrency(Currency.valueOf(dtoProductIU != null ? dtoProductIU.getCurrencyCode().name() : null));
        priceInfo.setVatRate(dtoProductIU != null ? dtoProductIU.getTaxRate().floatValue() : 0);
        priceInfo.setNormalPrice(new NormalPrice(dtoProductIU != null ? dtoProductIU.getPrice() : null, true));
        priceInfo.setDiscountPrice(new DiscountPrice(dtoProductIU != null ? dtoProductIU.getPrice() : null, true));

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