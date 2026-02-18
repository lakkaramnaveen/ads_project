package com.walmart.ads.common.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CampaignDTO {
    private Long id;
    
    @NotBlank(message = "Campaign name is required")
    @Size(max = 255, message = "Campaign name must not exceed 255 characters")
    private String name;
    
    @NotBlank(message = "Advertiser ID is required")
    private String advertiserId;
    
    @NotNull(message = "Budget is required")
    @DecimalMin(value = "0.01", message = "Budget must be greater than 0")
    private BigDecimal budget;
    
    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;
    
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
    
    @NotNull(message = "Status is required")
    private CampaignStatus status;
    
    private List<String> targetSegments;
    private List<String> targetGeographies;
    private String creativeUrl;
    private String landingPageUrl;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CampaignDTO() {}

    public CampaignDTO(
            Long id,
            String name,
            String advertiserId,
            BigDecimal budget,
            LocalDateTime startDate,
            LocalDateTime endDate,
            CampaignStatus status,
            List<String> targetSegments,
            List<String> targetGeographies,
            String creativeUrl,
            String landingPageUrl,
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
        this.targetSegments = targetSegments;
        this.targetGeographies = targetGeographies;
        this.creativeUrl = creativeUrl;
        this.landingPageUrl = landingPageUrl;
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

    public List<String> getTargetSegments() {
        return targetSegments;
    }

    public void setTargetSegments(List<String> targetSegments) {
        this.targetSegments = targetSegments;
    }

    public List<String> getTargetGeographies() {
        return targetGeographies;
    }

    public void setTargetGeographies(List<String> targetGeographies) {
        this.targetGeographies = targetGeographies;
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

    public enum CampaignStatus {
        DRAFT, ACTIVE, PAUSED, COMPLETED, ARCHIVED
    }
}
