package com.walmart.ads.adserving.repository;

import com.walmart.ads.adserving.model.AdImpression;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdImpressionRepository extends CassandraRepository<AdImpression, UUID> {
    List<AdImpression> findByUserId(String userId);
    List<AdImpression> findByCampaignId(String campaignId);
}
