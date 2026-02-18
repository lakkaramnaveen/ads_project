package com.walmart.ads.adserving.controller;

import com.walmart.ads.common.dto.AdRequestDTO;
import com.walmart.ads.common.dto.AdResponseDTO;
import com.walmart.ads.common.dto.ApiResponse;
import com.walmart.ads.adserving.service.AdServingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ads")
@RequiredArgsConstructor
@Slf4j
public class AdServingController {
    
    private final AdServingService adServingService;
    
    @PostMapping("/serve")
    public ResponseEntity<ApiResponse<AdResponseDTO>> serveAd(@Valid @RequestBody AdRequestDTO adRequest) {
        log.info("Ad request received for user: {}, requestId: {}", 
                adRequest.getUserId(), adRequest.getRequestId());
        
        AdResponseDTO adResponse = adServingService.serveAd(adRequest);
        
        if (adResponse == null) {
            return ResponseEntity.ok(ApiResponse.error("No ad available", "NO_AD_AVAILABLE"));
        }
        
        return ResponseEntity.ok(ApiResponse.success("Ad served successfully", adResponse));
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Ad Serving Service is healthy");
    }
}
