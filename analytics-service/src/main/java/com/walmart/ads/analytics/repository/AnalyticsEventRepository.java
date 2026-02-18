package com.walmart.ads.analytics.repository;

import com.walmart.ads.analytics.model.AnalyticsEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsEventRepository extends MongoRepository<AnalyticsEvent, String> {
    List<AnalyticsEvent> findByCampaignIdAndTimestampBetween(
            String campaignId, LocalDateTime start, LocalDateTime end);
    
    List<AnalyticsEvent> findByEventTypeAndTimestampBetween(
            String eventType, LocalDateTime start, LocalDateTime end);
    
    List<AnalyticsEvent> findByUserId(String userId);
}
