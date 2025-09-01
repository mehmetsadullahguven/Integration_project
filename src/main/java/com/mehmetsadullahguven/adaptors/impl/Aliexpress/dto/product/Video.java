package com.mehmetsadullahguven.adaptors.impl.Aliexpress.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Video {

    @JsonProperty("preview_url")
    private String previewUrl;

    @JsonProperty("video_url")
    private String videoUrl;
}
