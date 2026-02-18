package com.walmart.ads.usertargeting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Table("user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @PrimaryKey
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String location;
    private List<String> interests;
    private List<String> segments; // User segments for targeting
    private Map<String, Object> demographics;
    private Map<String, Object> behaviorData; // Browsing history, purchase history
    private LocalDateTime lastUpdated;
}
