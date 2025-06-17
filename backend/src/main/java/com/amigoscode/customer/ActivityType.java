package com.amigoscode.customer;

public enum ActivityType {
    LOGIN("User Login"),
    LOGOUT("User Logout"),
    PROFILE_UPDATE("Profile Updated"),
    PROFILE_IMAGE_UPLOAD("Profile Image Uploaded"),
    PASSWORD_CHANGE("Password Changed"),
    ACCOUNT_CREATED("Account Created"),
    ACCOUNT_DELETED("Account Deleted"),
    DATA_EXPORT("Data Exported"),
    DATA_IMPORT("Data Imported"),
    SEARCH_PERFORMED("Search Performed"),
    ANALYTICS_VIEWED("Analytics Viewed");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 