package com.walmart.ads.analytics.service;

import com.walmart.ads.analytics.model.AnalyticsEvent;
import com.walmart.ads.analytics.repository.AnalyticsEventRepository;
import com.walmart.ads.common.dto.AnalyticsEventDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final AnalyticsEventRepository eventRepository;

    public AnalyticsService(AnalyticsEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @KafkaListener(topics = "analytics-events", groupId = "analytics-service")
    public void consumeAnalyticsEvent(Map<String, Object> eventData) {
        try {
            AnalyticsEvent event = new AnalyticsEvent();
            event.setId(java.util.UUID.randomUUID().toString());
            event.setEventType((String) eventData.get("eventType"));
            event.setAdId((String) eventData.get("adId"));
            event.setCampaignId((String) eventData.get("campaignId"));
            event.setUserId((String) eventData.get("userId"));
            event.setRequestId((String) eventData.get("requestId"));
            event.setTimestamp(LocalDateTime.parse((String) eventData.get("timestamp")));
            event.setUserAgent((String) eventData.get("userAgent"));
            event.setIpAddress((String) eventData.get("ipAddress"));
            
            eventRepository.save(event);
            log.debug("Analytics event saved: {}", event.getId());
        } catch (Exception e) {
            log.error("Error processing analytics event", e);
        }
    }
    
    public AnalyticsEvent trackEvent(AnalyticsEventDTO eventDTO) {
        AnalyticsEvent event = convertToEntity(eventDTO);
        event = eventRepository.save(event);
        log.info("Event tracked: {} for campaign: {}", event.getEventType(), event.getCampaignId());
        return event;
    }
    
    public Map<String, Object> getCampaignReport(String campaignId, LocalDateTime startDate, LocalDateTime endDate) {
        List<AnalyticsEvent> events = eventRepository.findByCampaignIdAndTimestampBetween(
                campaignId, startDate, endDate);
        
        long impressions = events.stream()
                .filter(e -> "IMPRESSION".equals(e.getEventType()))
                .count();
        
        long clicks = events.stream()
                .filter(e -> "CLICK".equals(e.getEventType()))
                .count();
        
        long conversions = events.stream()
                .filter(e -> "CONVERSION".equals(e.getEventType()))
                .count();
        
        double ctr = impressions > 0 ? (double) clicks / impressions * 100 : 0;
        double conversionRate = clicks > 0 ? (double) conversions / clicks * 100 : 0;
        
        Map<String, Object> report = new HashMap<>();
        report.put("campaignId", campaignId);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        report.put("impressions", impressions);
        report.put("clicks", clicks);
        report.put("conversions", conversions);
        report.put("ctr", String.format("%.2f%%", ctr));
        report.put("conversionRate", String.format("%.2f%%", conversionRate));
        
        return report;
    }
    
    private AnalyticsEvent convertToEntity(AnalyticsEventDTO dto) {
        AnalyticsEvent event = new AnalyticsEvent();
        event.setId(java.util.UUID.randomUUID().toString());
        event.setEventType(dto.getEventType());
        event.setAdId(dto.getAdId());
        event.setCampaignId(dto.getCampaignId());
        event.setUserId(dto.getUserId());
        event.setRequestId(dto.getRequestId());
        event.setTimestamp(dto.getTimestamp());
        event.setUserAgent(dto.getUserAgent());
        event.setIpAddress(dto.getIpAddress());
        event.setMetadata(dto.getMetadata());
        return event;
    }
}
