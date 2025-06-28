package com.amigoscode.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomerNotificationService {

    private final CustomerNotificationRepository notificationRepository;
    private final CustomerDao customerDao;

    public CustomerNotificationService(CustomerNotificationRepository notificationRepository, 
                                     CustomerDao customerDao) {
        this.notificationRepository = notificationRepository;
        this.customerDao = customerDao;
    }

    public void createNotification(Integer customerId, NotificationType type, 
                                 String title, String message, NotificationPriority priority) {
        CustomerNotification notification = new CustomerNotification(customerId, type, title, message, priority);
        notificationRepository.save(notification);
    }

    public void createWelcomeNotification(Integer customerId, String customerName) {
        createNotification(
            customerId,
            NotificationType.WELCOME,
            "Welcome to Our Platform!",
            "Hello " + customerName + "! Welcome to our customer management platform. We're excited to have you on board.",
            NotificationPriority.NORMAL
        );
    }

    public void createProfileUpdateNotification(Integer customerId) {
        createNotification(
            customerId,
            NotificationType.PROFILE_UPDATE,
            "Profile Updated Successfully",
            "Your profile information has been updated successfully.",
            NotificationPriority.LOW
        );
    }

    public void createSecurityNotification(Integer customerId, String action) {
        createNotification(
            customerId,
            NotificationType.ACCOUNT_SECURITY,
            "Security Alert",
            "Security action detected: " + action + ". If this wasn't you, please contact support immediately.",
            NotificationPriority.HIGH
        );
    }

    public void createSystemMaintenanceNotification(Integer customerId, String maintenanceInfo) {
        createNotification(
            customerId,
            NotificationType.SYSTEM_MAINTENANCE,
            "System Maintenance Scheduled",
            "Scheduled maintenance: " + maintenanceInfo + ". Service may be temporarily unavailable.",
            NotificationPriority.NORMAL
        );
    }

    public void createNewFeatureNotification(Integer customerId, String featureName) {
        createNotification(
            customerId,
            NotificationType.NEW_FEATURE,
            "New Feature Available!",
            "We've added a new feature: " + featureName + ". Check it out in your dashboard!",
            NotificationPriority.NORMAL
        );
    }

    public List<CustomerNotification> getCustomerNotifications(Integer customerId) {
        return notificationRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public List<CustomerNotification> getUnreadNotifications(Integer customerId) {
        return notificationRepository.findByCustomerIdAndIsReadOrderByCreatedAtDesc(customerId, false);
    }

    public List<CustomerNotification> getNotificationsByPriority(Integer customerId, NotificationPriority priority) {
        return notificationRepository.findByCustomerIdAndPriorityOrderByCreatedAtDesc(customerId, priority);
    }

    public Long getUnreadCount(Integer customerId) {
        return notificationRepository.countUnreadNotifications(customerId);
    }

    public List<CustomerNotification> getRecentNotifications(Integer customerId, int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return notificationRepository.findRecentNotifications(customerId, startDate);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId, LocalDateTime.now());
    }

    public void markAllAsRead(Integer customerId) {
        notificationRepository.markAllAsRead(customerId, LocalDateTime.now());
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void sendBulkNotification(List<Integer> customerIds, String title, String message, NotificationPriority priority) {
        for (Integer customerId : customerIds) {
            createNotification(customerId, NotificationType.CUSTOM, title, message, priority);
        }
    }

    public void sendSystemNotification(String title, String message, NotificationPriority priority) {
        List<Customer> allCustomers = customerDao.selectAllCustomers();
        for (Customer customer : allCustomers) {
            createNotification(customer.getId(), NotificationType.SYSTEM_MAINTENANCE, title, message, priority);
        }
    }

    public List<CustomerNotification> getPendingNotifications() {
        return notificationRepository.findByIsSentFalse();
    }

    public void markAsSent(Long notificationId) {
        CustomerNotification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsSent(true);
            notificationRepository.save(notification);
        }
    }
} 