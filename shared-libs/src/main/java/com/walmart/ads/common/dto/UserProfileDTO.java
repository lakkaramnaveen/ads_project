package com.walmart.ads.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
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
