package com.walmart.ads.common.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    private Map<String, String> demographics;
    private Map<String, String> behaviorData; // Browsing history, purchase history
    private LocalDateTime lastUpdated;

    public UserProfileDTO() {}

    public UserProfileDTO(
            String userId,
            String email,
            String firstName,
            String lastName,
            Integer age,
            String gender,
            String location,
            List<String> interests,
            List<String> segments,
            Map<String, String> demographics,
            Map<String, String> behaviorData,
            LocalDateTime lastUpdated
    ) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.interests = interests;
        this.segments = segments;
        this.demographics = demographics;
        this.behaviorData = behaviorData;
        this.lastUpdated = lastUpdated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getSegments() {
        return segments;
    }

    public void setSegments(List<String> segments) {
        this.segments = segments;
    }

    public Map<String, String> getDemographics() {
        return demographics;
    }

    public void setDemographics(Map<String, String> demographics) {
        this.demographics = demographics;
    }

    public Map<String, String> getBehaviorData() {
        return behaviorData;
    }

    public void setBehaviorData(Map<String, String> behaviorData) {
        this.behaviorData = behaviorData;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
