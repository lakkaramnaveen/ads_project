package com.walmart.ads.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdResponseDTO {
    private String adId;
    private String campaignId;
    private String creativeUrl;
    private String landingPageUrl;
    private String trackingPixelUrl;
    private Integer width;
    private Integer height;
    private String adType; // BANNER, VIDEO, NATIVE
    private Long price; // CPM in micros
}
