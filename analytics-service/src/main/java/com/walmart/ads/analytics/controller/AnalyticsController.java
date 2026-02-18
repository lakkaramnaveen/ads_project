package com.walmart.ads.analytics.controller;

import com.walmart.ads.analytics.service.AnalyticsService;
import com.walmart.ads.common.dto.AnalyticsEventDTO;
import com.walmart.ads.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @PostMapping("/events")
    public ResponseEntity<ApiResponse<String>> trackEvent(@Valid @RequestBody AnalyticsEventDTO eventDTO) {
        analyticsService.trackEvent(eventDTO);
        return ResponseEntity.ok(ApiResponse.success("Event tracked successfully", null));
    }
    
    @GetMapping("/reports")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReport(
            @RequestParam String campaignId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Map<String, Object> report = analyticsService.getCampaignReport(campaignId, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }
}
