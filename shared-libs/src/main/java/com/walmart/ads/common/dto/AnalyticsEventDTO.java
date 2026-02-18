package com.walmart.ads.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
