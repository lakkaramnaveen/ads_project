package com.walmart.ads.campaign.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaigns")
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

    public Campaign() {}

    public Campaign(
            Long id,
            String name,
            String advertiserId,
            BigDecimal budget,
            LocalDateTime startDate,
            LocalDateTime endDate,
            CampaignStatus status,
            String creativeUrl,
            String landingPageUrl,
            String targetSegments,
            String targetGeographies,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.advertiserId = advertiserId;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.creativeUrl = creativeUrl;
        this.landingPageUrl = landingPageUrl;
        this.targetSegments = targetSegments;
        this.targetGeographies = targetGeographies;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(String advertiserId) {
        this.advertiserId = advertiserId;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public CampaignStatus getStatus() {
        return status;
    }

    public void setStatus(CampaignStatus status) {
        this.status = status;
    }

    public String getCreativeUrl() {
        return creativeUrl;
    }

    public void setCreativeUrl(String creativeUrl) {
        this.creativeUrl = creativeUrl;
    }

    public String getLandingPageUrl() {
        return landingPageUrl;
    }

    public void setLandingPageUrl(String landingPageUrl) {
        this.landingPageUrl = landingPageUrl;
    }

    public String getTargetSegments() {
        return targetSegments;
    }

    public void setTargetSegments(String targetSegments) {
        this.targetSegments = targetSegments;
    }

    public String getTargetGeographies() {
        return targetGeographies;
    }

    public void setTargetGeographies(String targetGeographies) {
        this.targetGeographies = targetGeographies;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
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
