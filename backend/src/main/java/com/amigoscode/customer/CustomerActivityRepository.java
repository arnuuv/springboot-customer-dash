package com.amigoscode.customer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerActivityRepository extends JpaRepository<CustomerActivity, Long> {
    
    List<CustomerActivity> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);
    
    List<CustomerActivity> findByCustomerIdAndActivityTypeOrderByCreatedAtDesc(
            Integer customerId, ActivityType activityType);
    
    @Query("SELECT ca FROM CustomerActivity ca WHERE ca.createdAt >= :startDate ORDER BY ca.createdAt DESC")
    List<CustomerActivity> findRecentActivities(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT ca FROM CustomerActivity ca WHERE ca.customerId = :customerId AND ca.createdAt >= :startDate ORDER BY ca.createdAt DESC")
    List<CustomerActivity> findCustomerRecentActivities(
            @Param("customerId") Integer customerId, 
            @Param("startDate") LocalDateTime startDate);
} 