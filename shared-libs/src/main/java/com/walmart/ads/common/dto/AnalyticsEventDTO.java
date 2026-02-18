package com.walmart.ads.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public class AnalyticsEventDTO {
    @NotBlank(message = "Event type is required")
    private String eventType; // IMPRESSION, CLICK, CONVERSION
    
    @NotBlank(message = "Ad ID is required")
    private String adId;
    
    @NotBlank(message = "Campaign ID is required")
    private String campaignId;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
    
    private String requestId;
    private String userAgent;
    private String ipAddress;
    private Map<String, String> metadata;

    public AnalyticsEventDTO() {}

    public AnalyticsEventDTO(
            String eventType,
            String adId,
            String campaignId,
            String userId,
            LocalDateTime timestamp,
            String requestId,
            String userAgent,
            String ipAddress,
            Map<String, String> metadata
    ) {
        this.eventType = eventType;
        this.adId = adId;
        this.campaignId = campaignId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.requestId = requestId;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.metadata = metadata;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
