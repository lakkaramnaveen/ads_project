package com.walmart.ads.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
