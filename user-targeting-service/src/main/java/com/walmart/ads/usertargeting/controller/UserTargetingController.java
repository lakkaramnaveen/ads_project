package com.walmart.ads.usertargeting.controller;

import com.walmart.ads.common.dto.ApiResponse;
import com.walmart.ads.common.dto.UserProfileDTO;
import com.walmart.ads.usertargeting.service.UserTargetingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserTargetingController {
    
    private final UserTargetingService userTargetingService;

    public UserTargetingController(UserTargetingService userTargetingService) {
        this.userTargetingService = userTargetingService;
    }
    
    @GetMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getUserProfile(@PathVariable String userId) {
        UserProfileDTO profile = userTargetingService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(profile));
    }
    
    @GetMapping("/{userId}/segments")
    public ResponseEntity<ApiResponse<List<String>>> getUserSegments(@PathVariable String userId) {
        List<String> segments = userTargetingService.getUserSegments(userId);
        return ResponseEntity.ok(ApiResponse.success(segments));
    }
    
    @PutMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateUserProfile(
            @PathVariable String userId,
            @Valid @RequestBody UserProfileDTO profileDTO) {
        UserProfileDTO updated = userTargetingService.updateUserProfile(userId, profileDTO);
        return ResponseEntity.ok(ApiResponse.success("User profile updated successfully", updated));
    }
}
