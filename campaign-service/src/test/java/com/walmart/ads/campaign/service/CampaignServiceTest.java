package com.walmart.ads.campaign.service;

import com.walmart.ads.campaign.model.Campaign;
import com.walmart.ads.campaign.repository.CampaignRepository;
import com.walmart.ads.common.dto.CampaignDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {
    
    @Mock
    private CampaignRepository campaignRepository;
    
    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    @InjectMocks
    private CampaignService campaignService;
    
    private CampaignDTO campaignDTO;
    private Campaign campaign;
    
    @BeforeEach
    void setUp() {
        campaignDTO = new CampaignDTO();
        campaignDTO.setName("Test Campaign");
        campaignDTO.setAdvertiserId("adv-001");
        campaignDTO.setBudget(new BigDecimal("10000.00"));
        campaignDTO.setStartDate(LocalDateTime.now());
        campaignDTO.setEndDate(LocalDateTime.now().plusDays(30));
        campaignDTO.setStatus(CampaignDTO.CampaignStatus.ACTIVE);
        
        campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Test Campaign");
        campaign.setAdvertiserId("adv-001");
        campaign.setBudget(new BigDecimal("10000.00"));
        campaign.setStartDate(LocalDateTime.now());
        campaign.setEndDate(LocalDateTime.now().plusDays(30));
        campaign.setStatus(Campaign.CampaignStatus.ACTIVE);
    }
    
    @Test
    void testCreateCampaign() {
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);
        
        CampaignDTO result = campaignService.createCampaign(campaignDTO);
        
        assertNotNull(result);
        assertEquals(campaignDTO.getName(), result.getName());
        verify(campaignRepository, times(1)).save(any(Campaign.class));
        verify(kafkaTemplate, times(1)).send(eq("campaign-events"), eq("campaign.created"), any());
    }
    
    @Test
    void testGetCampaignById() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        
        CampaignDTO result = campaignService.getCampaignById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(campaignRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetCampaignByIdNotFound() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            campaignService.getCampaignById(1L);
        });
    }
}
