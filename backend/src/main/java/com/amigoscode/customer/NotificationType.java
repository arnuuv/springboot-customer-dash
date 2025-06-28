package com.amigoscode.customer;

public enum NotificationType {
    WELCOME("Welcome Notification"),
    PROFILE_UPDATE("Profile Update"),
    PASSWORD_CHANGE("Password Change"),
    ACCOUNT_SECURITY("Security Alert"),
    SYSTEM_MAINTENANCE("System Maintenance"),
    NEW_FEATURE("New Feature"),
    DATA_EXPORT("Data Export"),
    DATA_IMPORT("Data Import"),
    ACTIVITY_SUMMARY("Activity Summary"),
    CUSTOM("Custom Notification");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 