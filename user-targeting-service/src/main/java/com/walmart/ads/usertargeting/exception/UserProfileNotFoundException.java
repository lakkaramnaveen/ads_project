package com.walmart.ads.usertargeting.exception;

public class UserProfileNotFoundException extends RuntimeException {
    public UserProfileNotFoundException(String userId) {
        super("User profile not found: " + userId);
    }
}
