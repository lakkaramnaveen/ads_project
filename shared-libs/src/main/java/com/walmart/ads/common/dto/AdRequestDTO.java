package com.walmart.ads.common.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public class AdRequestDTO {
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Request ID is required")
    private String requestId;
    
    private String pageUrl;
    private String userAgent;
    private String ipAddress;
    private Map<String, String> contextData; // Additional context for targeting
    private Integer slotWidth;
    private Integer slotHeight;

    public AdRequestDTO() {}

    public AdRequestDTO(
            String userId,
            String requestId,
            String pageUrl,
            String userAgent,
            String ipAddress,
            Map<String, String> contextData,
            Integer slotWidth,
            Integer slotHeight
    ) {
        this.userId = userId;
        this.requestId = requestId;
        this.pageUrl = pageUrl;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.contextData = contextData;
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
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

    public Map<String, String> getContextData() {
        return contextData;
    }

    public void setContextData(Map<String, String> contextData) {
        this.contextData = contextData;
    }

    public Integer getSlotWidth() {
        return slotWidth;
    }

    public void setSlotWidth(Integer slotWidth) {
        this.slotWidth = slotWidth;
    }

    public Integer getSlotHeight() {
        return slotHeight;
    }

    public void setSlotHeight(Integer slotHeight) {
        this.slotHeight = slotHeight;
    }
}
