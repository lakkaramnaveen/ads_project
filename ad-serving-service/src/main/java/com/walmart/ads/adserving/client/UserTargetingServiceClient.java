package com.walmart.ads.adserving.client;

import com.walmart.ads.common.dto.UserProfileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-targeting-service")
public interface UserTargetingServiceClient {
    @GetMapping("/api/users/{userId}/profile")
    UserProfileDTO getUserProfile(@PathVariable String userId);
    
    @GetMapping("/api/users/{userId}/segments")
    List<String> getUserSegments(@PathVariable String userId);
}
