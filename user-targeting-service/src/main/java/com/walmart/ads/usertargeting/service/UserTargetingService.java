package com.walmart.ads.usertargeting.service;

import com.walmart.ads.common.dto.UserProfileDTO;
import com.walmart.ads.usertargeting.exception.UserProfileNotFoundException;
import com.walmart.ads.usertargeting.model.UserProfile;
import com.walmart.ads.usertargeting.repository.UserProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserTargetingService {

    private static final Logger log = LoggerFactory.getLogger(UserTargetingService.class);

    private final UserProfileRepository userProfileRepository;

    public UserTargetingService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Cacheable(value = "userProfiles", key = "#root.args[0]")
    public UserProfileDTO getUserProfile(String userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        return convertToDTO(profile);
    }
    
    public List<String> getUserSegments(String userId) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new UserProfileNotFoundException(userId));
        return profile.getSegments() != null ? profile.getSegments() : new ArrayList<>();
    }
    
    public UserProfileDTO updateUserProfile(String userId, UserProfileDTO profileDTO) {
        UserProfile profile = userProfileRepository.findById(userId)
                .orElse(new UserProfile());
        
        profile.setUserId(userId);
        profile.setEmail(profileDTO.getEmail());
        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName(profileDTO.getLastName());
        profile.setAge(profileDTO.getAge());
        profile.setGender(profileDTO.getGender());
        profile.setLocation(profileDTO.getLocation());
        profile.setInterests(profileDTO.getInterests());
        profile.setSegments(profileDTO.getSegments());
        profile.setDemographics(profileDTO.getDemographics());
        profile.setBehaviorData(profileDTO.getBehaviorData());
        profile.setLastUpdated(LocalDateTime.now());
        
        profile = userProfileRepository.save(profile);
        log.info("User profile updated: {}", userId);
        
        return convertToDTO(profile);
    }
    
    @KafkaListener(topics = "user-events", groupId = "user-targeting-service")
    public void processUserEvent(Map<String, Object> eventData) {
        try {
            String userId = (String) eventData.get("userId");
            String eventType = (String) eventData.get("eventType");
            
            // Update user segments based on behavior events
            UserProfile profile = userProfileRepository.findById(userId).orElse(null);
            if (profile != null) {
                // Update segments based on behavior (simplified logic)
                updateUserSegments(profile, eventType, eventData);
                userProfileRepository.save(profile);
            }
        } catch (Exception e) {
            log.error("Error processing user event", e);
        }
    }
    
    private void updateUserSegments(UserProfile profile, String eventType, Map<String, Object> eventData) {
        // Simplified segment update logic
        // In production, implement sophisticated ML-based segmentation
        List<String> segments = profile.getSegments() != null ? 
                new ArrayList<>(profile.getSegments()) : new ArrayList<>();
        
        // Example: Add segments based on behavior
        if ("PURCHASE".equals(eventType)) {
            if (!segments.contains("PURCHASER")) {
                segments.add("PURCHASER");
            }
        }
        
        profile.setSegments(segments);
    }
    
    private UserProfileDTO convertToDTO(UserProfile profile) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(profile.getUserId());
        dto.setEmail(profile.getEmail());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setAge(profile.getAge());
        dto.setGender(profile.getGender());
        dto.setLocation(profile.getLocation());
        dto.setInterests(profile.getInterests());
        dto.setSegments(profile.getSegments());
        dto.setDemographics(profile.getDemographics());
        dto.setBehaviorData(profile.getBehaviorData());
        dto.setLastUpdated(profile.getLastUpdated());
        return dto;
    }
}
