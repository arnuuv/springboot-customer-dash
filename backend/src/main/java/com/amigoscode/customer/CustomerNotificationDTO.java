package com.amigoscode.customer;

import java.time.LocalDateTime;

public record CustomerNotificationDTO(
        Long id,
        Integer customerId,
        NotificationType notificationType,
        String title,
        String message,
        Boolean isRead,
        Boolean isSent,
        NotificationPriority priority,
        LocalDateTime createdAt,
        LocalDateTime readAt
) {
} 