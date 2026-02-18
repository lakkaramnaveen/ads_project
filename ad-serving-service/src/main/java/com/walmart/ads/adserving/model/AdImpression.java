package com.walmart.ads.adserving.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("ad_impressions")
public class AdImpression {
    @PrimaryKey
    private UUID id;
    private String adId;
    private String campaignId;
    private String userId;
    private String requestId;
    private LocalDateTime timestamp;
    private String userAgent;
    private String ipAddress;
    
    public AdImpression() {}

    public AdImpression(
            UUID id,
            String adId,
            String campaignId,
            String userId,
            String requestId,
            LocalDateTime timestamp,
            String userAgent,
            String ipAddress
    ) {
        this.id = id;
        this.adId = adId;
        this.campaignId = campaignId;
        this.userId = userId;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
    }

    public AdImpression(String adId, String campaignId, String userId, String requestId) {
        this.id = UUID.randomUUID();
        this.adId = adId;
        this.campaignId = campaignId;
        this.userId = userId;
        this.requestId = requestId;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
}
