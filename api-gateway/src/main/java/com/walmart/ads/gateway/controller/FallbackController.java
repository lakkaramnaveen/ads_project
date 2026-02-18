package com.walmart.ads.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {
    
    @GetMapping("/campaign")
    public ResponseEntity<Map<String, Object>> campaignFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Campaign service is temporarily unavailable");
        response.put("status", "SERVICE_UNAVAILABLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/ad")
    public ResponseEntity<Map<String, Object>> adServingFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ad serving service is temporarily unavailable");
        response.put("status", "SERVICE_UNAVAILABLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> analyticsFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Analytics service is temporarily unavailable");
        response.put("status", "SERVICE_UNAVAILABLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
    
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> userTargetingFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User targeting service is temporarily unavailable");
        response.put("status", "SERVICE_UNAVAILABLE");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
