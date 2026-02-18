package com.walmart.ads.adserving.service;

import com.walmart.ads.adserving.client.CampaignServiceClient;
import com.walmart.ads.adserving.client.UserTargetingServiceClient;
import com.walmart.ads.adserving.model.AdImpression;
import com.walmart.ads.adserving.repository.AdImpressionRepository;
import com.walmart.ads.common.dto.AdRequestDTO;
import com.walmart.ads.common.dto.AdResponseDTO;
import com.walmart.ads.common.dto.CampaignDTO;
import com.walmart.ads.common.dto.UserProfileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AdServingService {

    private static final Logger log = LoggerFactory.getLogger(AdServingService.class);

    private final CampaignServiceClient campaignServiceClient;
    private final UserTargetingServiceClient userTargetingServiceClient;
    private final AdImpressionRepository impressionRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Random random = new Random();

    public AdServingService(
            CampaignServiceClient campaignServiceClient,
            UserTargetingServiceClient userTargetingServiceClient,
            AdImpressionRepository impressionRepository,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.campaignServiceClient = campaignServiceClient;
        this.userTargetingServiceClient = userTargetingServiceClient;
        this.impressionRepository = impressionRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Core ad serving logic - optimized for low latency
     * This method handles 900M+ user requests and must be highly performant
     */
    public AdResponseDTO serveAd(AdRequestDTO adRequest) {
        long startTime = System.currentTimeMillis();
        
        try {
            // Step 1: Get active campaigns (cached)
            List<CampaignDTO> activeCampaigns = getActiveCampaigns();
            
            if (activeCampaigns.isEmpty()) {
                log.warn("No active campaigns available for user: {}", adRequest.getUserId());
                return null;
            }
            
            // Step 2: Get user profile for targeting (cached)
            UserProfileDTO userProfile = getUserProfile(adRequest.getUserId());
            
            // Step 3: Match campaigns based on targeting criteria
            List<CampaignDTO> matchedCampaigns = matchCampaigns(activeCampaigns, userProfile, adRequest);
            
            if (matchedCampaigns.isEmpty()) {
                log.debug("No matching campaigns for user: {}", adRequest.getUserId());
                return null;
            }
            
            // Step 4: Select best ad (simplified - in production use auction logic)
            CampaignDTO selectedCampaign = selectBestAd(matchedCampaigns);
            
            // Step 5: Create ad response
            AdResponseDTO adResponse = createAdResponse(selectedCampaign, adRequest);
            
            // Step 6: Record impression asynchronously (non-blocking)
            recordImpressionAsync(selectedCampaign, adRequest);
            
            // Step 7: Publish analytics event to Kafka
            publishAnalyticsEvent(selectedCampaign, adRequest, "IMPRESSION");
            
            long latency = System.currentTimeMillis() - startTime;
            log.info("Ad served in {}ms for user: {}, campaign: {}", 
                    latency, adRequest.getUserId(), selectedCampaign.getId());
            
            return adResponse;
            
        } catch (Exception e) {
            log.error("Error serving ad for user: {}", adRequest.getUserId(), e);
            throw new RuntimeException("Failed to serve ad", e);
        }
    }
    
    @Cacheable(value = "activeCampaigns", unless = "#result.isEmpty()")
    private List<CampaignDTO> getActiveCampaigns() {
        return campaignServiceClient.getActiveCampaigns();
    }
    
    @Cacheable(value = "userProfiles", key = "#userId")
    private UserProfileDTO getUserProfile(String userId) {
        try {
            return userTargetingServiceClient.getUserProfile(userId);
        } catch (Exception e) {
            log.warn("Could not fetch user profile for: {}", userId, e);
            return null; // Return null if user not found - continue with default targeting
        }
    }
    
    private List<CampaignDTO> matchCampaigns(List<CampaignDTO> campaigns, 
                                             UserProfileDTO userProfile, 
                                             AdRequestDTO adRequest) {
        return campaigns.stream()
                .filter(campaign -> matchesTargeting(campaign, userProfile, adRequest))
                .collect(Collectors.toList());
    }
    
    private boolean matchesTargeting(CampaignDTO campaign, UserProfileDTO userProfile, AdRequestDTO adRequest) {
        // Simplified targeting logic
        if (campaign.getTargetSegments() == null || campaign.getTargetSegments().isEmpty()) {
            return true; // No targeting = show to all
        }
        
        if (userProfile == null || userProfile.getSegments() == null) {
            return false; // No user segments = no match
        }
        
        // Check if user segments intersect with campaign target segments
        return campaign.getTargetSegments().stream()
                .anyMatch(segment -> userProfile.getSegments().contains(segment));
    }
    
    private CampaignDTO selectBestAd(List<CampaignDTO> campaigns) {
        // Simplified selection - in production, implement real-time bidding (RTB) auction
        // For now, randomly select from matched campaigns
        return campaigns.get(random.nextInt(campaigns.size()));
    }
    
    private AdResponseDTO createAdResponse(CampaignDTO campaign, AdRequestDTO adRequest) {
        AdResponseDTO response = new AdResponseDTO();
        response.setAdId("ad-" + campaign.getId());
        response.setCampaignId(campaign.getId().toString());
        response.setCreativeUrl(campaign.getCreativeUrl());
        response.setLandingPageUrl(campaign.getLandingPageUrl());
        response.setTrackingPixelUrl("/api/analytics/track?adId=" + campaign.getId());
        response.setWidth(adRequest.getSlotWidth() != null ? adRequest.getSlotWidth() : 300);
        response.setHeight(adRequest.getSlotHeight() != null ? adRequest.getSlotHeight() : 250);
        response.setAdType("BANNER");
        response.setPrice(1000000L); // $1 CPM in micros
        return response;
    }
    
    private void recordImpressionAsync(CampaignDTO campaign, AdRequestDTO adRequest) {
        // Save to Cassandra for high-volume writes
        AdImpression impression = new AdImpression(
                campaign.getId().toString(),
                campaign.getId().toString(),
                adRequest.getUserId(),
                adRequest.getRequestId()
        );
        impression.setUserAgent(adRequest.getUserAgent());
        impression.setIpAddress(adRequest.getIpAddress());
        
        // Async save to avoid blocking
        new Thread(() -> {
            try {
                impressionRepository.save(impression);
            } catch (Exception e) {
                log.error("Failed to save impression", e);
            }
        }).start();
    }
    
    private void publishAnalyticsEvent(CampaignDTO campaign, AdRequestDTO adRequest, String eventType) {
        try {
            // Publish to Kafka for analytics processing
            kafkaTemplate.send("analytics-events", eventType, Map.of(
                    "eventType", eventType,
                    "adId", campaign.getId().toString(),
                    "campaignId", campaign.getId().toString(),
                    "userId", adRequest.getUserId(),
                    "requestId", adRequest.getRequestId(),
                    "timestamp", java.time.LocalDateTime.now().toString()
            ));
        } catch (Exception e) {
            log.error("Failed to publish analytics event", e);
        }
    }
}
