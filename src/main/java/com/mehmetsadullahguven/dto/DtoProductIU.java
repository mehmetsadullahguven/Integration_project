package com.mehmetsadullahguven.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mehmetsadullahguven.enums.CurrencyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class DtoProductIU extends DtoBase {

    @JsonProperty("r2s_product_id")
    private String r2sProductId;

    @JsonProperty("r2s_product_list_id")
    private String r2sProductListId;

    @JsonProperty("supplier_id")
    private String supplierId;

    private String type;

    private Integer active;

    private String sku;

    private String barcode;

    @JsonProperty("currency_code")
    @Enumerated(EnumType.STRING)
    @NotNull
    private CurrencyType currencyCode;

    @JsonProperty("exchange_rates")
    private Map<String,Double> exchangeRates;

    @JsonProperty("is_adult")
    private Integer isAdult;

    private Float price;

    @JsonProperty("buyingPrice")
    private Float buying_price;

    @JsonProperty("costPrice")
    private Float cost_price;

    private String brand;

    private Integer quantity;

    private String manufacture;

    @JsonProperty("tax_rate")
    private Integer taxRate;

    @JsonProperty("model_number")
    private String modelNumber;

    private DtoSizeIU size;

    private Integer weight;

    private List<DtoCombineIU> combines;

    private List<DtoGtipIU> gtip;

    @JsonProperty("changed_properties")
    private DtoChangedPropertiesIU changedProperties;

    private Object store;

    private List<DtoTranslationIU> translations;

    @JsonProperty("default_shipping_prices")
    private Map<String,String> defaultShippingPrices;

    private DtoCategoryIU category;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty("updated_at")
    private Date updatedAt;

    @JsonProperty("base_url")
    private String baseUrl;

    @JsonProperty("access_token")
    private String accessToken;
}
