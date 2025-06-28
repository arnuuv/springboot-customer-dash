package com.amigoscode.customer;

import java.util.List;

public record BulkNotificationRequest(
        List<Integer> customerIds,
        String title,
        String message,
        NotificationPriority priority
) {
} 