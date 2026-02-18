package com.walmart.ads.campaign.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaigns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 255)
    private String name;
    
    @Column(nullable = false, name = "advertiser_id")
    private String advertiserId;
    
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal budget;
    
    @Column(nullable = false, name = "start_date")
    private LocalDateTime startDate;
    
    @Column(nullable = false, name = "end_date")
    private LocalDateTime endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampaignStatus status;
    
    @Column(name = "creative_url", length = 500)
    private String creativeUrl;
    
    @Column(name = "landing_page_url", length = 500)
    private String landingPageUrl;
    
    @Column(name = "target_segments", columnDefinition = "TEXT")
    private String targetSegments; // JSON array stored as text
    
    @Column(name = "target_geographies", columnDefinition = "TEXT")
    private String targetGeographies; // JSON array stored as text
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum CampaignStatus {
        DRAFT, ACTIVE, PAUSED, COMPLETED, ARCHIVED
    }
}
