package com.walmart.ads.campaign.controller;

import com.walmart.ads.common.dto.ApiResponse;
import com.walmart.ads.common.dto.CampaignDTO;
import com.walmart.ads.campaign.service.CampaignService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {
    
    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<CampaignDTO>> createCampaign(@Valid @RequestBody CampaignDTO campaignDTO) {
        CampaignDTO created = campaignService.createCampaign(campaignDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Campaign created successfully", created));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignDTO>> getCampaign(@PathVariable Long id) {
        CampaignDTO campaign = campaignService.getCampaignById(id);
        return ResponseEntity.ok(ApiResponse.success(campaign));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<CampaignDTO>>> getAllCampaigns(
            @RequestParam(required = false) String advertiserId) {
        List<CampaignDTO> campaigns;
        if (advertiserId != null) {
            campaigns = campaignService.getCampaignsByAdvertiser(advertiserId);
        } else {
            campaigns = campaignService.getAllCampaigns();
        }
        return ResponseEntity.ok(ApiResponse.success(campaigns));
    }
    
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CampaignDTO>>> getActiveCampaigns() {
        List<CampaignDTO> campaigns = campaignService.getActiveCampaigns();
        return ResponseEntity.ok(ApiResponse.success(campaigns));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CampaignDTO>> updateCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CampaignDTO campaignDTO) {
        CampaignDTO updated = campaignService.updateCampaign(id, campaignDTO);
        return ResponseEntity.ok(ApiResponse.success("Campaign updated successfully", updated));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.ok(ApiResponse.success("Campaign deleted successfully", null));
    }
}
