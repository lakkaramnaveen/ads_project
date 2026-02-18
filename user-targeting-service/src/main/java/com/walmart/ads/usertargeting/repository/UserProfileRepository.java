package com.walmart.ads.usertargeting.repository;

import com.walmart.ads.usertargeting.model.UserProfile;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProfileRepository extends CassandraRepository<UserProfile, String> {
    List<UserProfile> findBySegmentsContaining(String segment);
    List<UserProfile> findByLocation(String location);
}
