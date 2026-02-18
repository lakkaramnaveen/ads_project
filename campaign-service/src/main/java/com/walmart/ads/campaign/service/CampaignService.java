package com.walmart.ads.campaign.service;

import com.walmart.ads.campaign.model.Campaign;
import com.walmart.ads.campaign.repository.CampaignRepository;
import com.walmart.ads.common.dto.CampaignDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private static final Logger log = LoggerFactory.getLogger(CampaignService.class);

    private final CampaignRepository campaignRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CampaignService(CampaignRepository campaignRepository, KafkaTemplate<String, Object> kafkaTemplate) {
        this.campaignRepository = campaignRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public CampaignDTO createCampaign(CampaignDTO campaignDTO) {
        Campaign campaign = convertToEntity(campaignDTO);
        campaign = campaignRepository.save(campaign);
        
        // Publish event to Kafka for other services
        kafkaTemplate.send("campaign-events", "campaign.created", campaign);
        log.info("Campaign created: {}", campaign.getId());
        
        return convertToDTO(campaign);
    }
    
    public CampaignDTO getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found: " + id));
        return convertToDTO(campaign);
    }
    
    public List<CampaignDTO> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<CampaignDTO> getCampaignsByAdvertiser(String advertiserId) {
        return campaignRepository.findByAdvertiserId(advertiserId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<CampaignDTO> getActiveCampaigns() {
        return campaignRepository.findActiveCampaigns(java.time.LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CampaignDTO updateCampaign(Long id, CampaignDTO campaignDTO) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found: " + id));
        
        campaign.setName(campaignDTO.getName());
        campaign.setBudget(campaignDTO.getBudget());
        campaign.setStartDate(campaignDTO.getStartDate());
        campaign.setEndDate(campaignDTO.getEndDate());
        campaign.setStatus(Campaign.CampaignStatus.valueOf(campaignDTO.getStatus().name()));
        campaign.setCreativeUrl(campaignDTO.getCreativeUrl());
        campaign.setLandingPageUrl(campaignDTO.getLandingPageUrl());
        
        campaign = campaignRepository.save(campaign);
        kafkaTemplate.send("campaign-events", "campaign.updated", campaign);
        log.info("Campaign updated: {}", campaign.getId());
        
        return convertToDTO(campaign);
    }
    
    @Transactional
    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
        kafkaTemplate.send("campaign-events", "campaign.deleted", id);
        log.info("Campaign deleted: {}", id);
    }
    
    private Campaign convertToEntity(CampaignDTO dto) {
        Campaign campaign = new Campaign();
        campaign.setName(dto.getName());
        campaign.setAdvertiserId(dto.getAdvertiserId());
        campaign.setBudget(dto.getBudget());
        campaign.setStartDate(dto.getStartDate());
        campaign.setEndDate(dto.getEndDate());
        campaign.setStatus(Campaign.CampaignStatus.valueOf(dto.getStatus().name()));
        campaign.setCreativeUrl(dto.getCreativeUrl());
        campaign.setLandingPageUrl(dto.getLandingPageUrl());
        // Convert lists to JSON strings (simplified - in production use proper JSON handling)
        return campaign;
    }
    
    private CampaignDTO convertToDTO(Campaign campaign) {
        CampaignDTO dto = new CampaignDTO();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setAdvertiserId(campaign.getAdvertiserId());
        dto.setBudget(campaign.getBudget());
        dto.setStartDate(campaign.getStartDate());
        dto.setEndDate(campaign.getEndDate());
        dto.setStatus(CampaignDTO.CampaignStatus.valueOf(campaign.getStatus().name()));
        dto.setCreativeUrl(campaign.getCreativeUrl());
        dto.setLandingPageUrl(campaign.getLandingPageUrl());
        dto.setCreatedAt(campaign.getCreatedAt());
        dto.setUpdatedAt(campaign.getUpdatedAt());
        return dto;
    }
}
