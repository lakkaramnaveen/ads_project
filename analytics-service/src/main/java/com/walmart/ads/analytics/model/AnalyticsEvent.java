package com.walmart.ads.analytics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "analytics_events")
public class AnalyticsEvent {
    @Id
    private String id;
    private String eventType; // IMPRESSION, CLICK, CONVERSION
    private String adId;
    private String campaignId;
    private String userId;
    private String requestId;
    private LocalDateTime timestamp;
    private String userAgent;
    private String ipAddress;
    private Map<String, String> metadata;

    public AnalyticsEvent() {}

    public AnalyticsEvent(
            String id,
            String eventType,
            String adId,
            String campaignId,
            String userId,
            String requestId,
            LocalDateTime timestamp,
            String userAgent,
            String ipAddress,
            Map<String, String> metadata
    ) {
        this.id = id;
        this.eventType = eventType;
        this.adId = adId;
        this.campaignId = campaignId;
        this.userId = userId;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
        this.metadata = metadata;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
