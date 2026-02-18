package com.walmart.ads.common.dto;

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

    public AdResponseDTO() {}

    public AdResponseDTO(
            String adId,
            String campaignId,
            String creativeUrl,
            String landingPageUrl,
            String trackingPixelUrl,
            Integer width,
            Integer height,
            String adType,
            Long price
    ) {
        this.adId = adId;
        this.campaignId = campaignId;
        this.creativeUrl = creativeUrl;
        this.landingPageUrl = landingPageUrl;
        this.trackingPixelUrl = trackingPixelUrl;
        this.width = width;
        this.height = height;
        this.adType = adType;
        this.price = price;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCreativeUrl() {
        return creativeUrl;
    }

    public void setCreativeUrl(String creativeUrl) {
        this.creativeUrl = creativeUrl;
    }

    public String getLandingPageUrl() {
        return landingPageUrl;
    }

    public void setLandingPageUrl(String landingPageUrl) {
        this.landingPageUrl = landingPageUrl;
    }

    public String getTrackingPixelUrl() {
        return trackingPixelUrl;
    }

    public void setTrackingPixelUrl(String trackingPixelUrl) {
        this.trackingPixelUrl = trackingPixelUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
