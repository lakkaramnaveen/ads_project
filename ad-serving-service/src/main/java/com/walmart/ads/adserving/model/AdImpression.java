package com.walmart.ads.adserving.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("ad_impressions")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    
    public AdImpression(String adId, String campaignId, String userId, String requestId) {
        this.id = UUID.randomUUID();
        this.adId = adId;
        this.campaignId = campaignId;
        this.userId = userId;
        this.requestId = requestId;
        this.timestamp = LocalDateTime.now();
    }
}
