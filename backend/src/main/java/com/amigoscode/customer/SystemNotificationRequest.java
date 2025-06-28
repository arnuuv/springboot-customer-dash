package com.amigoscode.customer;

public record SystemNotificationRequest(
        String title,
        String message,
        NotificationPriority priority
) {
} 