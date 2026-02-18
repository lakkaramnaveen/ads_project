package com.walmart.ads.usertargeting.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Table("user_profiles")
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

    public UserProfile() {}

    public UserProfile(
            String userId,
            String email,
            String firstName,
            String lastName,
            Integer age,
            String gender,
            String location,
            List<String> interests,
            List<String> segments,
            Map<String, Object> demographics,
            Map<String, Object> behaviorData,
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

    public Map<String, Object> getDemographics() {
        return demographics;
    }

    public void setDemographics(Map<String, Object> demographics) {
        this.demographics = demographics;
    }

    public Map<String, Object> getBehaviorData() {
        return behaviorData;
    }

    public void setBehaviorData(Map<String, Object> behaviorData) {
        this.behaviorData = behaviorData;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
