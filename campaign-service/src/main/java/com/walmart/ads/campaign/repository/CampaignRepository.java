package com.walmart.ads.campaign.repository;

import com.walmart.ads.campaign.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByAdvertiserId(String advertiserId);
    
    List<Campaign> findByStatus(Campaign.CampaignStatus status);
    
    @Query("SELECT c FROM Campaign c WHERE c.status = 'ACTIVE' " +
           "AND c.startDate <= :now AND c.endDate >= :now")
    List<Campaign> findActiveCampaigns(LocalDateTime now);
    
    List<Campaign> findByAdvertiserIdAndStatus(String advertiserId, Campaign.CampaignStatus status);
}
