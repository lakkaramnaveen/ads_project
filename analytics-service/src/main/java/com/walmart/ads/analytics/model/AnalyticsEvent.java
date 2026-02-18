package com.walmart.ads.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "analytics_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
