package com.amigoscode.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification, Long> {
    
    List<CustomerNotification> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);
    
    List<CustomerNotification> findByCustomerIdAndIsReadOrderByCreatedAtDesc(Integer customerId, Boolean isRead);
    
    List<CustomerNotification> findByCustomerIdAndPriorityOrderByCreatedAtDesc(Integer customerId, NotificationPriority priority);
    
    @Query("SELECT COUNT(n) FROM CustomerNotification n WHERE n.customerId = :customerId AND n.isRead = false")
    Long countUnreadNotifications(@Param("customerId") Integer customerId);
    
    @Query("SELECT n FROM CustomerNotification n WHERE n.customerId = :customerId AND n.createdAt >= :startDate ORDER BY n.createdAt DESC")
    List<CustomerNotification> findRecentNotifications(@Param("customerId") Integer customerId, @Param("startDate") LocalDateTime startDate);
    
    @Modifying
    @Query("UPDATE CustomerNotification n SET n.isRead = true, n.readAt = :readAt WHERE n.customerId = :customerId AND n.isRead = false")
    void markAllAsRead(@Param("customerId") Integer customerId, @Param("readAt") LocalDateTime readAt);
    
    @Modifying
    @Query("UPDATE CustomerNotification n SET n.isRead = true, n.readAt = :readAt WHERE n.id = :notificationId")
    void markAsRead(@Param("notificationId") Long notificationId, @Param("readAt") LocalDateTime readAt);
    
    List<CustomerNotification> findByIsSentFalse();
} 