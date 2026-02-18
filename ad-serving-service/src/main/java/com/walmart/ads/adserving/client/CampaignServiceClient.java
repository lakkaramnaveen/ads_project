package com.walmart.ads.adserving.client;

import com.walmart.ads.common.dto.CampaignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "campaign-service")
public interface CampaignServiceClient {
    @GetMapping("/api/campaigns/active")
    List<CampaignDTO> getActiveCampaigns();
    
    @GetMapping("/api/campaigns/{id}")
    CampaignDTO getCampaign(@PathVariable("id") Long id);
}
