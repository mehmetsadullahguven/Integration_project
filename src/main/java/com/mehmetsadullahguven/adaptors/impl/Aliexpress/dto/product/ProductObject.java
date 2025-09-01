package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mehmetsadullahguven.adaptors.impl.Aliexpress.enums.Language;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductObject {

    @JsonProperty("aliexpress_category_id")
    private Integer aliexpressCategoryId;

    @JsonProperty("external_id")
    private String externalId;

    @JsonProperty("attribute_list")
    private List<AttributeList> attributeList;

    @JsonProperty("freight_template_id")
    private Long freightTemplateId;

    @JsonProperty("language")
    private Language language;

    @JsonProperty("main_image_urls_list")
    private List<String> mainImageUrlsList;

    @JsonProperty("multi_language_description_list")
    private List<MultiLanguageDescriptionList> multiLanguageDescriptionList;

    @JsonProperty("multi_language_subject_list")
    private List<MultiLanguageSubjectList> multiLanguageSubjectList;

    @JsonProperty("package_length")
    private Integer packageLength;

    @JsonProperty("package_width")
    private Integer packageWidth;

    @JsonProperty("package_height")
    private Integer packageHeight;

    @JsonProperty("weight")
    private String weight;

    @JsonProperty("shipping_lead_time")
    private Integer shippingLeadTime;

    @JsonProperty("product_unit")
    private Integer productUnit;

    @JsonProperty("bulk_discount")
    private Integer bulkDiscount;

    @JsonProperty("bulk_order")
    private Integer bulkOrder;

    @JsonProperty("sku_info_list")
    private List<SkuInfoList> skuInfoList;

    @JsonProperty("video")
    private Video video;

    @JsonProperty("hs_codes")
    private List<HsCodes> hsCodes;
}
